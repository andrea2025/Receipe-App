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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity(), View.OnClickListener, OnCompleteListener<AuthResult> {
    private lateinit var auth: FirebaseAuth
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var db: FirebaseFirestore
    var stringFirstName: String = ""
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var mButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var mLogin = findViewById<TextView>(R.id.goToLogin)
        mLogin.setOnClickListener(this)
        mEmail = findViewById(R.id.signUpEmail)
        mPassword = findViewById(R.id.signUpPassword)
        mFirstName = findViewById(R.id.firstName)
        mLastName = findViewById(R.id.lastName)
    }


    override fun onClick(v: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

    fun btnSignUp(view: View) {
        val stringEmail = mEmail.text.toString().trim()
        val stringPassword = mPassword.text.toString().trim()
        stringFirstName = mFirstName.text.toString().trim()
        val stringLastName = mLastName.text.toString().trim()

        Log.e("sss", stringEmail + stringPassword)

        if (stringEmail.isEmpty()) {
            Toast.makeText(applicationContext, "Input your email", Toast.LENGTH_SHORT).show()
        } else if (stringPassword.isEmpty()) {
            Toast.makeText(applicationContext, "Input your Password", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(stringEmail, stringPassword)
                .addOnCompleteListener(this)
        }
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            val newUser = firebaseUser?.uid?.let { UserInfo(stringFirstName, it) }
            if (newUser != null) {
                db.collection("users").document(auth.currentUser?.uid.toString()).set(newUser)
                    .addOnSuccessListener { documentRef ->
                        Log.d("jjjj", newUser.name)
                    }
            }
            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.w("error", task.exception)
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
        }

    }

}
