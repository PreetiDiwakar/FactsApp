package com.wipro.facts.features

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.wipro.facts.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
    // Splash Activity with the dealy of 1500ms
    override fun onResume() {
        super.onResume()

        val intent = Intent(this, FactsActivity::class.java)
        Handler().postDelayed(Runnable {
            startActivity(Intent(intent))
            finish()
        }, 1500)
    }
}