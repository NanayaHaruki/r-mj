package com.nanaya.r_mj.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nanaya.r_mj.data.local.dao.MjSchoolDao
import com.nanaya.r_mj.data.local.dao.MjSchoolImgDao
import com.nanaya.r_mj.data.local.dao.TransactionRunnerDao
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolImg

@Database(
    entities = [
        MjSchoolDetail::class,
        MjSchoolImg::class
    ],
    version = 1
)
abstract class RmjDatabase : RoomDatabase() {
    abstract fun mjSchoolDao(): MjSchoolDao
    abstract fun mjSchoolImgDao(): MjSchoolImgDao
    abstract fun transactionRunnerDao(): TransactionRunnerDao
}