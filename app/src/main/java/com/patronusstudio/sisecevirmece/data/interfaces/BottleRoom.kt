package com.patronusstudio.sisecevirmece.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece.data.DbTables
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel

@Dao
interface BottleRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBottles(bottles: List<BottleDbModel>)

    @Insert
    suspend fun insertBottle(bottleRoomDb: BottleDbModel): Long

    @Query("Select * from ${DbTables.bottleTable}")
    suspend fun getBottlesList(): List<BottleDbModel>
}