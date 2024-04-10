package com.example.ecosortapp.requests.model

data class RequestData(
    val id: String = "",
    val uid: String = "",
    val client: String? = "",
    val requestMail: String? = "",
    val weight: String? = "",
    val tvTime: String? = "",
    val tvSelectDate: String? = "",
    val imageUrl: String? = "",
    val collectorId : String = ""
)
