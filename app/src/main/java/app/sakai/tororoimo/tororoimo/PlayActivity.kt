package app.sakai.tororoimo.tororoimo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PlayActivity : AppCompatActivity(), SimpleRecognizerListener.SimpleRecognizerResponseListener {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var speechState = false

    var permissionState : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val recordAudioPermission = android.Manifest.permission.RECORD_AUDIO
        val currentPermissionState = ContextCompat.checkSelfPermission(this, recordAudioPermission)
        if (currentPermissionState != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity, recordAudioPermission)) {
                permissionState = false
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(recordAudioPermission), 1)
                permissionState = true
            }
        }

        setupSpeechRecognizer()

        setupRecognizerIntent()






    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(SimpleRecognizerListener(this))
    }

    private  fun setupRecognizerIntent() {
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)

    }

    private fun startListening() {
        speechState = true
        speechRecognizer.startListening(recognizerIntent)
    }



    private fun stopListening() {
        speechState = false
        speechRecognizer.stopListening()
    }

    override fun onResultsResponse(speechText: String) {
        Toast.makeText(this, speechText, Toast.LENGTH_SHORT).show()
    }
}

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener)
    :RecognitionListener {
    interface SimpleRecognizerResponseListener {
        fun onResultsResponse(speechText: String)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onRmsChanged(rmsdB: Float) {
        TODO("Not yet implemented")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onPartialResults(partialResults: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onBeginningOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onEndOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onError(error: Int) {
        TODO("Not yet implemented")
    }

    override fun onResults(results: Bundle?) {
        if (results == null) {
            listener.onResultsResponse("")
            return
        }

        val key = SpeechRecognizer.RESULTS_RECOGNITION
        val result = results.getStringArrayList(key)
        val speechText = result?.get(0)?.replace("¥¥s".toRegex(), "")
        if (speechText.isNullOrEmpty()) {
            listener.onResultsResponse("")
        } else {
            listener.onResultsResponse(speechText)
        }
    }
}
