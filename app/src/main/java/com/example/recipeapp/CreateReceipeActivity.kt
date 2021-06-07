package com.example.recipeapp

import android.Manifest
import android.Manifest.*
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

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
    private val TAKE_PHOTO = 0
    private val PICK_PHOTO = 1
    var PERMISSION_CAMERA: Int = 1001
    var PERMISSION_CODE_WRITE: Int = 1002
    private lateinit var cameraPerssion: Array<String>
    private lateinit var storagePermission: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_receipe)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        mFoodDesc = findViewById(R.id.foodDescription)
        mFoodTitle = findViewById(R.id.foodName)
        mImage = findViewById(R.id.foodImage)
        mImage.setOnClickListener {
            selectImage()
        }
        permissionClik()
    }

    private fun permissionClik() {
        cameraPerssion =
            arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.CAMERA)
        storagePermission = arrayOf(permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext.applicationContext, permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCamera() {
        ActivityCompat.requestPermissions(this, cameraPerssion, PERMISSION_CAMERA)
    }

    private fun requestStorage() {
        ActivityCompat.requestPermissions(this, storagePermission, PERMISSION_CODE_WRITE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val camera = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storrage = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (camera && storrage) {
                        openCamera()
                        //selectImage()
                    } else {
                        Toast.makeText(this, "Camera granted", Toast.LENGTH_LONG).show()
                    }
                }
            }

            PERMISSION_CODE_WRITE -> {
                val storrage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storrage) {
                    allowStorage()
                    //selectImage()
                } else {
                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openCamera() {
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePhoto, TAKE_PHOTO)
    }

    private fun allowStorage() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.type = "image/*"
        startActivityForResult(pickPhoto, PICK_PHOTO)
    }


    private fun selectImage() {
        val options = arrayOf("Take Photo", "Choose from Library", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setItems(options, DialogInterface.OnClickListener { dialog, items ->
            if (options[items].equals("Take Photo")) {

                if (!checkPermission()) {
                    requestCamera()
                } else {
                    openCamera()
                }
            } else if (options[items].equals("Choose from Library")) {
                if (!checkPermission()) {
                    requestStorage()
                } else {
                    allowStorage()
                }


            } else if (options[items].equals("Cancel")) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO && data != null) {
            //mImage.setImageBitmap(data.extras?.get("data") as Bitmap)
            var photo = data.extras!!["data"] as Bitmap?
            photo = Bitmap.createScaledBitmap(photo!!, 80, 80, false)
          mImage.setImageBitmap(photo)
        } else if (resultCode == Activity.RESULT_OK && requestCode == PICK_PHOTO) {
//            val selectedImage: Bitmap? = data!!.extras!!["data"] as Bitmap?
//            mImage.setImageBitmap(selectedImage)
            mImage.setImageURI(data?.data)
        }
    }

    fun creatReceipe(title: String, description: String, image: String) {
        foodClass = FoodClass(title, description, image)

        reference =
            db.collection("users").document(auth.currentUser?.uid.toString()).collection("Receipe")
                .document()
        reference.set(foodClass).addOnSuccessListener(OnSuccessListener { task ->
            Log.e("detail", foodClass.toString())

        }).addOnFailureListener(OnFailureListener { exception: Exception ->
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
