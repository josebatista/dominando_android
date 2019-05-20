package dominando.android.http

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_books_list.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BooksListFragment : InternetFragment(), CoroutineScope {

    private lateinit var job: Job
    private var downloadJob: Job? = null

    private val bookList = mutableListOf<Book>()
    private var adapter: ArrayAdapter<Book>? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_books_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = BookListAdapter(requireContext(), bookList)
        listView.emptyView = txtMessage
        listView.adapter = adapter

        if (bookList.isNotEmpty()) {
            showProgress(false)
            updateBookLit(bookList)
        } else {
            startDownload()
        }
    }

    override fun startDownload() {
        if (downloadJob == null) {
            if (BookHttp.hasConnection(requireContext())) {
                startDownloadJson()
            } else {
                progressBar.visibility = View.GONE
                txtMessage.setText(R.string.error_no_connection)
            }
        } else if (downloadJob?.isActive == true) {
            showProgress(true)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            txtMessage.setText(R.string.message_progress)
        }
        txtMessage.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownloadJson() {
        downloadJob = launch {
            showProgress(true)
            val booksTask = withContext(Dispatchers.IO) {
                BookHttp.loadBooksGson()
            }
            updateBookLit(booksTask)
            showProgress(false)

        }
    }

    private fun updateBookLit(livros: List<Book>?) {
        if (livros != null) {
            bookList.clear()
            bookList.addAll(livros)
        } else {
            txtMessage.setText(R.string.error_load_books)
        }
        adapter?.notifyDataSetChanged()
        downloadJob = null
    }

}