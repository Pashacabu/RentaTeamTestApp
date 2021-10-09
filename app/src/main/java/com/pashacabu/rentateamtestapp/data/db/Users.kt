package com.pashacabu.rentateamtestapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity
class Users(
    @PrimaryKey
    var id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    var email: String? = null
)


