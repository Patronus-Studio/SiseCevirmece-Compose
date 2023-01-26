package com.patronusstudio.sisecevirmece.data.repository.local

import android.app.Application
import com.patronusstudio.sisecevirmece.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BottleLocalRepository @Inject constructor(private val application: Application) {

    private lateinit var tempPackages: List<BottleDbModel>

    suspend fun getBottles(): List<BottleDbModel> {
        return if (this::tempPackages.isInitialized) tempPackages
        else {
            withContext(Dispatchers.IO) {
                tempPackages =
                    BottleRoomDb.getInstance(application.applicationContext).getBottleRoomDao()
                        .getBottlesList()
                tempPackages
            }
        }
    }

    suspend fun insertBottles(bottless:List<BottleDbModel>){
        withContext(Dispatchers.IO){
            BottleRoomDb.getInstance(application.applicationContext).getBottleRoomDao()
                .insertBottles(bottless)
        }
    }

    suspend fun insertBottle(bottle:BottleDbModel):Long{
        return withContext(Dispatchers.IO){
            BottleRoomDb.getInstance(application.applicationContext).getBottleRoomDao()
                .insertBottle(bottle)
        }
    }

}