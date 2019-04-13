package dominando.android.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jogoDaVelha.listener = object : JogoDaVelhaView.JogoDaVelhaListener {
            override fun fimDeJogo(vencedor: Int) {
                val message = when (vencedor) {
                    JogoDaVelhaView.XIS -> "X venceu"
                    JogoDaVelhaView.BOLA -> "O venceu"
                    else -> "Empatou"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        button.setOnClickListener { jogoDaVelha.reiniciarJogo() }
    }
}
