package com.staqoapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.staqoapp.model.User
import com.staqoapp.model.UsersResponse
import com.staqoapp.network.ApiInterface
import com.staqoapp.network.Resource
import com.staqoapp.network.networkResource.NetworkBoundResource
import com.staqoapp.storage.StaqoDatabase
import com.staqoapp.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MainRepository(private val database: StaqoDatabase) {
    val apiInterface        : ApiInterface by inject(ApiInterface::class.java)
    val appExecutors        : AppExecutors by inject(AppExecutors::class.java)

    fun callGetUsersAPI(): LiveData<Resource<UsersResponse>> {
        return object : NetworkBoundResource<UsersResponse, UsersResponse>(appExecutors) {
            override fun createCall() = apiInterface.getUsers()
            override fun onFetchFailed() {
                Log.e(MainRepository::class.java.simpleName, "onFetchFailed callGetUsersAPI")
            }
        }.asLiveData()
    }

    suspend fun getUserFromDB(): List<User> {
        return database.mainDao.getUsersFromDB()
    }

    fun insertUsersToDB(users: List<User>) {
        CoroutineScope(IO).launch {
            database.mainDao.insertUsers(users)
        }
    }
}