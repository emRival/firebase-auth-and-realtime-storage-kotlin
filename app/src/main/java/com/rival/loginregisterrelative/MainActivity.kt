package com.rival.loginregisterrelative

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        btnLoginListener()
        btnRegisterListener()
    }

    private fun btnLoginListener() {
        btn_1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun btnRegisterListener() {
        btn_2.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            startActivity(Intent(this, FragmentActivity::class.java))
            Toast.makeText(this, "Selamat Datang ${FirebaseAuth.getInstance().currentUser?.email}", Toast.LENGTH_SHORT).show()
        }
    }

}