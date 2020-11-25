package com.staqoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    val avatar: String,
    val email: String,
    val first_name: String,
    @PrimaryKey
    val id: Int,
    val last_name: String
)