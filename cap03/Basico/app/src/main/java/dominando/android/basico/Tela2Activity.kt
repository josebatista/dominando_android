package dominando.android.basico

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tela2.*

class Tela2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela2)

        val nome = intent.getStringExtra("nome")
        val idade = intent.getIntExtra("idade", -1)
        val cliente = intent.getParcelableExtra<Cliente>("cliente")
        val pessoa = intent.getSerializableExtra("pessoa") as Pessoa? //o objeto pessoa pode ser nulo

        textMensagem.text = if (cliente != null) {
            "Nome: ${cliente.nome} / Código: ${cliente.codigo}"
        } else if (pessoa != null) {
            "Nome: ${pessoa.nome} / Idade: ${pessoa.idade}"
        } else {
            "Nome: $nome / Idade: $idade"
        }
    }
}
