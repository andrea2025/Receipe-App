package com.example.recipeapp

data class FoodClass(var foodTitle:String?="", var description:String?="", var image: String?="") {
    constructor() : this(null,null,null) {
        this.foodTitle = foodTitle
        this.description= description
       this.image = image

    }
}