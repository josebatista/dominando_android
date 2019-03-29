package dominando.android.basico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.parceler.Parcels

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "Tela1::onCreate")

        buttonToast.setOnClickListener {
            val texto = editTexto.text.toString()
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }

        buttonTela2.setOnClickListener {
            val intent = Intent(this, Tela2Activity::class.java)
            intent.putExtra("nome", "José")
            intent.putExtra("idade", 32)
            startActivity(intent)
        }

        buttonParcel.setOnClickListener {
            //val c1 = Cliente() // codigo = 0, nome = ""
            //val c2 = Cliente(10) // codigo = 10, nome = ""
            //val c3 = Cliente(12, "José") // codigo = 12, nome = "José"
            //val c4 = Cliente(nome="José") // codigo = 0, nome = "José"

            val cliente = Cliente(1, "José")
            val intent = Intent(this, Tela2Activity::class.java)
//            intent.putExtra("cliente", cliente)
            intent.putExtra("cliente", Parcels.wrap(cliente))
            startActivity(intent)
        }

        buttonSerializable.setOnClickListener {
            val intent = Intent(this, Tela2Activity::class.java)
            intent.putExtra("pessoa", Pessoa("José Pereira", 32))
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Tela1::onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "Tela1::onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "Tela1::onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "Tela1::onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "Tela1::onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Tela1::onDestroy")
    }
}
