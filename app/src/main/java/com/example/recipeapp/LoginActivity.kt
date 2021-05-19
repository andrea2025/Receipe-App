package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {
   private lateinit var auth: FirebaseAuth
    private lateinit var mEmail:EditText
    private lateinit var mPassword:EditText
    private lateinit var mBtn:Button
    private lateinit var db: FirebaseFirestore
    var name : String = ""
    var userID :String =""
    lateinit var userClass : UserInfo
    private lateinit var collectionReference: CollectionReference
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("users")
        userClass= UserInfo(name,userID)
        auth = FirebaseAuth.getInstance()
       mEmail=binding.loginEmail
        mPassword=binding.LoginPassword
        var mRegister = binding.goToRegister
//        mEmail= findViewById(R.id.loginEmail)
//        mPassword = findViewById(R.id.LoginPassword)
//        var mRegister = findViewById<TextView>(R.id.goToRegister)
        mRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)

    }

    fun btnLogin(view: View) {
        val stringEmail = mEmail.text.toString().trim()
        val stringPassword = mPassword.text.toString().trim()
        name = userClass.name.toString()
        userID = userClass.uID.toString()
        Log.i("name",userID)
        Log.i("name",name)

        if (stringEmail.isEmpty()) {
            Toast.makeText(applicationContext, "Input your email", Toast.LENGTH_SHORT).show()
        } else if (stringPassword.isEmpty()) {
            Toast.makeText(applicationContext, "Input your Password", Toast.LENGTH_SHORT).show()
        } else {
            if (firebaseUser != null) {
                auth.signInWithEmailAndPassword(stringEmail, stringPassword)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.w("error", "loginUserWithEmail:failure", task.exception)
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    })

            }else{
                    Log.w("error", "loginUserWithEmail:failure")
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
            }

        }
    }

}
