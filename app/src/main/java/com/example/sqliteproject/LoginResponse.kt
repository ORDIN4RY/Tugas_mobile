package com.example.sqliteproject

data class LoginResponse(
    val code: Int,
    val message: String,
    val user: User? = null,
    val error_detail: String? = null
)

