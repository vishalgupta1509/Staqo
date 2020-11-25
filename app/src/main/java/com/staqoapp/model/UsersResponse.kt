package com.staqoapp.model

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("data")
    val usersList: List<User>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)