package dominando.android.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), InternetSearchView {

    val presenter = InternetSearchPresenter(this, GoogleInternetSearch())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSearch.setOnClickListener {
            presenter.search(editText.text.toString())
        }
    }

    override fun showProgress() {
        //EXIBIR MENSAGEM DE PROGRESSO (EX.: PROCURANDO...)
    }

    override fun hideProgress() {
        //OCULTAR PROGRESSO
    }

    override fun showResults(results: List<SearchResult>) {
        //EXIBIR O RESULTADO (EM UMA RECYCLERVIEW, POR EXEMPLO)
    }

    override fun showSearchError() {
        //EXIBIR MENSAGEM DE ERRO
    }
}
