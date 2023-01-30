package com.patronusstudio.sisecevirmece.data.repository.local

import android.app.Application
import com.patronusstudio.sisecevirmece.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackgroundLocalRepository @Inject constructor(private val application: Application) {


    suspend fun getBackgrounds(): List<BackgroundDbModel> {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBackgroundRoomDao()
                .getBackgroundsList()
        }
    }

    suspend fun insertBackgrounds(backgrounds: List<BackgroundDbModel>) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBackgroundRoomDao()
                .insertBackgrounds(backgrounds)
        }
    }

    suspend fun insertBackground(backgroundDbModel: BackgroundDbModel): Long {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBackgroundRoomDao()
                .insertBackground(backgroundDbModel)
        }
    }

    suspend fun updateActiveStatu(primaryId:Int,isActive:Boolean){
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBackgroundRoomDao()
                .updateBackgroundStatu(primaryId,isActive)
        }
    }

    suspend fun getActiveBackground():BackgroundDbModel {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBackgroundRoomDao()
                .getActiveBackground()
        }
    }

}