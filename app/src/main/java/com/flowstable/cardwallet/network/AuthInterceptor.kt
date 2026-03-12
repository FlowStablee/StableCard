package com.flowstable.cardwallet.network

import com.flowstable.cardwallet.security.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val accessToken = runBlocking { tokenStore.getAccessToken() }
        val requestBuilder = original.newBuilder()
        if (!accessToken.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            response.close()
            val refreshed = runBlocking { tokenStore.tryRefreshTokens() }
            if (refreshed) {
                val newAccessToken = runBlocking { tokenStore.getAccessToken() }
                val retriedRequest = original.newBuilder().apply {
                    if (!newAccessToken.isNullOrBlank()) {
                        header("Authorization", "Bearer $newAccessToken")
                    }
                }.build()
                return chain.proceed(retriedRequest)
            }
        }

        return response
    }
}

