package com.abhat.core.SortType

import com.google.gson.annotations.SerializedName

enum class SortType {
    @SerializedName("")
    empty,
    @SerializedName("best")
    best,
    @SerializedName("hot")
    hot,
    @SerializedName("new")
    new,
    @SerializedName("rising")
    rising,
    @SerializedName("saved")
    saved
}
