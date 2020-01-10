package com.example.firebaseremoteconfigexample

import android.graphics.Color.parseColor
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch singleton FirebaseRemoteConfig object
        var firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        /*firebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build()
        )*/

        firebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600L)
                .build()
        )

        firebaseRemoteConfig.setDefaults(R.xml.default_map)

        text.setTextColor(parseColor(firebaseRemoteConfig.getString("text_color")))
        text.textSize = firebaseRemoteConfig.getValue("text_size").asDouble().toFloat()
        text.text = firebaseRemoteConfig.getString("text_str")
        rlMain.setBackgroundColor(parseColor(firebaseRemoteConfig.getString("background_color")))

        ivBackground.load(firebaseRemoteConfig.getString("background_image")) {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
        }

        val maxLength = firebaseRemoteConfig.getValue("input_field_max_count").asDouble().toInt()
        edNumber.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

        fetch.setOnClickListener {
            firebaseRemoteConfig.fetch(0).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Activated", Toast.LENGTH_SHORT).show()
                    firebaseRemoteConfig.activate()

                } else {
                    Toast.makeText(this@MainActivity, "Not Activated", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}
