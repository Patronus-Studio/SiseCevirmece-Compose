package com.patronusstudio.sisecevirmece.data.repository.local

import android.app.Application
import android.content.Context
import com.patronusstudio.sisecevirmece.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageLocalRepository @Inject constructor(private val application: Application) {

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

    suspend fun getPackageOnCloudPackageCategoryId(
        cloudPackageCategoryId: Int
    ): PackageDbModel {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao()
                .getPackageWithCloudPackageCategoryId(cloudPackageCategoryId)
        }
    }

    suspend fun removePackage(packageId: Int) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao()
                .removePackage(packageId)
        }
    }

    suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao().removePackages()
        }
    }

    suspend fun addPackages(model: PackageDbModel): Long {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao()
                .addPackage(model)
        }
    }

    suspend fun updatePackage(model: PackageDbModel) {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao()
                .updatePackage(model)
        }
    }

    suspend fun getPackageByName(packageName: String): PackageDbModel? {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getBottleDao()
                .getPackageByName(packageName)
        }
    }
}