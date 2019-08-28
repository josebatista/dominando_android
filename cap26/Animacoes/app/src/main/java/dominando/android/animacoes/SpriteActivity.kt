package dominando.android.animacoes

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sprite.*

class SpriteActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sprite)

        imgSprite.setOnClickListener {
            val spriteAnimation = imgSprite.background as AnimationDrawable
            if (spriteAnimation.isRunning) {
                spriteAnimation.stop()
            } else {
                spriteAnimation.start()
            }


        }

        /* criando o spriteAnimation por codigo e nao por XML
         *
         * spriteAnimation.addFrame(
         *     ResourcesCompat.getDrawable(resources, R.drawable.owl_1, theme), 2000
         * )
         * spriteAnimation.addFrame(
         *     ResourcesCompat.getDrawable(resources, R.drawable.owl_2, theme), 100
         * )
         * spriteAnimation.addFrame(
         *     ResourcesCompat.getDrawable(resources, R.drawable.owl_3, theme), 200
         * )
         * spriteAnimation.addFrame(
         *     ResourcesCompat.getDrawable(resources, R.drawable.owl_1, theme), 100
         * )
         */

    }
}
