package app.sakai.tororoimo.tororoimo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment


class CustomDialogFragment : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog
        val date : String? = arguments?.getString("date")
        dialog = Dialog(context!!)
        val dw = dialog.window
        dw?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }

        dialog.setContentView(R.layout.dialog_quota)
        if (date != null) {
            dialog.findViewById<TextView>(R.id.dateView).text = date
        }

        dialog.findViewById<ImageButton>(R.id.dialogOkButton).setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }

}