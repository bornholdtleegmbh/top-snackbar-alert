package de.bornholdtlee.topsnackbaralert

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import de.bornholdtlee.topsnackbar.TopSnackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.positiveBtn).setOnClickListener {
            TopSnackbar.show(
                activity = this,
                message = "This is a positive Alert",
                type = TopSnackbar.Type.Positive,
                options = getOptions()
            )
        }
        findViewById<MaterialButton>(R.id.neutralBtn).setOnClickListener {
            TopSnackbar.show(
                activity = this,
                message = "This is a neutral Alert",
                type = TopSnackbar.Type.Neutral,
                options = getOptions()
            )
        }
        findViewById<MaterialButton>(R.id.negativeBtn).setOnClickListener {
            TopSnackbar.show(
                activity = this,
                message = "This is a negative Alert",
                type = TopSnackbar.Type.Negative,
                options = getOptions()
            )
        }
    }

    private fun getOptions(): TopSnackbar.Options {
        val isActionEnabled: Boolean = findViewById<MaterialCheckBox>(R.id.actionClickedCB).isChecked
        val isActionMessageEnabled: Boolean = findViewById<MaterialCheckBox>(R.id.actionMessageCB).isChecked
        val actionMessage: String = findViewById<TextInputEditText>(R.id.actionMessageET).text.toString()
        val duration: Int = findViewById<Slider>(R.id.durationSlider).value.toInt()
        val applyStatusBarPadding: Boolean = findViewById<MaterialCheckBox>(R.id.topMarginPaddingCB).isChecked

        return TopSnackbar.Options(
            onActionClicked = if (isActionEnabled) ::displayToast else null,
            actionMessage = if (isActionMessageEnabled) actionMessage else "",
            duration = duration,
            applyStatusBarTopMarginToMessage = applyStatusBarPadding
        )
    }

    private fun displayToast() {
        Toast.makeText(this, "Action was clicked", Toast.LENGTH_SHORT).show()
    }
}