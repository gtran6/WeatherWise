package com.myprojects.model

import com.myprojects.data.entity.AlertEntity
import com.myprojects.ui.adapter.AlertAdapter

data class AlertModel(
    var list: ArrayList<AlertEntity>?,
    var listener : AlertAdapter.AlertAdapterListener
    )
