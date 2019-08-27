package dominando.android.animacoes

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import kotlinx.android.synthetic.main.activity_property_animations.*

class PropertyAnimationsActivity : AppCompatActivity() {

    private val listInterpolators = arrayOf(
        AccelerateDecelerateInterpolator(),
        AccelerateInterpolator(1.0f), // <- fator opcional
        AnticipateInterpolator(2.0f), // -< fator opcional
        AnticipateOvershootInterpolator(2.0f, 1.5f), // <- tensao, tensao extra
                                                                        // tension opcional
        BounceInterpolator(),
        CycleInterpolator(2f), // <- ciclos
        DecelerateInterpolator(1.0f), // <- fator opcional
        FastOutLinearInInterpolator(),
        FastOutSlowInInterpolator(),
        LinearOutSlowInInterpolator(),
        LinearInterpolator(),
        OvershootInterpolator(2.0f) // <- tensao opcional
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animations)
        imgBazinga.setOnClickListener { executeAnimator() }
    }

    private fun executeAnimator() {
        val animationIndex = spnAnimators.selectedItemPosition
        val interpolator = listInterpolators[spnInterpolators.selectedItemPosition]
        executeViewPropertyAnimation(animationIndex, interpolator)
    }

    private fun executeViewPropertyAnimation(index: Int, interpolator: Interpolator) {
        when (index) {
            0 -> imgBazinga.animate()
                .alpha(0f)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator)
                .withEndAction {
                    imgBazinga.animate()
                        .alpha(1f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
            1 -> imgBazinga.animate()
                .rotation(360f)
                .setDuration(ANIMATION_DURATION)
                .withEndAction {
                    imgBazinga.animate()
                        .rotation(0f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
            2 -> imgBazinga.animate()
                .scaleX(2f)
                .scaleY(2f)
                .setDuration(ANIMATION_DURATION)
                .withEndAction {
                    imgBazinga.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
            3 -> imgBazinga.animate()
                .translationX(500f)
                .setDuration(ANIMATION_DURATION)
                .withEndAction {
                    imgBazinga.animate()
                        .translationX(0f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
            4 -> imgBazinga.animate()
                .alpha(0f)
                .rotation(360f)
                .scaleX(2f)
                .scaleY(2f)
                .translationX(500f)
                .setDuration(ANIMATION_DURATION)
                .withEndAction {
                    imgBazinga.animate()
                        .alpha(1f)
                        .rotation(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationX(0f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 1000L
    }
}
