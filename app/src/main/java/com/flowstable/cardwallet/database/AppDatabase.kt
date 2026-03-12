package com.flowstable.cardwallet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flowstable.cardwallet.model.CardEntity
import com.flowstable.cardwallet.model.TransactionEntity

@Database(
    entities = [CardEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
}

