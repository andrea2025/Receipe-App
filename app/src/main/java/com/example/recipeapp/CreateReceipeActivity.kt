package com.example.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class CreateReceipeActivity : AppCompatActivity() {

    private lateinit var mImage: ImageView
    private lateinit var mFoodTitle: EditText
    private lateinit var mFoodDesc: EditText
    private lateinit var foodClass: FoodClass
    var titleString: String = ""
    var decscriptionString: String = ""
    var Image: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DocumentReference
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_receipe)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        mFoodDesc = findViewById(R.id.foodDescription)
        mFoodTitle = findViewById(R.id.foodName)
        mImage = findViewById(R.id.foodImage)
    }

    fun creatReceipe(title: String, description: String, image: String) {
        foodClass = FoodClass(title, description, image)

       reference = db.collection("users").
        document(auth.currentUser?.uid.toString()).
        collection("Receipe").document()
        reference.set(foodClass).addOnSuccessListener(OnSuccessListener { task ->
            Log.e("detail", foodClass.toString())

        }).addOnFailureListener(OnFailureListener {
            exception: Exception ->
            Log.e("detail", exception.toString())
        })

    }

    fun saveFood(view: View) {
        titleString = mFoodTitle.text.toString()
        decscriptionString = mFoodDesc.text.toString()

        if (TextUtils.isEmpty(titleString))
            Toast.makeText(applicationContext, "Please add receipe title.", Toast.LENGTH_SHORT)
                .show()
        else if (TextUtils.isEmpty(decscriptionString))
            Toast.makeText(applicationContext, "Please add a description.", Toast.LENGTH_SHORT)
                .show()
        else
            creatReceipe(titleString, decscriptionString, Image)

    }
}
