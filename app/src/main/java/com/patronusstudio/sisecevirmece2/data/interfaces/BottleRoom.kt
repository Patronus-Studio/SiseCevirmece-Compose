package com.patronusstudio.sisecevirmece2.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece2.data.DbTables
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BottleDbModel

@Dao
interface BottleRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBottles(bottles: List<BottleDbModel>)

    @Insert
    suspend fun insertBottle(bottleRoomDb: BottleDbModel): Long

    @Query("Select * from ${DbTables.bottleTable}")
    suspend fun getBottlesList(): List<BottleDbModel>

    @Query("Update ${DbTables.bottleTable} set isActive= :isActive where primaryId= :primaryId")
    suspend fun updateBottleStatu(primaryId:Int,isActive:Boolean):Int

    @Query("Update ${DbTables.bottleTable} set isActive= :isActive")
    suspend fun updateAllBottleStatu(isActive:Boolean)

    @Query("Select * from ${DbTables.bottleTable} where isActive = 1")
    suspend fun getActiveBottle():BottleDbModel
}