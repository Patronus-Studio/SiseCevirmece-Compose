package com.patronusstudio.sisecevirmece2.data.repository.local

import android.app.Application
import com.patronusstudio.sisecevirmece2.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageLocalRepository @Inject constructor(private val application: Application) {

    suspend fun getPackages(): List<PackageDbModel> {
       return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .getPackages()
        }
    }

    suspend fun getPackageOnCloudPackageCategoryId(
        cloudPackageCategoryId: Int
    ): PackageDbModel {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .getPackageWithCloudPackageCategoryId(cloudPackageCategoryId)
        }
    }

    suspend fun removePackage(packageId: Int) {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .removePackage(packageId)
        }
    }

    suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao().removePackages()
        }
    }

    suspend fun addPackages(model: PackageDbModel): Long {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .addPackage(model)
        }
    }

    suspend fun updatePackage(model: PackageDbModel) {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .updatePackage(model)
        }
    }

    suspend fun getPackageByName(packageName: String): PackageDbModel? {
        return withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(application.applicationContext).getPackageRoomDao()
                .getPackageByName(packageName)
        }
    }
}