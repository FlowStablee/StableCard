package com.flowstable.cardwallet.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowstable.cardwallet.model.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM cards")
    fun observeCards(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun observeCard(id: String): Flow<CardEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCard(card: CardEntity)

    @Query("DELETE FROM cards")
    suspend fun clear()
}

