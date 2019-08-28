package dominando.android.animacoes

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_transition.*

class TransitionActivity : AppCompatActivity() {

    private var fieldsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
        btnOk.setOnClickListener {
            //            executeAnimation()
            executeAnimationFromXml()
            fieldsVisible = !fieldsVisible
            val visibility = if (fieldsVisible) View.VISIBLE else View.GONE
            txtName.visibility = visibility
            edtName.visibility = visibility
            txtEmail.visibility = visibility
            edtEmail.visibility = visibility
        }
    }

    // region transicao a partir do XML
    private fun executeAnimationFromXml() {
        val transition = if (fieldsVisible) {
            TransitionInflater.from(this).inflateTransition(R.transition.fields_invisible)
        } else {
            TransitionInflater.from(this).inflateTransition(R.transition.fields_visible)
        }
        TransitionManager.beginDelayedTransition(llContainer, transition)
    }
    //endregion

// region transicao a partir de codigos
//    private fun executeAnimation() {
//        val transitionSet = TransitionSet()
//        transitionSet.ordering = TransitionSet.ORDERING_SEQUENTIAL
//        if (fieldsVisible) {
//            executeInvisibleTransition(transitionSet)
//        } else {
//            executeVisibleTransition(transitionSet)
//        }
//        TransitionManager.beginDelayedTransition(llContainer, transitionSet)
//    }

//    private fun executeInvisibleTransition(transitionSet: TransitionSet) {
//        transitionSet.addTransition(Explode())
//        transitionSet.addTransition(ChangeBounds())
//    }
//
//    private fun executeVisibleTransition(transitionSet: TransitionSet) {
//        transitionSet.addTransition(ChangeBounds())
////        transitionSet.addTransition(Slide(Gravity.END))
//        transitionSet.addTransition(TransitionSet().apply {
//            ordering = TransitionSet.ORDERING_TOGETHER
//            addTransition(Slide(Gravity.START).apply {
//                addTarget(txtName)
//                addTarget(edtName)
//            })
//            addTransition(Slide(Gravity.END).apply {
//                addTarget(txtEmail)
//                addTarget(edtEmail)
//            })
//        })
//    }
// endregion
}
