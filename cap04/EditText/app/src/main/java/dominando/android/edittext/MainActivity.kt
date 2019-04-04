package dominando.android.edittext

import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtPassword.setOnEditorActionListener { v, actionId, event ->
            if (v == edtPassword && EditorInfo.IME_ACTION_DONE == actionId) {
                registerUser()
            }
            false
        }
    }

    fun registerUser() {
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        var isValid = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = getString(R.string.msg_error_email)
            isValid = false
        }
        if (password != "123") {
            edtPassword.error = getString(R.string.msg_error_password)
            isValid = false
        }
        if (isValid) {
            Toast.makeText(
                this,
                getString(R.string.msg_success, name, email),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
