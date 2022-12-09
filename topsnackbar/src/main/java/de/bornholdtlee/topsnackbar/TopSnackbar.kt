package de.bornholdtlee.topsnackbar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlin.time.Duration

class TopSnackbar {

    sealed class Type {
        object Neutral : Type()
        object Negative : Type()
        object Positive : Type()
    }

    data class Options(
        val onActionClicked: (() -> Unit)? = null,
        val actionMessage: String? = null,
        val duration: Duration? = null,
        val applyStatusBarTopMarginToMessage: Boolean = false
    )

    companion object {

        private var currentSnackbar: Snackbar? = null

        fun show(
            activity: Activity,
            message: String,
            type: Type,
            options: Options = Options()
        ) {
            this.show(
                view = activity.findViewById(android.R.id.content),
                message = message,
                type = type,
                options = options
            )
        }

        fun show(
            fragment: Fragment,
            message: String,
            type: Type,
            options: Options = Options()
        ) {
            this.show(
                view = fragment.requireView(),
                message = message,
                type = type,
                options = options
            )
        }

        fun show(
            view: View,
            message: String,
            type: Type,
            options: Options = Options()
        ) {

            val snackbar: Snackbar = createSnackBar(
                view = view,
                message = message,
                options = options
            )

            when (type) {
                Type.Negative -> showNegativeMessage(snackbar)
                Type.Neutral -> showNeutralMessage(snackbar)
                Type.Positive -> showPositiveMessage(snackbar)
            }
        }

        fun hideCurrentSnackbar() {
            currentSnackbar?.dismiss()
            currentSnackbar = null
        }

        @SuppressLint("WrongConstant")
        private fun createSnackBar(
            view: View,
            message: String,
            options: Options
        ): Snackbar {
            val topSnackBar: Snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

            if (options.duration != null) topSnackBar.duration = options.duration.inWholeMilliseconds.toInt()

            topSnackBar.view.background = null

            topSnackBar.view.updateLayoutParams<FrameLayout.LayoutParams> {
                gravity = Gravity.TOP
                if (options.applyStatusBarTopMarginToMessage && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
                    val systemBarTopInset: Int = ViewCompat.getRootWindowInsets(view)
                        ?.getInsets(WindowInsets.Type.systemBars())
                        ?.top
                        ?: 0
                    updateMargins(top = topMargin + systemBarTopInset)
                }
                ViewCompat.setLayoutDirection(topSnackBar.view, ViewCompat.LAYOUT_DIRECTION_LOCALE)
            }

            val inflatedView = LayoutInflater.from(view.context).inflate(R.layout.snackbar, topSnackBar.view as ViewGroup, false)
            (topSnackBar.view as Snackbar.SnackbarLayout).addView(inflatedView)

            topSnackBar.view.findViewById<TextView>(R.id.tsbAction).apply {
                text = options.actionMessage
                options.actionMessage?.let { message -> text = message }
                paintFlags = paintFlags or UNDERLINE_TEXT_FLAG
                setOnClickListener {
                    options.onActionClicked?.invoke()
                    hideCurrentSnackbar()
                }
                currentSnackbar = topSnackBar
                isVisible = options.actionMessage?.isNotEmpty() == true
            }

            topSnackBar.view.findViewById<TextView>(R.id.tsbMessage).text = message

            return topSnackBar
        }

        private fun showNeutralMessage(snackbar: Snackbar) {
            val background = snackbar.view.findViewById<View>(R.id.tsbBackground)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(snackbar.view.context, R.color.tsb_grey))

            val message = snackbar.view.findViewById<TextView>(R.id.tsbMessage)
            message.setTextColor(ContextCompat.getColor(snackbar.view.context, R.color.tsb_text_neutral))

            snackbar.show()
        }

        private fun showPositiveMessage(snackbar: Snackbar) {
            val background = snackbar.view.findViewById<View>(R.id.tsbBackground)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(snackbar.view.context, R.color.tsb_green))

            val message = snackbar.view.findViewById<TextView>(R.id.tsbMessage)
            message.setTextColor(ContextCompat.getColor(snackbar.view.context, R.color.tsb_text_positive))

            snackbar.show()
        }

        private fun showNegativeMessage(snackbar: Snackbar) {
            val background: View = snackbar.view.findViewById(R.id.tsbBackground)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(snackbar.view.context, R.color.tsb_red))

            val message = snackbar.view.findViewById<TextView>(R.id.tsbMessage)
            message.setTextColor(ContextCompat.getColor(snackbar.view.context, R.color.tsb_text_negative))

            snackbar.show()
        }
    }
}
