package de.bornholdtlee.topsnackbaralert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import de.bornholdtlee.topsnackbar.TopSnackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.positiveBtn).setOnClickListener {
            TopSnackbar.showPositiveMessage(activity = this, message = "This is a positive Alert")
        }
        findViewById<MaterialButton>(R.id.neutralBtn).setOnClickListener {
            TopSnackbar.showNeutralMessage(activity = this, message = "This is a neutral Alert")
        }
        findViewById<MaterialButton>(R.id.negativeBtn).setOnClickListener {
            TopSnackbar.showNegativeMessage(activity = this, message = "This is a negative Alert")
        }
    }
}