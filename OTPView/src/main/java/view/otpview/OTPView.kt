package view.otpview

import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import view.otpview.R
import view.otpview.utils.OTPTextWatcher


class OTPView : LinearLayout {
    private var _boxStyle: Int? = null
    private var _boxSpacing: Int? = null
    private var _boxCount: Int? = null
    private var _boxSize: Int? = null

    var boxStyle: Int?
        get() = _boxStyle
        set(value) {
            _boxStyle = value
        }

    var boxSpacing: Int?
        get() = _boxSpacing
        set(value) {
            _boxSpacing = value
        }
    var boxCount: Int?
        get() = _boxCount
        set(value) {
            _boxCount = value
        }
    var boxSize: Int?
        get() = _boxSize
        set(value) {
            _boxSize = value
        }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    val etList = mutableListOf<EditText>()
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.OTPView, defStyle, 0
        )
        //Get Values
        _boxStyle = a.getResourceId(R.styleable.OTPView_boxStyle, 0)
        _boxCount = a.getInteger(R.styleable.OTPView_boxCount, 4)
        _boxSpacing = a.getInteger(R.styleable.OTPView_boxSpacing, 16)
        _boxSize = a.getInteger(R.styleable.OTPView_boxSize, 100)

        //Adjust theme style
        setBackgroundColor(Color.TRANSPARENT)
        val finalStyle = if (boxStyle != 0) boxStyle ?: 0 else R.style.otp_editText_default_style

        //Box Creation
        repeat(boxCount ?: 4) {
            val boxEt = EditText(context, null, 0, finalStyle)
            boxEt.apply {
                layoutParams = LayoutParams(
                    boxSize ?: 100,
                    boxSize ?: 100
                )
                val lp: LayoutParams = layoutParams as LayoutParams
                lp.setMargins(0, 0, boxSpacing ?: 16, 0)
                layoutParams = lp
                maxLines = 1
                addTextChangedListener(OTPTextWatcher(boxEt) { value ->
                    if (value != "-1" && value != "-2") {
                        etList[it + 1].apply {
                            if (value.isNotEmpty()) {
                                setText(value)
                                requestFocus()
                                setSelection(1)
                            }
                        }
                        checkForSubmition()
                    } else if (value == "-2") {
                        checkForSubmition()
                    } else {
                        if (it != 0) {
                            etList[it - 1].apply {
                                if (this.text.isNotEmpty()) {
                                    requestFocus()
                                    setSelection(1)
                                }
                            }
                            ACTIVE_SUBMITION = false
                        }
                    }
                    if (value.length > 1 && value != "-1" && value != "-2") {
                        etList[value.length].apply {
                            requestFocus()
                            setSelection(1)
                        }
                        if (value.length == boxCount)
                            checkForSubmition()
                    }
                })
                if (it >= boxCount?.minus(1) ?: 0)
                    filters = arrayOf(InputFilter.LengthFilter(1))
            }
            addView(boxEt)
            etList.add(boxEt)
        }

        //Recycle
        a.recycle()
    }

    private val TAG = "OTPView"
    private var ACTIVE_SUBMITION = false
    private fun checkForSubmition() {
        var otp = ""
        etList.forEach { otp += it.text }.toString()
        if (otp.length == boxCount && !ACTIVE_SUBMITION) {
            ACTIVE_SUBMITION = true
            //invoke listener
            onOtpSubmitted(otp)
        }
    }

    fun getOtpValue(): String {
        var otp = ""
        etList.forEach { otp += it.text }.toString()
        return otp
    }

    private var onOtpSubmitted: (String) -> Unit = {}

    fun setOnOtpSubmitListener(onOtpSubmitted: (String) -> Unit = {}) {
        this.onOtpSubmitted = onOtpSubmitted
    }
}