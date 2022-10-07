package de.bornholdtlee.topsnackbar

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class TopSnackbar {

    sealed class Type {
        object Neutral : Type()
        object Negative : Type()
        object Positive : Type()
    }

    data class Options(
        val actionMessage: Int? = null,
        val duration: Int? = 2000,
        val onActionTap: (() -> Unit)? = null,
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
            when (type) {
                Type.Negative -> showNegativeMessage(view, message)
                Type.Neutral -> showNeutralMessage(view, message)
                Type.Positive -> showPositiveMessage(view, message)
            }
        }

        private fun createSnackBar(
            view: View,
            message: String,
            actionMessage: Int?,
            onActionTap: (() -> Unit)?,
            applyStatusBarTopMarginToMessage: Boolean = false
        ): Snackbar {
            val snackbar: Snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

            val layout: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout
            snackbar.view.background = null
            val params = (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP
                if (applyStatusBarTopMarginToMessage && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
                    val systemBarTopInset: Int = ViewCompat.getRootWindowInsets(view)
                        ?.getInsets(WindowInsets.Type.systemBars())
                        ?.top
                        ?: 0
                    updateMargins(top = topMargin + systemBarTopInset)
                }
            }
            ViewCompat.setLayoutDirection(snackbar.view, ViewCompat.LAYOUT_DIRECTION_LOCALE)
            snackbar.view.layoutParams = params
            layout.addView(LayoutInflater.from(view.context).inflate(R.layout.snackbar, null, false))

            val actionText = snackbar.view.findViewById<TextView>(R.id.snackbar_action_bkk)
            actionText.isVisible = actionMessage != null
            actionMessage?.let { actionText.text = view.context.getString(it) }
            actionText.paintFlags = actionText.paintFlags or UNDERLINE_TEXT_FLAG
            actionText.setOnClickListener {
                if (onActionTap != null) onActionTap()
                hideCurrentSnackbar()
            }

            snackbar.view.findViewById<TextView>(R.id.snackbar_message).text = message

            return snackbar
        }

        fun showNeutralMessage(
            view: View,
            message: String,
            actionMessage: Int? = null,
            duration: Int? = 2000,
            onActionTap: (() -> Unit)? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                view = view,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).apply {
                this.duration = duration ?: 0
            }

            val background = snackbar.view.findViewById<View>(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.foreground))

            snackbar.show()
        }

        fun showPositiveMessage(
            view: View,
            message: String,
            actionMessage: Int? = null,
            onActionTap: (() -> Unit)? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                view = view,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).apply {
                duration = 2000
            }
            val background = snackbar.view.findViewById<View>(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.actionGreen))

            snackbar.show()
        }

        fun showNegativeMessage(
            view: View,
            message: String,
            actionMessage: Int? = android.R.string.ok,
            onActionTap: (() -> Unit)? = null,
            duration: Int? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                view = view,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).also { snackbar ->
                if (actionMessage == null) snackbar.duration = 2000
                if (duration != null) snackbar.duration = duration
            }

            val background: View = snackbar.view.findViewById(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.actionRed))

            currentSnackbar = snackbar
            snackbar.show()
        }

        fun hideCurrentSnackbar() {
            currentSnackbar?.dismiss()
            currentSnackbar = null
        }
    }
}
