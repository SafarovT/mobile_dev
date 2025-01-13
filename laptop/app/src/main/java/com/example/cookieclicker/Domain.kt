package com.example.cookieclicker

data class Product(
    val resId: Int,
    val titleId: Int,
    val price: Long,
    val income: Int,
    val count: Int = 0,
    val disabled: Boolean = false,
)