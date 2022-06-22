package com.rival.loginregisterrelative

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rival.loginregisterrelative.databinding.ActivityLoginBinding

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.btnLogin.setOnClickListener {
            login()
        }

        btnBackLoginListener()
        txtRegisterListener()
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            binding.edtPassword.error = "Password tidak boleh kosong"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Email tidak valid"
        }

        if (password.length < 6){
            binding.edtPassword.error = "Password minimal 6 karakter"
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Selamat datang $email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, FragmentActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun btnBackLoginListener() {
        L_img_1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun txtRegisterListener() {
        txt_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}