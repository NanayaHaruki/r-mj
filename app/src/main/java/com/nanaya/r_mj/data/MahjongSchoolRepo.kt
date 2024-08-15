package com.nanaya.r_mj.data

import com.nanaya.r_mj.data.local.LocalMahjongSchool
import com.nanaya.r_mj.data.local.LocalMjSchoolRepo
import com.nanaya.r_mj.data.remote.RemoteMjSchoolRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext


class MahjongSchoolRepo(private val localMjSchoolRepo: LocalMjSchoolRepo, private val remoteMjSchoolRepo: RemoteMjSchoolRepo) {

    private val schools = MutableStateFlow<List<LocalMahjongSchool>>(emptyList())
    fun observerSchools(): Flow<List<LocalMahjongSchool>> = schools

    suspend fun findSchoolsByArea(area:String?=null){
        schools.emit(localMjSchoolRepo.getAllSchools(area))
    }

    suspend fun findSchoolsByNameOrCity(content:String){
        schools.emit(localMjSchoolRepo.searchSchoolByNameOrCity(content))
    }

    suspend fun fetchSchoolsFromServer() = withContext(Dispatchers.Default) {
        val remoteRes = remoteMjSchoolRepo.getAllSchools()
        if (remoteRes.isFailure) {
            remoteRes.exceptionOrNull()?.printStackTrace()
            return@withContext
        }
        val localCidToEntity = mutableMapOf<String, LocalMahjongSchool>()

        for (local in localMjSchoolRepo.getAllSchools(null)) {
            localCidToEntity[local.cid] = local
        }

        val updateSchools = mutableListOf<LocalMahjongSchool>()
        for (remoteEntity in remoteRes.getOrNull()!!) {
            if (remoteEntity.cid in localCidToEntity) {
                localCidToEntity[remoteEntity.cid]?.run {
                    LocalMahjongSchool.convertFromMahjongSchoolItem(remoteEntity,this)
                    updateSchools.add(this)
                    localCidToEntity.remove(remoteEntity.cid)
                }
            } else {
                updateSchools.add(LocalMahjongSchool.convertFromMahjongSchoolItem(remoteEntity))
            }
        }

        schools.emit(updateSchools)

        localMjSchoolRepo.update(updateSchools,localCidToEntity.keys.toList())
    }

    suspend fun fetchSchoolDetailFromServer(cid: String):LocalMahjongSchool? = withContext(Dispatchers.IO) {
        val remoteRes = remoteMjSchoolRepo.getSchoolDetail(cid)
        if (remoteRes.isFailure) return@withContext null
        val remote = remoteRes.getOrNull()!!
        val local =  localMjSchoolRepo.getSchoolDetail(remote.cid)
        val cvtLocal = LocalMahjongSchool.convertFromMahjongSchoolDetail(remote,local)
        localMjSchoolRepo.update(cvtLocal)
        cvtLocal
    }


}



