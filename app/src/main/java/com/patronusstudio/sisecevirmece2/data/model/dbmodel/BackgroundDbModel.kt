package com.patronusstudio.sisecevirmece2.data.model.dbmodel

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patronusstudio.sisecevirmece2.data.DbTables

@Entity(tableName = DbTables.backgroundTable)
data class BackgroundDbModel(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var packageImage: ByteArray? = null,
    val backgroundName: String,
    val isActive: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgroundDbModel

        if (primaryId != other.primaryId) return false
        if (packageImage != null) {
            if (other.packageImage == null) return false
            if (!packageImage.contentEquals(other.packageImage)) return false
        } else if (other.packageImage != null) return false
        if (backgroundName != other.backgroundName) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = primaryId
        result = 31 * result + (packageImage?.contentHashCode() ?: 0)
        result = 31 * result + backgroundName.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }
}