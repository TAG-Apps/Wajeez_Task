package  com.wajeez.sample.model.network_utils.remote

import android.content.Context
import com.wajeez.sample.model.network_utils.remote.NetworkResponse
import com.wajeez.sample.model.network_utils.remote.NetworkResponseCall
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>,
    private val context: Context
) : CallAdapter<S, Call<NetworkResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S, E>> {
        return NetworkResponseCall(call, errorBodyConverter , context)
    }
}