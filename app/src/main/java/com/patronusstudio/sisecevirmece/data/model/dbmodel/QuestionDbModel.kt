package com.patronusstudio.sisecevirmece.data.model.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "QuestionTable")
data class QuestionDbModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    val localPackageCategoryId: Int,
    val question: String,
    var isShowed: Boolean = false
)