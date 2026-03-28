package com.kabir.realcallingapp.model

data class CallLogModel(
    val id: String,
    val name: String,
    val number: String,
    val type: Int,
    val date: Long,
    val duration: String
)