package com.abhat.reddit.SortType

import com.google.gson.annotations.SerializedName

enum class SortType {
    @SerializedName("Best")
    Best,
    @SerializedName("Hot")
    Hot,
    @SerializedName("New")
    New,
    @SerializedName("Rising")
    Rising
}
