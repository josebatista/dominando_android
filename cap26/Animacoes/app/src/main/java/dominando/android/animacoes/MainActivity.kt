package dominando.android.animacoes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        startActivity(Intent(this, ViewAnimationsActivity::class.java))
//        startActivity(Intent(this, PropertyAnimationsActivity::class.java))
//        startActivity(Intent(this, SpriteActivity::class.java))
//        startActivity(Intent(this, LayoutChangesActivity::class.java))
//        startActivity(Intent(this, TransitionActivity::class.java))
        startActivity(Intent(this, RevealActivity::class.java))
    }
}
