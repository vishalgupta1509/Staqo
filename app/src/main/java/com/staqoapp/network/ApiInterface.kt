package com.staqoapp.network

import androidx.lifecycle.LiveData
import com.staqoapp.model.UsersResponse
import com.staqoapp.utils.Constants.ApiConstants.GET_USERS
import retrofit2.http.GET

interface ApiInterface {

    @GET(GET_USERS)
    fun getUsers(): LiveData<ApiResponse<UsersResponse>>
}