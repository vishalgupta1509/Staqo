package com.staqoapp.appScreens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.staqoapp.model.User
import com.staqoapp.model.UsersResponse
import com.staqoapp.network.Resource
import com.staqoapp.network.Status
import com.staqoapp.repositories.MainRepository
import com.staqoapp.utils.ConnectionDetector
import com.staqoapp.utils.Constants.OtherConstants.ERROR
import com.staqoapp.utils.Constants.OtherConstants.LOADING
import com.staqoapp.utils.Constants.OtherConstants.NO_USER_FOUND
import com.staqoapp.utils.Constants.OtherConstants.SUCCESS
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private lateinit var userObserver: Observer<Resource<UsersResponse>>
    private val connectionDetector: ConnectionDetector by inject(ConnectionDetector::class.java)
    lateinit var responseStatus: MutableLiveData<Int>
    var userList: ArrayList<User> = ArrayList()
    var userMutableData: MutableLiveData<ArrayList<User>> = MutableLiveData()

    fun getUsers() {
        viewModelScope.launch {
            responseStatus = MutableLiveData()
            if(connectionDetector.isInternetOn()) {
                val usersResponse = repository.callGetUsersAPI()
                userObserver = Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data != null) {
                                Log.e(MainViewModel::class.java.simpleName, "API RESPONSE ${it.data}")
                                responseStatus.postValue(SUCCESS)
                                if(it.data.usersList.isNotEmpty()) {
                                    userList = ArrayList(it.data.usersList as ArrayList)
                                    userMutableData.value = userList
                                    repository.insertUsersToDB(it.data.usersList)
                                } else {
                                    responseStatus.postValue(NO_USER_FOUND)
                                }
                            }
                        }
                        Status.ERROR -> {
                            Log.e(MainViewModel::class.java.simpleName, "ERROR : ${it.message}")
                            responseStatus.postValue(ERROR)
                        }
                    }
                }
                usersResponse.observeForever(userObserver)
            } else {
                val users = repository.getUserFromDB()
                if(users.isNotEmpty()) {
                    responseStatus.postValue(SUCCESS)
                    userList = ArrayList(users as ArrayList)
                    userMutableData.value = userList
                    Log.e(MainViewModel::class.java.simpleName, "DATABASE $userList")
                } else {
                    responseStatus.postValue(NO_USER_FOUND)
                }
            }
        }
    }
}