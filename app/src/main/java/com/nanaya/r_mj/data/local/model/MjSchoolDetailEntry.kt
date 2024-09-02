package com.nanaya.r_mj.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class MjSchoolDetailEntry(
    @Embedded
    val detail:MjSchoolDetail,

    @Relation(
        entity = MjSchoolImg::class,
        parentColumn = "id",
        entityColumn = "mj_school_id",
    )
    val imgs:List<MjSchoolImg>
)