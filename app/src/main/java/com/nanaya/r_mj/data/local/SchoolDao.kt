package com.nanaya.r_mj.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SchoolDao {
    @Query("SELECT * FROM LocalMahjongSchool")
     suspend fun findAll(): List<LocalMahjongSchool>

     @Query("SELECT * FROM LocalMahjongSchool WHERE area = :area")
    suspend fun findAllByArea(area: String): List<LocalMahjongSchool>

    @Query("SELECT * FROM LocalMahjongSchool WHERE cid=:cid")
    suspend fun findByCid(cid:String):LocalMahjongSchool?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localMahjongSchool: LocalMahjongSchool)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schools:List<LocalMahjongSchool>)

    @Query("Delete FROM LocalMahjongSchool WHERE cid IN (:ids)")
    suspend fun deleteAll(ids:List<String>)

    @Delete
    suspend fun delete(localMahjongSchool: LocalMahjongSchool)

    @Transaction
    suspend fun update(updates:List<LocalMahjongSchool>,deleteCids:List<String>){
        Log.d("SchoolDao","transaction")
        if (updates.isNotEmpty())
            insertAll(updates)
        Log.d("SchoolDao","transaction insert over")
        if(deleteCids.isNotEmpty())
            deleteAll(deleteCids)
        Log.d("SchoolDao","transaction delete over")
    }
    @Query("SELECT * FROM LocalMahjongSchool WHERE name LIKE '%'||:content||'%' or city LIKE '%'||:content||'%'")
    suspend fun search(content: String): List<LocalMahjongSchool>


}