package com.flowstable.cardwallet.repository

import com.flowstable.cardwallet.database.CardDao
import com.flowstable.cardwallet.database.TransactionDao
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.model.TransactionEntity
import com.flowstable.cardwallet.model.WalletSummary
import com.flowstable.cardwallet.network.ApiService
import com.flowstable.cardwallet.network.CardControlsRequest
import com.flowstable.cardwallet.network.CreateCardRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(
    private val apiService: ApiService,
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
) {

    val cards: Flow<List<CardEntity>> = cardDao.observeCards()
    val transactions: Flow<List<TransactionEntity>> = transactionDao.observeTransactions()

    suspend fun refreshDashboard(): Result<WalletSummary> {
        return try {
            val summary = apiService.getWalletSummary()
            val cardsRemote = apiService.getCards()
            val txsRemote = apiService.getTransactions()
            cardDao.upsertCards(cardsRemote)
            transactionDao.upsertTransactions(txsRemote)
            Result.success(summary)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun createVirtualCard(): Result<CardEntity> {
        return try {
            val card = apiService.createVirtualCard(CreateCardRequest())
            cardDao.upsertCard(card)
            Result.success(card)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun getCard(cardId: String): Flow<CardEntity?> = cardDao.observeCard(cardId)

    suspend fun updateControls(
        cardId: String,
        limitMinor: Long?,
        onlineEnabled: Boolean?,
        internationalEnabled: Boolean?,
    ): Result<CardEntity> {
        return try {
            val updated = apiService.updateCardControls(
                cardId,
                CardControlsRequest(
                    status = null,
                    spendingLimit = limitMinor,
                    onlineEnabled = onlineEnabled,
                    internationalEnabled = internationalEnabled,
                )
            )
            cardDao.upsertCard(updated)
            Result.success(updated)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}

