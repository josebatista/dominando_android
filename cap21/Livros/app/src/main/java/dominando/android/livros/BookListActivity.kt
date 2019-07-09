package dominando.android.livros

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dominando.android.livros.model.Book
import dominando.android.livros.viewmodel.BookListViewModel
import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : BaseActivity() {

    private val viewModel: BookListViewModel by lazy {
        ViewModelProviders.of(this).get(BookListViewModel::class.java)
    }

    override fun init() {
        try {
            viewModel.getBooks().observe(this, Observer { books ->
                updateList(books)
            })
        } catch (e: Exception) {
            Toast.makeText(this, R.string.message_error_load_books, Toast.LENGTH_SHORT).show()
        }

    }

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

//        val books = listOf(
//            Book().apply {
//                id = "1"
//                title = "Dominando o Android"
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

//        rvBooks.layoutManager = LinearLayoutManager(this)
//        rvBooks.adapter = BookAdapter(books) { book ->
//            BookDetailsActivity.start(this, book)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.book_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateList(books: List<Book>) {
        rvBooks.layoutManager = LinearLayoutManager(this)
        rvBooks.adapter = BookAdapter(books) { book ->
            BookDetailsActivity.start(this, book)
        }
    }

}
