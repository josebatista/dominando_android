package dominando.android.enghaw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.squareup.picasso.Picasso
import dominando.android.enghaw.model.Album
import dominando.android.enghaw.model.AlbumHttp
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val album = intent.getParcelableExtra<Album>(EXTRA_ALBUM)

        if (album != null) {
            loadCover(album)
            initTitleBar(album.title)
            fillFields(album)
        }
    }

    private fun loadCover(album: Album) {
        Picasso.get().load(AlbumHttp.BASE_URL + album.coveBig).into(imgCover)
    }

    private fun initTitleBar(title: String) {
        setSupportActionBar(toolbar)

        if (appBar != null) {
            if (appBar.layoutParams is CoordinatorLayout.LayoutParams) {
                val lp = appBar.layoutParams as CoordinatorLayout.LayoutParams
                lp.height = resources.displayMetrics.widthPixels
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (collapseToolbar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            collapseToolbar.title = title
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun fillFields(album: Album) {
        txtTitle.text = album.title
        txtYear.text = album.year.toString()
        txtRecordingCompany.text = album.recordingCompany

        var sb = StringBuilder()
        for (member in album.formation) {
            if (sb.isNotEmpty()) {
                sb.append('\n')
            }
            sb.append(member)
        }

        txtFormation.text = sb.toString()

        sb = StringBuilder()
        album.tracks.forEachIndexed { index, track ->
            if (sb.isNotEmpty()) {
                sb.append('\n')
            }
            sb.append(index + 1).append(". ").append(track)
        }
        txtSongs.text = sb.toString()
    }

    companion object {
        const val EXTRA_ALBUM = "album"
        fun start(context: Context, album: Album) {
            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_ALBUM, album)
            })
        }
    }
}
