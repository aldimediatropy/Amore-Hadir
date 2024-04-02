package com.setalis.amorehr.base

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.setalis.amorehr.R
import com.setalis.amorehr.utils.internet.AmConnectionInterface
import com.setalis.amorehr.utils.internet.AmConnectionReceiver

abstract class BaseActivity<T: ViewBinding> : AmActivity(), AmConnectionInterface, LifecycleOwner {

    lateinit var binding: T
    /**
     * Object for specific ui handler
     */
    //private val parentNavigation = setOf(R.id.boardingFragment, R.id.boardingWelcome, R.id.mainFragment, R.id.billingPlanFragment)

    /**
     * Internet network state callback
     */
    private var connectionReceiver: AmConnectionReceiver? = null
    private var connectionChecker: Boolean = false

    private var navHostFragment: NavHostFragment? = null

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
            onBackPressedDispatcher.addCallback(this@BaseActivity, object: OnBackPressedCallback(true) {
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

    protected var toolbarState = false

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

    fun setupNavigationController() {
        val navView: BottomNavigationView? = findViewById(R.id.nav_view)

        if(navView != null) {
            navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?
            navController = navHostFragment?.navController

            val topLevel = setOf(R.id.navigation_home, R.id.navigation_time_off, R.id.navigation_pay_slips, R.id.navigation_profile)

            val appBarConfiguration = AppBarConfiguration(
                topLevelDestinationIds = topLevel,
                fallbackOnNavigateUpListener = ::onSupportNavigateUp
            )

            navController?.let {
                findViewById<MaterialToolbar>(R.id.topAppBar).setupWithNavController(it, appBarConfiguration)
                navView.setupWithNavController(it)
                it.addOnDestinationChangedListener { _, destination, arguments ->
                    findViewById<MaterialToolbar>(R.id.topAppBar)?.apply {
                        menu?.clear()
                        if(destination.id == R.id.navigation_home) {
                            menuInflater.inflate(R.menu.top_menu, this.menu)
                        }else if(destination.id == R.id.navigation_time_off) {
                            menuInflater.inflate(R.menu.time_off_menu, this.menu)
                        }else if(destination.id == R.id.navigation_profile) {
                            menuInflater.inflate(R.menu.refresh_menu, this.menu)
                        }
                    }
                    navView.isVisible = topLevel.contains(destination.id)
                    findViewById<FloatingActionButton>(R.id.buttonCenter).isVisible = topLevel.contains(destination.id)

                    if((arguments?.getBoolean("ShowAppBarBack", true) == false)) {
                        if(topLevel.contains(destination.id)) {
                            findViewById<MaterialToolbar>(R.id.topAppBar).navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_notification)
                        }
                    }
                }
            }
        }
    }

}