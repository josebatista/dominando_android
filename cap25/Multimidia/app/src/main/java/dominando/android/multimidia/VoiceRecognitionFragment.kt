package dominando.android.multimidia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_voice_recognition.*

class VoiceRecognitionFragment : Fragment() {

    private val voiceIntent: Intent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale alguma coisa!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_voice_recognition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVoice.setOnClickListener {
            openVoiceRecognitoinIntent()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (voiceIntent.resolveActivity(requireActivity().packageManager) == null) {
            btnVoice.isEnabled = false
            Toast.makeText(activity, "Aparelho não suporta comando de voz", Toast.LENGTH_SHORT)
                .show()
            activity?.finish()
        }
    }

    private fun openVoiceRecognitoinIntent() {
        startActivityForResult(voiceIntent, MediaUtils.REQUEST_CODE_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MediaUtils.REQUEST_CODE_AUDIO && resultCode == Activity.RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            updateResultsList(matches)
        }
    }

    private fun updateResultsList(results: ArrayList<String>?) {
        if (results != null) {
            lstResults.adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1, results
            )
        }
    }
}