package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.FoodAdapter.ItemClickListener
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var text: TextView
    private lateinit var db: FirebaseFirestore
    private var userId: String = ""
    private lateinit var reference: CollectionReference
    private lateinit var document: DocumentReference
    private lateinit var auth: FirebaseAuth
    var food: FoodAdapter? = null
    lateinit var query: Query


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.foodRecycler)
        text = findViewById<TextView>(R.id.mName)
        reference = db.collection("users")

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        getFoodReceipe()
        getDataOneTime()
        setUpAdapter()
    }

    private fun getFoodReceipe() {
        query = reference.document(auth.currentUser?.uid.toString()).collection("Receipe")
        Log.i("QUERY", query.get().toString())
        val options = FirestoreRecyclerOptions.Builder<FoodClass>()
            .setLifecycleOwner(this).setQuery(query, FoodClass::class.java)
            .build()

        food = FoodAdapter(this, options, object : ItemClickListener {
            override fun onItemClick(foodItem: Int) {
                var title = options.snapshots.get(foodItem).foodTitle
                var desc = options.snapshots.get(foodItem).description
                Log.i("QUERY", title + desc)
                val fragment = FoodDetailFragment.newInstance()
                val bundle = Bundle()
                bundle.putString("title", title)
                bundle.putString("desc", desc)
                fragment.arguments = bundle
                fragment.show(supportFragmentManager, "dialog")
            }
        })
    }


    fun setUpAdapter() {
        recyclerView.adapter = food
        food?.startListening()
    }


    private fun getDataOneTime() {
        val docRef = db.collection("users").document(auth.currentUser?.uid.toString())
        docRef.addSnapshotListener { value: DocumentSnapshot?,
                                     _error: FirebaseFirestoreException? ->
            val user = value?.toObject(UserInfo::class.java)
            if (user != null) {
                text.setText(user.name)
                Log.e("detail", "user data is changed" + user.name)
            }

        }

    }


    fun CreateRecipe(view: View) {
        val intent = Intent(this, CreateReceipeActivity::class.java)
        startActivity(intent)
        finish()
    }


}
