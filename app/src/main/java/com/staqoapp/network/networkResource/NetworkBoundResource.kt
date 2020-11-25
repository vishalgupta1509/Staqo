package com.staqoapp.network.networkResource

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.staqoapp.network.*
import com.staqoapp.utils.AppExecutors
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.InvocationTargetException

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val TAG = NetworkBoundResource::class.java.name
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        Log.d(TAG, "fetchFromNetwork12")
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(apiResponse) { response ->
            Log.d(TAG, "fetchFromNetwork111111")
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    Log.d(TAG, "fetchFromNetwork  success")
                    appExecutors.diskIO().execute {
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(processResponse(response)) as Resource<ResultType>)
//                             we specially request a new live data,
//                             otherwise we will get immediately last cached value,
//                             which may not be updated with latest results received from network.
//                            result.addSource(loadFromDb()) { newData ->
//                                setValue(Resource.success(newData))
//                            }

                        }
                    }
                }
                is ApiEmptyResponse -> {
                    Log.d(TAG, "fetchFromNetwork  empty")
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
//                        result.addSource(loadFromDb()) { newData ->
//                            setValue(Resource.success(newData))
//                        }
                    }
                }
                is ApiErrorResponse -> {
                    Log.d(TAG, "fetchFromNetwork  failed")
                    onFetchFailed()
                    Log.d("NetworkBoundResource", "Network Error " + response.errorMessage)
//                    setValue(Resource.error(response.errorMessage, response) as Resource<ResultType>)
                    result.addSource(apiResponse) { newData ->
                        try {
                            val parser = JsonParser()
                            parser.parse(response.errorMessage)
                            val json = JSONObject(response.errorMessage)
                            setValue(
                                Resource.error(
                                    json.getString("message"),
                                    newData
                                ) as Resource<ResultType>
                            )
                        } catch (e: JsonSyntaxException ) {
                            setValue(
                                Resource.error(
                                    response.errorMessage,
                                    newData
                                ) as Resource<ResultType>
                            )
                        } catch (e: InvocationTargetException) {
                            setValue(
                                Resource.error(
                                    response.errorMessage,
                                    newData
                                ) as Resource<ResultType>
                            )
                        } catch (e: RuntimeException) {
                            setValue(
                                Resource.error(
                                    response.errorMessage,
                                    newData
                                ) as Resource<ResultType>
                            )
                        } catch (e: Exception) {
                            setValue(
                                Resource.error(
                                    response.errorMessage,
                                    newData
                                ) as Resource<ResultType>
                            )
                        }
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}

