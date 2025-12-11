package com.example.moneytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moneytracker.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseAuth = FirebaseAuth.getInstance()


        binding.register.setOnClickListener {

            val email = binding.email.text.toString()
            val password = binding.passwrod.text.toString()
            val conpass = binding.confirmpass.text.toString()

            if (!email.isEmpty() || !password.isEmpty() || !conpass.isEmpty()){
                if (password == conpass){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this,Login::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Registered Succesfully ", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "Something Went Wrong ", Toast.LENGTH_SHORT).show()
                        }
                        }

                }else{
                    Toast.makeText(this, "Password Doesn't Match ", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show()
            }

        }


    }
}