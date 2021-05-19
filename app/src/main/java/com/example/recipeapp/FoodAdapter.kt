package com.example.recipeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_list.view.*

class FoodAdapter(val context:Context,val foodList:ArrayList<FoodClass>,val clickData: ItemClickListener) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.FoodViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.food_list,parent,false)
        return FoodAdapter.FoodViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
       return foodList.size
    }

    override fun onBindViewHolder(holder: FoodAdapter.FoodViewHolder, position: Int) {
       val foodItem = foodList[position]

        holder.itemView.nameFood.text = foodItem.foodTitle
        Picasso.get().load(foodItem.image).into(holder.imageLogo)
        holder.itemView.setOnClickListener{
            clickData.onItemClick(foodItem)
        }
    }

    class FoodViewHolder(private val item: View):RecyclerView.ViewHolder(item){
        val imageLogo = item.findViewById<ImageView>(R.id.imagePic)

    }

    interface ItemClickListener {
        fun onItemClick(foodItem: FoodClass)
    }


}
