package com.example.tochkaapp.data.http.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Vladimir Kraev
 */
data class DtoSearchResponse(

    @SerializedName("total_count")
    var total: Int,
    @SerializedName("items")
    var users: List<DtoUser>

)