package com.patronusstudio.sisecevirmece2.data.model.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patronusstudio.sisecevirmece2.data.DbTables

@Entity(tableName = DbTables.questionTable)
data class QuestionDbModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int = 0,
    val localPackagePrimaryId: Int,
    val question: String,
    var correctAnswer: String?,
    var punishment: String?,
    var isShowed: Int
)