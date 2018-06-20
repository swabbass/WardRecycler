package com.wardabbass.redit.models

import com.google.gson.annotations.SerializedName

 class Data(
        @SerializedName("modhash") val modhash: String?=null,
        @SerializedName("dist") val dist: Int = 0,
        @SerializedName("children") val children: List<Children> = listOf(),
        @SerializedName("after") val after: String ?=null,
        @SerializedName("before") val before: String? =null

) {
     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (javaClass != other?.javaClass) return false

         other as Data

         if (after != other.after) return false

         return true
     }

     override fun hashCode(): Int {
         return after?.hashCode() ?: 0
     }
 }