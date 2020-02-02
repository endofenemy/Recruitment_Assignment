package com.githubrepo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "REPOSITORY")
data class Repository(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author:String?,
    val name:String?,
    val avatar:String?,
    val description:String?,
    val language:String?,
    val languageColor:String?,
    val stars:Int?,
    val forks:Int?,
    val currentPeriodStars:Int?
)