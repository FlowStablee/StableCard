package com.flowstable.cardwallet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochSeconds: Long,
)

data class User(
    val id: String,
    val email: String,
    val fullName: String,
)

enum class CardStatus {
    ACTIVE, FROZEN, BLOCKED
}

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val last4: String,
    val brand: String,
    val holderName: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val status: CardStatus,
    val maskedPanEncrypted: String?,
    val cvvEncrypted: String?,
    val spendingLimit: Long?,
    val onlineEnabled: Boolean,
    val internationalEnabled: Boolean,
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val amountMinor: Long,
    val currency: String,
    val merchantName: String,
    val createdAtEpochSeconds: Long,
    val status: String,
)

data class WalletSummary(
    val balanceMinor: Long,
    val currency: String,
    val recentTransactions: List<TransactionEntity>,
)

data class TransactionFilter(
    val fromEpochSeconds: Long? = null,
    val toEpochSeconds: Long? = null,
    val minAmountMinor: Long? = null,
    val maxAmountMinor: Long? = null,
)

