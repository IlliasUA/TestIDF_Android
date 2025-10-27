package com.example.quizapp

import androidx.annotation.StringRes

data class Question(
    val image: String,
    val additionalImages: List<String>? = null,
    val correct: String,
    val options: List<String>,
    val description: String? = null,  // For backward compatibility
    @StringRes val descriptionResId: Int? = null,  // New field for string resource ID
    val moreInfo: String? = null,
    @StringRes val moreInfoResId: Int? = null,  // Optional: if you want to localize moreInfo too
    val category: String
)