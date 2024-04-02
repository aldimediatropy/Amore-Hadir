package com.setalis.amorehr.base

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.setalis.amorehr.AuthActivity
import com.setalis.amorehr.BuildConfig
import com.setalis.amorehr.MainActivity
import com.setalis.amorehr.R
import com.setalis.amorehr.commons.AmToast
import com.setalis.amorehr.commons.managers.SessionManager
import com.setalis.amorehr.data.entities.User
import com.setalis.amorehr.viewmodels.UserViewModel
import com.setalis.components.extensions.emptyString
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


open class AmActivity : AppCompatActivity() {

    internal var navController: NavController? = null

    private val session by inject<SessionManager>()

    var user: User? = null

    internal val userViewModel: UserViewModel by viewModel()

    /**
     *  Showing toast message, toast will showing if showErrorToast from Preference is false
     */
    fun message(message: String) {
        // Give an user access to disabling the annoying toast message
        if (message.isNotBlank()) {
            when (message) {
                getString(R.string.system_no_internet_connection) -> {
                    runOnUiThread {
                        // navController?.dialogNoInternet()
                    }
                }
                else -> {
                    AmToast(this, message)
                }
            }
        }

        // if (!Preferences(this, Constants.Preference.Setting.showErrorToast).getBoolean()) { }
    }

    /**
     *  Dismiss any keyboard once user tap outside the input
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is AppCompatEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    internal fun session() = session

    internal fun user(): User? {
        if(user == null) {
            runBlocking {
                fetchUser()
            }
        }

        return user
    }

    internal fun setUser(data: User) {
        this.user = data
    }

    internal fun fetchUser() {
        userViewModel.user()
    }

    fun loader(state: Boolean) {
        findViewById<LinearProgressIndicator?>(R.id.loading)?.isVisible = state
    }

    fun error(text: String? = null) {
        if(!text.isNullOrEmpty() && BuildConfig.DEBUG) {
            message(text)
        }
    }

    fun goToMain() {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent, options.toBundle())
        finishAffinity()
    }

    fun logout() {
        session().apply {
            session.isLogin = false
            session.tokenAccess = emptyString()
            goToAuth()
        }
    }

    fun goToAuth() {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        val intent = Intent(this, AuthActivity::class.java)

        startActivity(intent, options.toBundle())
        finishAffinity()

    }
}
