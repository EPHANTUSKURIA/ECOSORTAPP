package com.example.ecosortapp.auth.model

data class UserData(
    val uid: String,
    val name: String,
    val email: String,
    val phone: String,
)
{
    // Primary constructor
    constructor() : this("", "", "", "")
}