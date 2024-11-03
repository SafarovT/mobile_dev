package com.example.movies

data class Movie(
    val title: String,
    val score: Double,
    val description: String,
    val imageResId: Int,
) {
    companion object {
        const val TITLE = "TITLE"
        const val SCORE = "SCORE"
        const val DESCRIPTION = "DESCRIPTION"
        const val IMAGE_RES_ID = "IMAGE_RES_ID"
    }
}