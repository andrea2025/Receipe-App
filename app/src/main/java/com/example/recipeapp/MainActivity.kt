package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class MainActivity : AppCompatActivity(), FoodAdapter.ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val food: ArrayList<FoodClass> = ArrayList()
    lateinit var text:TextView
    private lateinit var db: FirebaseFirestore
    private var userId:String  =""
    private lateinit var reference: DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        val user= FirebaseAuth.getInstance().currentUser
        recyclerView = findViewById(R.id.foodRecycler)
        text = findViewById<TextView>(R.id.mName)

        if (user != null) {
            userId = user.uid
            Log.e("null",userId)
        }

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = FoodAdapter(this, food, this)

        reference = db.collection("users").document(user?.uid.toString())
            .collection("Receipe").document()

            reference.get().addOnCompleteListener{
            task->
            if (task.isSuccessful) {
                val document = task.result?.data
                if (document != null){
                    Log.d("result", "DocumentSnapshot data: " + task.result?.data)
                }else{
                    Log.d("result", "DocumentSnapshot data: " + task.result?.data)
                }
        }
        }
        getDataOneTime()
    }


    private fun getDataOneTime() {

        //getting the data onetime
        val docRef = db.collection("users").document(userId)
        docRef.addSnapshotListener{value: DocumentSnapshot?,
                                   _error: FirebaseFirestoreException? ->
            val user = value?.toObject(UserInfo::class.java)
            if (user != null) {
                text.setText(user.name)
                Log.e("detail", "user data is changed" + user.name)
            }
            //Log.i("error", error?.localizedMessage)
        }

    }


    override fun onItemClick(foodItem: FoodClass) {
        val intent = Intent(this, FoodDetailActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun CreateRecipe(view: View) {
        val intent = Intent(this, CreateReceipeActivity::class.java)
        startActivity(intent)
        finish()
    }


}
