package app.sakai.tororoimo.tororoimo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultNumber: Int = intent.getIntExtra("ResultNumber", 0)
        resultView.text = resultNumber.toString()


    }
}
