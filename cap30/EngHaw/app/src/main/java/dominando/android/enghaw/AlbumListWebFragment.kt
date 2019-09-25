package dominando.android.enghaw

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dominando.android.enghaw.model.Album
import dominando.android.enghaw.model.AlbumHttp
import kotlinx.android.synthetic.main.album_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumListWebFragment : AlbumListBaseFragment() {

    private var albums: List<Album>? = null
    private var downloadJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swpRefresh.setOnRefreshListener {
            loadAlbumsAsync()
        }
        rvAlbums.run {
            tag = "web" // sera utilizado no capitulo de testes
            setHasFixedSize(true)
            val orientation = resources.configuration.orientation
            layoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(activity)
            } else {
                GridLayoutManager(activity, 2)
            }
            addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (albums == null) {
            if (downloadJob?.isActive == true) {
                showProgress(true)
            } else {
                loadAlbumsAsync()
            }
        } else {
            updateList()
        }
    }

    private fun loadAlbumsAsync() {
        downloadJob = launch {
            showProgress(true)
            albums = withContext(Dispatchers.IO) {
                AlbumHttp.loadAlbums()?.toList()
            }
            showProgress(false)
            updateList()
            downloadJob = null
        }
    }

    private fun onItemClick(v: View, album: Album, position: Int) {
        context?.run {
            DetailsActivity.start(this, album)
        }
    }

    private fun updateList() {
        val list = albums ?: emptyList()
        rvAlbums.adapter = AlbumAdapter(list, this::onItemClick)
    }

    private fun showProgress(show: Boolean) {
        swpRefresh.post { swpRefresh.isRefreshing = show }
    }
}