package com.nanaya.r_mj.data.local

import com.nanaya.r_mj.data.remote.MahjongSchoolDetail
import com.nanaya.r_mj.data.remote.MahjongSchoolItem
import kotlinx.coroutines.flow.Flow

class LocalMjSchoolRepo(private val db:DB) {
      suspend fun getAllSchools(area:String?): List<LocalMahjongSchool> {
        return db.getSchools(area)
    }

     suspend fun getSchoolDetail(cid: String): LocalMahjongSchool?{
        return db.getSchoolByCid(cid)
    }

    suspend fun searchSchoolByNameOrCity(content:String):List<LocalMahjongSchool>{
        return db.search(content)
    }

    suspend fun update(updateList:List<LocalMahjongSchool>,deleteCids:List<String>){
        db.update(updateList,deleteCids)
    }

    suspend fun update(local:LocalMahjongSchool){
        db.update(local)
    }
}