package com.setalis.amorehr.views.attend

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.setalis.amorehr.MainActivity
import com.setalis.amorehr.R
import com.setalis.amorehr.base.AmFragment
import com.setalis.amorehr.commons.AmLog
import com.setalis.amorehr.databinding.FragmentAttendBinding
import com.setalis.amorehr.distance
import com.setalis.amorehr.now
import com.setalis.amorehr.viewmodels.AttendViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */

class AttendFragment: AmFragment<FragmentAttendBinding, MainActivity>() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var handler: Handler

    private val attendViewModel: AttendViewModel by viewModel()

    override fun viewBinding() = FragmentAttendBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()

        amActivity?.binding?.topAppBar?.menu?.clear()
    }

    override fun userInterface(context: Context) {
        super.userInterface(context)

        startCamera()

        handler = Handler()
        startClock()

        cameraExecutor = Executors.newSingleThreadExecutor()

        amActivity?.user?.let { user ->
            binding.apply {
                labelName.text = user.name + " - " + user.position
                labelDate.text = now()

                amActivity?.location()?.let {
                    val dist = distance(it.latitude, it.longitude, user.latitude ?: 0.0, user.longitude ?: 0.0)
                    val formattedDist = "%.1f".format(dist)

                    val color = when {
                        dist <= (user.radius ?: 0) * 0.5 -> R.color.yellow
                        dist >= (user.radius ?: 0) * 1.5 -> R.color.red
                        else -> R.color.green
                    }

                    binding.labelLocation.text = it.latitude.toString() + "," + it.longitude.toString()
                    binding.labelDistance.setTextColor(ContextCompat.getColor(context, color))
                    binding.labelDistance.text = "$formattedDist Km"
                }
            }
        }
    }

    private fun startCamera() {
        amActivity?.let {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(it)

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get()

                val screenSize = Size(640, 480)
                val resolutionSelector = ResolutionSelector.Builder().setResolutionStrategy(ResolutionStrategy(screenSize,
                    ResolutionStrategy.FALLBACK_RULE_NONE)).build()
                // Preview
                val preview = Preview.Builder()
                    .setResolutionSelector(resolutionSelector)
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder()
                    .setJpegQuality(10)
                    .setResolutionSelector(resolutionSelector)
                    .build()

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider?.unbindAll()

                    // Bind use cases to camera
                    cameraProvider?.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture)

                } catch(exc: Exception) {
                    AmLog.e("Use case binding failed$exc")
                }

            }, ContextCompat.getMainExecutor(it))
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        amActivity?.contentResolver?.let {
            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(it,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
                .build()

            // Set up image capture listener, which is triggered after photo has
            // been taken
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        AmLog.e("Photo capture failed: ${exc.message}")
                    }

                    override fun
                            onImageSaved(output: ImageCapture.OutputFileResults){

                        val savedUri = output.savedUri ?: return
                        // Display the captured image in an ImageView
                        displayCapturedImage(savedUri)

                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        AmLog.e(msg)
                    }
                }
            )
        }
    }

    private fun stopCamera() {
        // Unbind all use cases to release the camera resources
        cameraProvider?.unbindAll()
        cameraProvider = null
    }

    private fun displayCapturedImage(capturedUri: Uri) {
        handler.removeCallbacks(runnable)

        // Load the captured image into an ImageView
        Glide.with(requireContext())
            .load(capturedUri) // Load image from URI
            .into(binding.photoView) // Bind image to ImageView
        binding.photoView.scaleX = -1f

        binding.viewFinder.visibility = View.INVISIBLE
        binding.buttonCapture.isVisible = false
        stopCamera()

        amActivity?.location()?.let {
            Handler().postDelayed({
                val photo = "data:image/png;base64," + viewToBase64(binding.viewFrame)
                attendViewModel.attend(latitude = it.latitude, it.longitude, photo)
            }, 600)
        }
    }

    private fun startClock() {
        handler.post(runnable)
    }

    private fun updateClock() {
        val currentTime = Calendar.getInstance().time
        // Format the time as desired (e.g., HH:mm:ss for hours, minutes, and seconds)
        val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentTime)
        // Update the TextView with the formatted time
        binding.labelTime.text = formattedTime
    }

    private val runnable = object : Runnable {
        override fun run() {
            updateClock()
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun listener() {
        super.listener()

        binding.buttonCapture.setOnClickListener {
            takePhoto()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        cameraExecutor.shutdown()
    }

    override fun observer() {
        super.observer()

        attendViewModel.apply {
            loading.observe(viewLifecycleOwner) { amActivity?.loader(it.getEventIfNotHandled() == true) }
            attend.observe(viewLifecycleOwner) {
                it.getEventIfNotHandled()?.let {
                    amActivity?.onSupportNavigateUp()
                    amActivity?.message("Okay")
                }
            }
        }

    }

    fun viewToBase64(view: View): String {
        // Capture the screenshot of the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Convert the bitmap to Base64
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}