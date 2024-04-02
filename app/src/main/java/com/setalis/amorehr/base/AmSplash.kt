package com.setalis.amorehr.base

import com.setalis.amorehr.databinding.ActivitySplashBinding
import java.util.Timer
import kotlin.concurrent.timerTask


class AmSplash :  BaseActivity<ActivitySplashBinding>() {

    override fun viewBinding() = ActivitySplashBinding.inflate(layoutInflater)

    private var timerAnimate = Timer()
    private var countAnimate = 0

    override fun userInterface() {
        super.userInterface()
        animate()
    }

    private fun animate() {
        timerAnimate = Timer()
        timerAnimate.scheduleAtFixedRate(timerTask {
            countAnimate++

            if (countAnimate > 6) {
                load()
                timerAnimate.cancel()
                finish()
            }

        }, 0, 500)
    }

    private fun load() {
        runOnUiThread {
            val stateLogin = this.session().isLogin

            if(stateLogin) {
                goToMain()
            }else{
                goToAuth()
            }
        }
    }

}