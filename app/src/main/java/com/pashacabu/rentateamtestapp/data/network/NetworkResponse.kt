package com.pashacabu.rentateamtestapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse(
    @SerialName("per_page")
    val perPage: Int? = null,
    @SerialName("total")
    val total: Int? = null,
    @SerialName("data")
    val data: List<DataItem?>? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("support")
    val support: Support? = null
)

@Serializable
data class Support(
    @SerialName("text")
    val text: String? = null,
    @SerialName("url")
    val url: String? = null
)

@Serializable
data class DataItem(
    @SerialName("last_name")
    var lastName: String? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("avatar")
    var avatar: String? = null,
    @SerialName("first_name")
    var firstName: String? = null,
    @SerialName("email")
    var email: String? = null
)

