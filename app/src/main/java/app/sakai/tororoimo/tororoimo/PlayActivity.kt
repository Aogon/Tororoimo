package app.sakai.tororoimo.tororoimo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity(), SimpleRecognizerListener.SimpleRecognizerResponseListener {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var speechState = false

    var permissionState : Boolean = false
    var second = 10

    val timer : CountDownTimer = object  :CountDownTimer(10000, 1000) {
        override fun onFinish() {
            second = 0
            secondText.text = second.toString()
            stopListening()
        }

        override  fun onTick(millisUntilFinished: Long) {
            second = second - 1
            secondText.text = second.toString()

        }
    }


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


        startListening()
        timer.start()






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
//        Toast.makeText(this, speechText, Toast.LENGTH_SHORT).show()
        Log.d("speechText", speechText)
        val speechTextArray: CharArray = speechText.toCharArray()
        var textNumber: Int = 0
        for (i in speechTextArray) {
            if(i == 'と' || i == 'た' || i == '取' || i == '谷' || i == '事'|| i == '元' || i == '止' || i == '頼' || i == '戻') {
                textNumber++
                Log.d("content of i", i.toString())
            }
        }

        Toast.makeText(this, textNumber.toString(), Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }
}

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener)
    :RecognitionListener {
    interface SimpleRecognizerResponseListener {
        fun onResultsResponse(speechText: String)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("ReadyForSpeech", params.toString())
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d("RmsChanged", rmsdB.toString())
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d("onBufferReceived", buffer.toString())
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Log.d("PartialResults", partialResults.toString())
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d("Event", eventType.toString())
    }

    override fun onBeginningOfSpeech() {
        Log.d("onBeginningOfSpeech", "onBeginningOfSpeech")
    }

    override fun onEndOfSpeech() {
        Log.d("onEndOfSpeech", "onEndOfSpeech")
    }

    override fun onError(error: Int) {
        Log.d("Error", error.toString())
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
