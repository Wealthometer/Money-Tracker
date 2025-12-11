package com.example.moneytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moneytracker.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val firebaseAuth =  FirebaseAuth.getInstance()

        binding.registerhere.setOnClickListener {
            intent = Intent(this,Register::class.java)
            startActivity(intent)
        }


        binding.login.setOnClickListener {
            val password = binding.password.text.toString()
            val email = binding.email.text.toString()

            if (!email.isEmpty() || !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this, "Login Succesfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Something Went Wrong ", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show()
            }

        }



    }
}