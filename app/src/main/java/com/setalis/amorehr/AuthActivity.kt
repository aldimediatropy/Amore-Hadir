package com.setalis.amorehr

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.setalis.amorehr.base.BaseActivity
import com.setalis.amorehr.databinding.ActivityAuthBinding
import com.setalis.amorehr.utils.commons.Constants.REQUEST.PERMISSION_LOCATION

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun viewBinding() = ActivityAuthBinding.inflate(layoutInflater)

    override fun userInterface() {
        super.userInterface()

        setupNavigationController()

        getLocation()
    }

    private fun getLocation() {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_LOCATION)
        }
    }
}