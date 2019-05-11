package dominando.android.http

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_books_list.*

class BooksListFragment : Fragment() {

    private var asyncTask: BooksDownloadTask? = null
    private val bookList = mutableListOf<Book>()
    private var adapter: ArrayAdapter<Book>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
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
        } else {
            if (asyncTask == null) {
                if (BookHttp.hasConnection(requireContext())) {
                    startDownload()
                } else {
                    progressBar.visibility = View.GONE
                    txtMessage.setText(R.string.error_no_connection)
                }
            } else if (asyncTask?.status == AsyncTask.Status.RUNNING) {
                showProgress(true)
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            txtMessage.setText(R.string.message_progress)
        }
        txtMessage.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownload() {
        if (asyncTask?.status != AsyncTask.Status.RUNNING) {
            asyncTask = BooksDownloadTask()
            asyncTask?.execute()
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
        asyncTask = null
    }

    inner class BooksDownloadTask : AsyncTask<Void, Void, List<Book>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg params: Void?): List<Book>? {
            return BookHttp.loadBooksGson()
        }

        override fun onPostExecute(result: List<Book>?) {
            super.onPostExecute(result)
            showProgress(false)
            updateBookLit(result)
        }
    }

}