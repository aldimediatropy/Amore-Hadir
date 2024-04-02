package com.setalis.amorehr

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.setalis.amorehr.base.BaseActivity
import com.setalis.amorehr.base.attend
import com.setalis.amorehr.data.entities.User
import com.setalis.amorehr.databinding.ActivityMainBinding
import com.setalis.amorehr.utils.commons.Constants.REQUEST.PERMISSION_CAMERA
import com.setalis.amorehr.utils.commons.Constants.REQUEST.PERMISSION_LOCATION


class MainActivity : BaseActivity<ActivityMainBinding>(), LocationListener {

    override fun viewBinding() = ActivityMainBinding.inflate(layoutInflater)

    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    var userChanged: ((User) -> Unit)? = null
    var locationChanged: ((Location) -> Unit)? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationManager: LocationManager
    private var location: Location? = null

    override fun userInterface() {
        super.userInterface()
        setupNavigationController()
        getLocation()

        user()

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sign_out -> {
                    logout()
                    true
                }

                R.id.add_time_off -> {
                    navController?.attend()
                    true
                }

                R.id.refresh -> {
                    fetchUser()
                    true
                }

                else -> false
            }
        }

        pickMedia = registerForActivityResult(
            PickVisualMedia()
        ) { uri: Uri? ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun listener() {
        super.listener()

        binding.buttonCenter.setOnClickListener {
            navController?.attend()
        }
    }

    private fun getCamera() {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA)
        }
    }

    private fun getLocation() {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_LOCATION)
        }else{
            getCamera()
            //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE, 0f, this)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        message("Cannot get location.")
                    else {
                        this.location = location

                        locationChanged?.invoke(location)
                        binding?.buttonCenter?.setImageResource(R.drawable.ic_clock)
                        binding?.loaderLocation?.isVisible = false

                    }

                }
        }
    }

    override fun onLocationChanged(location: Location) {
        this.location = location
        //message(location.toString())
        locationChanged?.invoke(location)
        binding?.buttonCenter?.setImageResource(R.drawable.ic_clock)
        binding?.loaderLocation?.isVisible = false
    }

    fun location() = location

    override fun observer() {
        super.observer()

        userViewModel.user.observe(this) {
            it.getEventIfNotHandled()?.let { data ->
                this.user = data
                userChanged?.invoke(data)
            }
        }
    }
}