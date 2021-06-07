package com.example.recipeapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_list.view.*

class FoodAdapter(
    val context: Context,
    options: FirestoreRecyclerOptions<FoodClass>, val clickData: ItemClickListener

) :
    FirestoreRecyclerAdapter<FoodClass, FoodAdapter.FoodViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.food_list, parent, false)
        return FoodAdapter.FoodViewHolder(adapterLayout, clickData)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int, model: FoodClass) {
        holder.bind(model)
        Glide.with(context)
            .load(model.image)
            .placeholder(R.drawable.food1)
            .error(R.drawable.food2)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context, "Error loading image", Toast.LENGTH_LONG).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(holder.mImage)


    }

    class FoodViewHolder(private val item: View, val clickData: ItemClickListener) :
        RecyclerView.ViewHolder(item) {
        var mImage = item.findViewById<ImageView>(R.id.imagePic)
        fun bind(model: FoodClass) {

            item.nameFood.text = model.foodTitle
            item.setOnClickListener {
                var position = adapterPosition
                clickData.onItemClick(position)


            }

        }
    }

    interface ItemClickListener {
        fun onItemClick(foodItem: Int)
    }
}








