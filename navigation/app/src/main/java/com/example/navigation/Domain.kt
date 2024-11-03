package com.example.navigation

data class UserData(
    var name: String,
    var surname: String,
    var birthDate: String,
) {
    companion object {
        const val NAME = "NAME"
        const val SURNAME = "SURNAME"
        const val BIRTH_DATE = "BIRTH_DATE"
    }
}