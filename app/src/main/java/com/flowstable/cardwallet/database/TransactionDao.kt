package com.flowstable.cardwallet.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowstable.cardwallet.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY createdAtEpochSeconds DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTransactions(txs: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clear()
}

