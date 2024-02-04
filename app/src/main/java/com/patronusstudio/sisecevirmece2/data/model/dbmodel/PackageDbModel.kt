package com.patronusstudio.sisecevirmece2.data.model.dbmodel

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.patronusstudio.sisecevirmece2.data.DbTables
import com.patronusstudio.sisecevirmece2.data.model.BasePackageModel

@Entity(tableName = DbTables.packageTable)
data class PackageDbModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    val cloudPackageCategoryId: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val packageImage: ByteArray? = null,
    override val version: Int,
    override val packageName: String,
    override val packageComment: String,
    override val createdTime: String,
    override val updatedTime: String,
    @Ignore
    var isSelected: Boolean
) : BasePackageModel() {
    constructor(
        primaryId: Int = 0,
        cloudPackageCategoryId: Int,
        packageImage: ByteArray? = null,
        version: Int,
        packageName: String,
        packageComment: String,
        createdTime: String,
        updatedTime: String
    ) : this(
        primaryId,
        cloudPackageCategoryId,
        packageImage,
        version,
        packageName,
        packageComment,
        createdTime,
        updatedTime,
        false
    )

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
        if (isSelected != other.isSelected) return false

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
        result = 31 * result + isSelected.hashCode()
        return result
    }
}