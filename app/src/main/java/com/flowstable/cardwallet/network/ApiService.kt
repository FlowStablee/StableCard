package com.flowstable.cardwallet.network

import com.flowstable.cardwallet.model.AuthTokens
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.model.WalletSummary
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochSeconds: Long,
)

data class RefreshRequest(val refreshToken: String)

data class CreateCardRequest(
    val type: String = "VIRTUAL",
)

data class CardControlsRequest(
    val status: String?,
    val spendingLimit: Long?,
    val onlineEnabled: Boolean?,
    val internationalEnabled: Boolean?,
)

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): LoginResponse

    @GET("wallet/summary")
    suspend fun getWalletSummary(): WalletSummary

    @GET("cards")
    suspend fun getCards(): List<CardEntity>

    @POST("cards")
    suspend fun createVirtualCard(@Body body: CreateCardRequest): CardEntity

    @GET("cards/{cardId}")
    suspend fun getCard(@Path("cardId") cardId: String): CardEntity

    @POST("cards/{cardId}/controls")
    suspend fun updateCardControls(
        @Path("cardId") cardId: String,
        @Body body: CardControlsRequest,
    ): CardEntity

    @GET("cards/{cardId}/pan")
    suspend fun getCardPan(
        @Path("cardId") cardId: String,
        @Header("x-device-auth") deviceAuth: String,
    ): String

    @GET("cards/{cardId}/cvv")
    suspend fun getCardCvv(
        @Path("cardId") cardId: String,
        @Header("x-device-auth") deviceAuth: String,
    ): String

    @GET("transactions")
    suspend fun getTransactions(): List<TransactionEntity>
}

