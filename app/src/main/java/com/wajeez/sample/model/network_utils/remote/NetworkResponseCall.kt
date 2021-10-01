package  com.wajeez.sample.model.network_utils.remote

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.wajeez.sample.R
import com.wajeez.sample.model.response.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>,
    private val context: Context
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()


                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )

                    } else {
                        // Response is successful but the body is null
                        showToastMessage(
                            context.getString(R.string.ops_error)
                        )
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                } else {

                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            Gson().fromJson(error.string(), BaseResponse::class.java)
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                            null
                        }
                    }
                    if (errorBody != null) {

                        showToastMessage(
                            errorBody.message ?: context.getString(R.string.ops_error)
                        )

                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.ApiError(errorBody, code))
                        )
                    } else {
                        showToastMessage(
                            context.getString(R.string.ops_error)
                        )
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                throwable.printStackTrace()
                val networkResponse = when (throwable) {
                    is IOException -> {
                        showToastMessage(context.getString(R.string.ops_error))
                        NetworkResponse.NetworkError(throwable)
                    }
                    is TimeoutException -> {
                        showToastMessage(context.getString(R.string.timeout_error))
                        NetworkResponse.TimeoutError(throwable)
                    }
                    else -> {
                        showToastMessage(context.getString(R.string.ops_error))
                        NetworkResponse.UnknownError(throwable)
                    }
                }

                throwable.printStackTrace()
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter, context)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("com.bsa.traxsApp.model.network_utils.NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()


    private fun showToastMessage(message: String) {
        CoroutineScope(Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

    }


}