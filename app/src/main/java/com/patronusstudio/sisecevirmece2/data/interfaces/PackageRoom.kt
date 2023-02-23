package com.patronusstudio.sisecevirmece2.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patronusstudio.sisecevirmece2.data.DbTables
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel

@Dao
interface PackageRoom {

    @Insert
    suspend fun insertPackage(packageModel: PackageDbModel): Long

    @Query("Select * from ${DbTables.packageTable}")
    suspend fun getPackages(): List<PackageDbModel>

    @Query("Select * from ${DbTables.packageTable} where packageName = :packageName")
    suspend fun getPackageByName(packageName: String): PackageDbModel?

    @Query("Delete from ${DbTables.packageTable}")
    suspend fun removePackages()

    @Query("Delete from ${DbTables.packageTable} where primaryId=:id")
    suspend fun removePackage(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPackage(model: PackageDbModel): Long

    @Query("Select * from ${DbTables.packageTable} where cloudPackageCategoryId = :id")
    suspend fun getPackageWithCloudPackageCategoryId(id: Int): PackageDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePackage(model: PackageDbModel)
}