package com.wajeez.sample.model.di.modules

import android.content.Context
import com.wajeez.sample.model.network_utils.remote.NetworkResponseAdapterFactory
import com.wajeez.sample.model.network_utils.APIServices
import com.wajeez.sample.model.utils.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideNetworkParent(mAppUtils: AppUtils): Interceptor {

        return Interceptor { chain: Interceptor.Chain ->
            val builder = chain.request()
                .newBuilder()
                .addHeader(CONTENT_TYPE, "application/json")
                .addHeader(ACCEPT, "application/json")
            chain.proceed(builder.build())
        }
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(networkInterceptor: Interceptor): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkInterceptor)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    

    @Singleton
    @Provides
    fun provideRetrofitServices(
        okHttpClient: OkHttpClient,
        appUtils: AppUtils,
        @ApplicationContext context: Context
    ): APIServices =
        Retrofit.Builder()
            .baseUrl(appUtils.getPreferencesStringIfNull(APP_URL) ?: BASE_URL)
            .addCallAdapterFactory((NetworkResponseAdapterFactory(context)))
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build().create(APIServices::class.java)


}