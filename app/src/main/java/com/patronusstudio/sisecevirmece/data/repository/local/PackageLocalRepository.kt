package com.patronusstudio.sisecevirmece.data.repository.local

import android.content.Context
import com.patronusstudio.sisecevirmece.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageLocalRepository @Inject constructor() {

    private lateinit var tempPackages: List<PackageDbModel>

    suspend fun getPackages(context: Context): List<PackageDbModel> {
        return if (this::tempPackages.isInitialized) tempPackages
        else {
            withContext(Dispatchers.IO) {
                tempPackages = BottleRoomDb.getInstance(context).getBottleDao().getPackages()
                tempPackages
            }
        }
    }
}