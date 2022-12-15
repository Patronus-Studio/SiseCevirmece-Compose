package com.patronusstudio.sisecevirmece.data.model.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PackageTable")
data class PackageDbModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    val cloudPackageCategoryId: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val packageImage: ByteArray? = null,
    val version: Int,
    val packageName:String,
    val packageComment:String,
    val createdTime: String,
    val updatedTime: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackageDbModel

        if (primaryId != other.primaryId) return false
        if (cloudPackageCategoryId != other.cloudPackageCategoryId) return false
        if (packageImage != null) {
            if (other.packageImage == null) return false
            if (!packageImage.contentEquals(other.packageImage)) return false
        } else if (other.packageImage != null) return false
        if (version != other.version) return false
        if (packageName != other.packageName) return false
        if (packageComment != other.packageComment) return false
        if (createdTime != other.createdTime) return false
        if (updatedTime != other.updatedTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = primaryId
        result = 31 * result + cloudPackageCategoryId
        result = 31 * result + (packageImage?.contentHashCode() ?: 0)
        result = 31 * result + version
        result = 31 * result + packageName.hashCode()
        result = 31 * result + packageComment.hashCode()
        result = 31 * result + createdTime.hashCode()
        result = 31 * result + updatedTime.hashCode()
        return result
    }


}