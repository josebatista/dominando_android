package dominando.android.youtube

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        youtubePlayerPreview.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player.cueVideo(VIDEO_ID)
                }
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider,
                result: YouTubeInitializationResult
            ) {
                Toast.makeText(this@MainActivity, "Erro ao reproduzir vídeo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * se desejar utilizar outro layout ou esconder alguma coisa deve ser feito manualmente devido estar evitando que a
     * activity seja criada novamente.
     */
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            //aparelho em landscape
//        } else {
//            //aparelho em portrait
//        }
//    }

    companion object {
        private const val API_KEY = "AIzaSyBiKmmmBo0w3Rej4wCtvvskA90MKr9WQI4"
        private const val VIDEO_ID = "GjSK3qA4gVk"
    }
}
