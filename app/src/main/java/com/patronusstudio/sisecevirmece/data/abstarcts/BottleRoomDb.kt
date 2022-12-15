package com.patronusstudio.sisecevirmece.data.abstarcts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.patronusstudio.sisecevirmece.data.interfaces.BottleDao
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel

@Database(entities = [QuestionDbModel::class, PackageDbModel::class], version = 1)
abstract class BottleRoomDb : RoomDatabase() {

    abstract fun getBottleDao(): BottleDao

    companion object {

        private var INSTANCE: BottleRoomDb? = null

        fun getInstance(context: Context): BottleRoomDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BottleRoomDb::class.java,
                        "bottle_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}