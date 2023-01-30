package com.patronusstudio.sisecevirmece.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece.data.DbTables
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel

@Dao
interface BackgroundRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackgrounds(bottles: List<BackgroundDbModel>)

    @Insert
    suspend fun insertBackground(bottleRoomDb: BackgroundDbModel): Long

    @Query("Select * from ${DbTables.backgroundTable}")
    suspend fun getBackgroundsList(): List<BackgroundDbModel>

    @Query("Update ${DbTables.backgroundTable} set isActive= :isActive where primaryId= :primaryId")
    suspend fun updateBackgroundStatu(primaryId:Int,isActive:Boolean)

    @Query("Select * from ${DbTables.backgroundTable} where isActive = 1")
    suspend fun getActiveBackground():BackgroundDbModel
}