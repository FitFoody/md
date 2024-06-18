package com.example.fitfoody.costumviews

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.fitfoody.R

class CvPassword : AppCompatEditText, View.OnTouchListener {

    private lateinit var showButton: TextView
    private var isPasswordVisible: Boolean = false

    var isPasswordValid: Boolean = false

    init {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.border)
        transformationMethod = PasswordTransformationMethod.getInstance()

        showButton = TextView(context).apply {
            text = "Show"
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
            textSize = 16f
            setOnClickListener { togglePasswordVisibility() }
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 0, 16, 0)
        }

        setPadding(paddingLeft, paddingTop, paddingRight + showButton.measuredWidth, paddingBottom)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        setOnTouchListener(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        showButton.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, h)
        showButton.x = (w - showButton.measuredWidth).toFloat()
        showButton.y = 0f
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val showButtonWidth = showButton.measuredWidth
            if (event.x >= (width - showButtonWidth)) {
                togglePasswordVisibility()
                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            transformationMethod = PasswordTransformationMethod.getInstance()
            showButton.text = "Show"
        } else {
            transformationMethod = HideReturnsTransformationMethod.getInstance()
            showButton.text = "Hide"
        }
        isPasswordVisible = !isPasswordVisible
        setSelection(text?.length ?: 0)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            validatePassword()
        }
    }

    private fun validatePassword() {
        isPasswordValid = (text?.length ?: 0) >= 8
        error = if (!isPasswordValid) {
            resources.getString(R.string.passwordLess)
        } else {
            null
        }
    }
}
