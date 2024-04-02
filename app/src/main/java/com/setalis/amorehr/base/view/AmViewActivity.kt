package com.setalis.amorehr.base.view

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.setalis.amorehr.R
import com.setalis.amorehr.base.AmActivity
import com.setalis.amorehr.utils.internet.AmConnectionInterface
import com.setalis.amorehr.utils.internet.AmConnectionReceiver

abstract class AmViewActivity<T: ViewBinding> : AmActivity(), AmConnectionInterface, LifecycleOwner {

    lateinit var binding: T

    /**
     * Internet network state callback
     */
    private var connectionReceiver: AmConnectionReceiver? = null
    private var connectionChecker: Boolean = false

    internal var back: (() -> Unit?)? = null

    abstract fun viewBinding(): T

    /** 1
     *  Default constructor for Activity. to define onCreate, called before binding a view
     */
    protected open fun initialize() {}

    /** 2
     *  Initialize user interface
     */
    protected open fun userInterface() {}

    /** 3
     *  Action or listener for any object
     */
    protected open fun listener() {}

    /** 4
     *  Observe ViewModel
     */
    protected open fun observer() {}

    /** 5
     *  Handle back pressed by function invoke object
     */
    private fun backPressed() {
        back?.apply {
            onBackPressedDispatcher.addCallback(this@AmViewActivity, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    this@apply.invoke()
                }
            })
        }
    }

    /** 5
     * Handling intent data
     */
    protected open fun intent() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        initialize()
        connection(this)

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (this::binding.isInitialized) {
                binding
                setContentView(binding.root)
            } else {
                binding = viewBinding()
                setContentView(binding.root)

                intent()
                userInterface()
                listener()
                observer()
                backPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        connection(this)
    }

    override fun onPause() {
        super.onPause()
        destroyConnection()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyConnection()
    }

    /**
     *  Internet state change
     */
    override fun onConnectionChange(message: String) {
        if (message == getString(R.string.system_no_internet_connection)) message(message)
    }

    private fun connection(receiver: AmConnectionInterface) {
        if (connectionReceiver == null) {
            connectionReceiver = AmConnectionReceiver(this)
            connectionReceiver?.apply {
                val intentFilter = IntentFilter()

                registerReceiver(receiver)
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
                registerReceiver(connectionReceiver, intentFilter)
                connectionChecker = true
            }
        }
    }

    private fun destroyConnection() {
        if (connectionChecker) {
            unregisterReceiver(connectionReceiver)
            connectionChecker = false
        }
    }

}
