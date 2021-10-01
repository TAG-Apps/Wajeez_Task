package com.wajeez.sample.model.network_utils

import com.wajeez.sample.model.network_utils.remote.ErrorResponseBody
import com.wajeez.sample.model.network_utils.remote.NetworkResponse
import retrofit2.http.*

typealias GenericResponse<S> = NetworkResponse<S, ErrorResponseBody>

private const val LOGIN = "security/TARS-login/LoginVault"
private const val AVAILABLE_VAULTS = "vault/GetAvailableVaults"
private const val RECENT_ETC = "document/SearchBySystemView"

interface APIServices {


   /* @POST(LOGIN)
    suspend fun login(
        @Body data: LoginData
    ): GenericResponse<LoginModel>
*/


}



