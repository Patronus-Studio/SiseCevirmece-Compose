package com.patronusstudio.sisecevirmece.data.model.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patronusstudio.sisecevirmece.data.DbTables

@Entity(tableName = DbTables.questionTable)
data class QuestionDbModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    val localPackageCategoryId: Int,
    val question: String,
    var isShowed: Boolean = false
)