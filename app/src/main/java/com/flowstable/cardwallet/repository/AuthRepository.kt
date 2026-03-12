package com.flowstable.cardwallet.repository

import com.flowstable.cardwallet.model.AuthTokens
import com.flowstable.cardwallet.network.ApiService
import com.flowstable.cardwallet.network.LoginRequest
import com.flowstable.cardwallet.security.TokenStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenStore: TokenStore,
) {

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val resp = apiService.login(LoginRequest(email, password))
            tokenStore.saveTokens(
                AuthTokens(
                    accessToken = resp.accessToken,
                    refreshToken = resp.refreshToken,
                    expiresAtEpochSeconds = resp.expiresAtEpochSeconds,
                )
            )
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun logout() {
        tokenStore.clear()
    }
}

