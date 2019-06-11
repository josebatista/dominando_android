package dominando.android.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        txtMessage.text = intent.getStringExtra(EXTRA_MESSAGE)

    }

    companion object {
        const val EXTRA_MESSAGE = "message"
    }

}
