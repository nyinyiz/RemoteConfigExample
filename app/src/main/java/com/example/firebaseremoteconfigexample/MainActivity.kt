package com.example.firebaseremoteconfigexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import android.graphics.Color.parseColor
import android.net.Uri
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.text.InputFilter
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import coil.api.load
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch singleton FirebaseRemoteConfig object
        var firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        firebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build()
        )

        firebaseRemoteConfig.setDefaults(R.xml.default_map)

        Log.d("THIS IS TEXt","THIS IS : ${firebaseRemoteConfig.getString("text_str")}")

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
                    firebaseRemoteConfig.activateFetched()

                } else {
                    Toast.makeText(this@MainActivity, "Not Activated", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}
