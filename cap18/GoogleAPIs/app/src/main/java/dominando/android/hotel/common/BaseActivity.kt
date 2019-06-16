package dominando.android.hotel.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dominando.android.hotel.auth.AuthManager
import dominando.android.hotel.auth.LoginActivity
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    private val authManager: AuthManager by inject()

    override fun onStart() {
        super.onStart()
        verifyUserLoggedIn()
    }

    fun verifyUserLoggedIn() {
        val account = authManager.getUserAccount()
        if (account == null) {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            finish()
        }
    }

}