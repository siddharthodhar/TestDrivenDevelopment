package com.example.tdd.model.data.remote.entity

import com.example.tdd.model.data.local.entity.DataEntity

fun DataResponse.toDataEntity(): DataEntity {
    return DataEntity(
        userId, id, title, body
    )
}