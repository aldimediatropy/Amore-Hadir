package com.setalis.amorehr.views.profile

import android.content.Context
import com.setalis.amorehr.MainActivity
import com.setalis.amorehr.base.AmFragment
import com.setalis.amorehr.databinding.FragmentProfileBinding


/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */

class ProfileFragment: AmFragment<FragmentProfileBinding, MainActivity>() {

    override fun viewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()

    }

    override fun userInterface(context: Context) {
        super.userInterface(context)

        amActivity?.userChanged = {
            binding.apply {
                labelName.text = it.name
                labelPhone.text = it.telpon
                labelEmail.text = it.email
                labelShift.text = it.shift + "("+it.clock_in+" - "+it.clock_out+")"
                labelPosition.text = it.company + " - " + it.position
            }
        }

        amActivity?.fetchUser()
    }
}