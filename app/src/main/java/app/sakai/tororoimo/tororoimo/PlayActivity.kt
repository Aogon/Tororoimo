package app.sakai.tororoimo.tororoimo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Contacts
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.dialog_end.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlayActivity : AppCompatActivity(), SimpleRecognizerListener.SimpleRecognizerResponseListener {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var speechState = false
    private var isAnimated = true

    var permissionState : Boolean = false
    var second = 10

    val timer : CountDownTimer = object  :CountDownTimer(10000, 1000) {
        override fun onFinish() {
            second = 0
            secondText.text = second.toString()
            tororoimoView.clearAnimation()
            stopListening()
            val dialog = EndDialogFragment()
            dialog.show(supportFragmentManager, "end")
        }

        override  fun onTick(millisUntilFinished: Long) {
            second = second - 1
            secondText.text = second.toString()

        }
    }
    val anim1 = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, -0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.0f,
        Animation.RELATIVE_TO_SELF, 0.0f)


    val anim3 = ScaleAnimation(
        1.0f, 1.0f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f
    )

    val set1: AnimationSet = AnimationSet(true)





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

        anim1.duration = 1000
        anim1.repeatCount = -1
        anim1.repeatMode = Animation.REVERSE
        anim3.duration = 10000
        anim3.repeatCount = -1
        set1.addAnimation(anim1)
        set1.addAnimation(anim3)




        GlobalScope.launch{

            tororoimoView.startAnimationAsync(set1)

        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResultsResponse(speechText: String) {

        val speechTextArray: CharArray = speechText.toCharArray()
        var textNumber: Int = 0
        for (i in speechTextArray) {
            if(i == 'と' || i == 'た' || i == '取' || i == '谷' || i == '事'|| i == '元' || i == '止' || i == '頼' || i == '戻') {
                textNumber++

            }
        }



        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))

        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)

        val pastTextNumber = dataStore.getInt(date, 0)
        val cumulativeTextNumber = pastTextNumber + textNumber
        val editor = dataStore.edit()
        editor.putInt(date, cumulativeTextNumber)
        editor.apply()


        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("Date", date)
        intent.putExtra("ResultNumber", textNumber)
        intent.putExtra("CumulativeTextNumber", cumulativeTextNumber)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    suspend fun View.startAnimationAsync(anim: Animation) {

        return suspendCoroutine { continuation ->
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    continuation.resume(Unit)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            this.startAnimation(anim)

        }
    }
}

class SimpleRecognizerListener(private val listener: SimpleRecognizerResponseListener)
    :RecognitionListener {
    interface SimpleRecognizerResponseListener {
        fun onResultsResponse(speechText: String)
    }

    override fun onReadyForSpeech(params: Bundle?) {

    }

    override fun onRmsChanged(rmsdB: Float) {

    }

    override fun onBufferReceived(buffer: ByteArray?) {

    }

    override fun onPartialResults(partialResults: Bundle?) {

    }

    override fun onEvent(eventType: Int, params: Bundle?) {

    }

    override fun onBeginningOfSpeech() {

    }

    override fun onEndOfSpeech() {

    }

    override fun onError(error: Int) {
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
