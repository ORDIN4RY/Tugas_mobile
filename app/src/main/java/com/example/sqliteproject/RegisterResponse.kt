package com.example.sqliteproject

data class RegisterResponse(
    val code: Int,
    val message: String,
    val user: User? = null,
    val error_detail: String? = null
)
