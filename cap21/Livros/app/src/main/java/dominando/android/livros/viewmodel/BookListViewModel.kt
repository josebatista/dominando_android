package dominando.android.livros.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dominando.android.livros.firebase.FbRepository
import dominando.android.livros.model.Book

class BookListViewModel : ViewModel() {

    private val repo = FbRepository()
    private var booksList: LiveData<List<Book>>? = null

    fun getBooks(): LiveData<List<Book>> {
        var list = booksList
        if (list == null) {
            list = repo.loadBooks()
            booksList = list
        }
        return list
    }

}