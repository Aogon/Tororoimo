package app.sakai.tororoimo.tororoimo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_result.*
import java.lang.IllegalStateException

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultNumber: Int = intent.getIntExtra("ResultNumber", 0)
        resultView.text = resultNumber.toString()

        val cumulativeTextNumber: Int = intent.getIntExtra("CumulativeTextNumber", 0)

        if (cumulativeTextNumber >= 30) {
            val dialog = CustomDialogFragment()
            dialog.show(this.supportFragmentManager, "achieve")
        }


    }

}


