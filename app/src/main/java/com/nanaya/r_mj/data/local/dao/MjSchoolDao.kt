package com.nanaya.r_mj.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nanaya.r_mj.data.local.model.MjSchoolDetail
import com.nanaya.r_mj.data.local.model.MjSchoolDetailEntry
import com.nanaya.r_mj.data.local.model.MjSchoolImg
import kotlinx.coroutines.flow.Flow

@Dao
interface MjSchoolDao:BaseDao<MjSchoolDetail> {
    @Transaction
    @Query("""
        SELECT * FROM mj_school 
        WHERE (
            (:name IS NULL OR name = :name)
            AND (:areaName IS NULL OR areaName=:areaName)
            AND (:province IS NULL OR province=:province)
            AND (:city IS NULL OR city=:areaName)
        )
        LIMIT :offset
        
            """)
    fun getList(offset:Int,name:String?,areaName:String?,province:String?,city:String?):Flow<List<MjSchoolDetailEntry>>

    @Transaction
    @Query("SELECT * FROM mj_school WHERE id=:id")
    fun getDetail(id:Int):Flow<MjSchoolDetailEntry?>

    @Query("SELECT COUNT(*) FROM mj_school")
    suspend fun count():Int


}

@Dao
interface MjSchoolImgDao:BaseDao<MjSchoolImg>