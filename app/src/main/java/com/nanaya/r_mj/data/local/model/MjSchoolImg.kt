package com.nanaya.r_mj.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mj_school_img",
    foreignKeys = [
        ForeignKey(
            entity = MjSchoolDetail::class,
            parentColumns = ["id"],
            childColumns = ["mj_school_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices=[
        Index("mj_school_id")
    ]
)
data class MjSchoolImg(
    @PrimaryKey val id: Int = 0, // 916
    val img: String = "", // /img/gsz/191/487264c2137b4dabb7a4e7e39c613698.png
    val type: Int = 0, // 3,
    @ColumnInfo(name = "mj_school_id")
    var schoolId: Int
){
    companion object{
        const val LICENSE = 1
        const val PICTURE=2
        const val AVATAR = 3

    }
}
