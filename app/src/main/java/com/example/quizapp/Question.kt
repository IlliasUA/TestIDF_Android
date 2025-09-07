package com.example.quizapp

data class Question(
    val image: String,
    val options: List<String>,
    val correct: String,
    val category: String,
    val description: String? = null,
    val moreInfo: String? = null,
    val additionalImages: List<String>? = null
)