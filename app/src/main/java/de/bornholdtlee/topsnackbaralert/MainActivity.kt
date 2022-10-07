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
            TopSnackbar.show(
                activity = this,
                message = "This is a positive Alert",
                type = TopSnackbar.Type.Positive
            )
        }
        findViewById<MaterialButton>(R.id.neutralBtn).setOnClickListener {
            TopSnackbar.show(
                activity = this,
                message = "This is a neutral Alert",
                type = TopSnackbar.Type.Neutral
            )
        }
        findViewById<MaterialButton>(R.id.negativeBtn).setOnClickListener {
            TopSnackbar.show(
                activity = this,
                message = "This is a negative Alert",
                type = TopSnackbar.Type.Negative
            )
        }


    }
}