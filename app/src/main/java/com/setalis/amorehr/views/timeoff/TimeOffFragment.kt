package com.setalis.amorehr.views.timeoff

import android.content.Context
import com.setalis.amorehr.MainActivity
import com.setalis.amorehr.base.AmFragment
import com.setalis.amorehr.databinding.FragmentTimeOffBinding


/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */

class TimeOffFragment: AmFragment<FragmentTimeOffBinding, MainActivity>() {

    override fun viewBinding() = FragmentTimeOffBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
    }

    override fun userInterface(context: Context) {
        super.userInterface(context)

    }
}