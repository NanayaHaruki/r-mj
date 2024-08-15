package com.nanaya.r_mj.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalMahjongSchool::class], version = 1)
abstract class RmjDB : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
}

class DB(appContext: Context) {
    private val db = Room.databaseBuilder(appContext, RmjDB::class.java, name = "rmj")
        .build()
    private val schoolDao = db.schoolDao()

    suspend fun update(updateList: List<LocalMahjongSchool>, deleteCids: List<String>) =
        schoolDao.update(updateList, deleteCids)

    suspend fun getSchoolByCid(cid: String) = schoolDao.findByCid(cid)
    suspend fun update(local:LocalMahjongSchool)=schoolDao.insert(local)

    suspend fun getSchools(area: String?): List<LocalMahjongSchool> {
        return if (area == null) schoolDao.findAll()
        else schoolDao.findAllByArea(area)
    }

    suspend fun search(content: String): List<LocalMahjongSchool> {
        return schoolDao.search(content)
    }
}