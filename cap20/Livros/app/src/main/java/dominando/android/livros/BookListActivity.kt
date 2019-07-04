package dominando.android.livros

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dominando.android.livros.model.Book
import dominando.android.livros.model.MediaType
import dominando.android.livros.model.Publisher
import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

//        BookDetailsActivity.start(
//            this,
//            Book().apply {
//                id = "1"
//                title = "Dominando o Android com Kotlin"
//                author = "Nelson Glauber"
//                coverUrl = "https://s3.novatec.com.br/capas-ampliadas/capa-ampliada-9788575224632.jpg"
//                pages = 954
//                year = 2018
//                publisher = Publisher("1", "Novatec")
//                available = true
//                mediaType = MediaType.PAPER
//                rating = 5.0f
//            }
//        )

//        BookFormActivity.start(
//            this,
//            Book().apply {
//                id = "1"
//                title = "Dominando o Android com Kotlin"
//                author = "Nelson Glauber"
//                coverUrl = "https://s3.novatec.com.br/capas-ampliadas/capa-ampliada-9788575224632.jpg"
//                pages = 954
//                year = 2018
//                publisher = Publisher("1", "Novatec")
//                available = true
//                mediaType = MediaType.PAPER
//                rating = 5.0f
//            }
//        )

        val books = listOf(
            Book().apply {
                id = "1"
                title = "Dominando o Android"
                author = "Nelson Glauber"
                coverUrl = "https://s3.novatec.com.br/capas-ampliadas/capa-ampliada-9788575224632.jpg"
                pages = 954
                year = 2018
                publisher = Publisher("1", "Novatec")
                available = true
                mediaType = MediaType.PAPER
                rating = 5.0f
            }
        )

        rvBooks.layoutManager = LinearLayoutManager(this)
        rvBooks.adapter = BookAdapter(books) { book ->
            BookDetailsActivity.start(this, book)
        }

    }
}
