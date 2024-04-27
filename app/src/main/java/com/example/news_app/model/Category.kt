package com.route.newsc39.model

import com.route.newsc39.R

data class Category (
    val id: String,
    val imageId: Int,
    val backgroundColorId: Int,
    val title: String
){
    companion object{
        val categories = listOf(
            Category(id = "sports", imageId = R.drawable.sports,
                    backgroundColorId = R.color.red, title = "Sports"),
            Category(id = "entertainment", imageId = R.drawable.politics,
                backgroundColorId = R.color.blue, title = "Entertainment"),
            Category(id = "health", imageId = R.drawable.health,
                backgroundColorId = R.color.pink, title = "Health"),
            Category(id = "business", imageId = R.drawable.bussines,
                backgroundColorId = R.color.orange, title = "Business"),
            Category(id = "technology", imageId = R.drawable.environment,
                backgroundColorId = R.color.light_blue, title = "Technology"),
            Category(id = "science", imageId = R.drawable.science,
                backgroundColorId = R.color.yellow, title = "Science"),
        )
    }
}