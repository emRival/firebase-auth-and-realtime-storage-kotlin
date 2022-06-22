package com.rival.loginregisterrelative

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rival.loginregisterrelative.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.RBtn1.setOnClickListener {
            register()
        }

        txtLoginListener()
        btnBackListener()
    }

    private fun register() {

        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfPassword.text.toString()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            binding.edtPassword.error = "Password tidak boleh kosong"
            binding.edtConfPassword.error = "Konfirmasi Password tidak boleh kosong"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
           binding.edtEmail.error = "Email tidak valid"
        }

        if (password.length < 6){
            binding.edtPassword.error = "Password minimal 6 karakter"
        }

        if (password != confirmPassword){
            binding.edtConfPassword.error = "Password tidak sama"
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun txtLoginListener() {
        txt_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun btnBackListener() {
        R_img_1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}