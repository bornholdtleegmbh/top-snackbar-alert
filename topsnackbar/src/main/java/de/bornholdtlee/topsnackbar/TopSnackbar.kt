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
import com.google.android.material.snackbar.Snackbar

class TopSnackbar {

    companion object {

        private var currentSnackbar: Snackbar? = null

        private fun createSnackBar(
            activity: Activity,
            message: String,
            actionMessage: Int?,
            onActionTap: (() -> Unit)?,
            applyStatusBarTopMarginToMessage: Boolean = false
        ): Snackbar {
            val activityContentView: View = activity.findViewById(android.R.id.content)
            val snackbar: Snackbar = Snackbar.make(activityContentView, "", Snackbar.LENGTH_INDEFINITE)

            val layout: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout
            snackbar.view.background = null
            val params = (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP
                if (applyStatusBarTopMarginToMessage && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
                    val systemBarTopInset: Int = ViewCompat.getRootWindowInsets(activityContentView)
                        ?.getInsets(WindowInsets.Type.systemBars())
                        ?.top
                        ?: 0
                    updateMargins(top = topMargin + systemBarTopInset)
                }
            }
            ViewCompat.setLayoutDirection(snackbar.view, ViewCompat.LAYOUT_DIRECTION_LOCALE)
            snackbar.view.layoutParams = params
            layout.addView(LayoutInflater.from(activity).inflate(R.layout.snackbar, activity.findViewById(android.R.id.content), false))

            val actionText = snackbar.view.findViewById<TextView>(R.id.snackbar_action_bkk)
            actionText.isVisible = actionMessage != null
            actionMessage?.let { actionText.text = activity.getString(it) }
            actionText.paintFlags = actionText.paintFlags or UNDERLINE_TEXT_FLAG
            actionText.setOnClickListener {
                if (onActionTap != null) onActionTap()
                hideCurrentSnackbar()
            }

            snackbar.view.findViewById<TextView>(R.id.snackbar_message).text = message

            return snackbar
        }

        fun showNeutralMessage(
            activity: Activity,
            message: String,
            actionMessage: Int? = null,
            duration: Int? = 2000,
            onActionTap: (() -> Unit)? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                activity = activity,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).apply {
                this.duration = duration ?: 0
            }

            val background = snackbar.view.findViewById<View>(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.foreground))

            snackbar.show()
        }

        fun showPositiveMessage(
            activity: Activity,
            message: String,
            actionMessage: Int? = null,
            onActionTap: (() -> Unit)? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                activity = activity,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).apply {
                duration = 2000
            }
            val background = snackbar.view.findViewById<View>(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.actionGreen))

            snackbar.show()
        }

        fun showNegativeMessage(
            activity: Activity,
            message: String,
            actionMessage: Int? = android.R.string.ok,
            onActionTap: (() -> Unit)? = null,
            duration: Int? = null,
            applyStatusBarTopMarginToMessage: Boolean = false
        ) {
            val snackbar: Snackbar = createSnackBar(
                activity = activity,
                message = message,
                actionMessage = actionMessage,
                onActionTap = onActionTap,
                applyStatusBarTopMarginToMessage = applyStatusBarTopMarginToMessage
            ).also { snackbar ->
                if (actionMessage == null) snackbar.duration = 2000
                if (duration != null) snackbar.duration = duration
            }

            val background: View = snackbar.view.findViewById(R.id.snackbar_background)
            background.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.actionRed))

            currentSnackbar = snackbar
            snackbar.show()
        }

        fun hideCurrentSnackbar() {
            currentSnackbar?.dismiss()
            currentSnackbar = null
        }
    }
}
