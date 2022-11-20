package view.otpview.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class OTPTextWatcher(private val et: EditText, val actionDone: (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //Ignored
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.toString().length > 1) {
            et.setText((p0.toString().first()).toString())
            actionDone(p0.toString().drop(1))
        }else if (p0.toString().length == 1){
            actionDone("-2")
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isEmpty())
            actionDone("-1")
    }
}