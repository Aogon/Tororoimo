package app.sakai.tororoimo.tororoimo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_result.*
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultNumber: Int = intent.getIntExtra("ResultNumber", 0)
        resultView.text = resultNumber.toString()

        val cumulativeTextNumber: Int = intent.getIntExtra("CumulativeTextNumber", 0)

        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))

        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val quotaState: Boolean = dataStore.getBoolean("QuotaState$date", false)

        Log.d("quotaState", quotaState.toString())

        if (cumulativeTextNumber >= 30 && !quotaState) {
            val editor = dataStore.edit()
            editor.putBoolean("QuotaState$date", true)
            editor.apply()
            val dialog = CustomDialogFragment()
            val args = Bundle()
            args.putString("date", date)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "achieve")

        }

        resultReturnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        resultReplayButton.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }


    }

}


