package com.example.schoolie.models

data class User(
    var username: String = "",
    var email: String = "",
    val user_image: String?,
    val user_type: String?,
    val registrationTokens: MutableList<String>
) {
    constructor() : this("", "", null,"", mutableListOf())
}
