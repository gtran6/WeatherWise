package com.myprojects.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var startDate: Long,
    var endDate: Long,
)
