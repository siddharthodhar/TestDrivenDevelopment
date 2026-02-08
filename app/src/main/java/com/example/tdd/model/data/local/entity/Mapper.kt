package com.example.tdd.model.data.local.entity

import com.example.tdd.viewmodels.domain.entity.Data

fun DataEntity.toDomain(): Data {
    return Data(
        userId, id, title, body
    )
}