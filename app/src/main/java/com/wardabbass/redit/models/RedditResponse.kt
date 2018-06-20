package com.wardabbass.redit.models

import com.google.gson.annotations.SerializedName

 class RedditResponse(
        @SerializedName("kind") val kind: String = "",
        @SerializedName("data") val data: Data = Data()

) {
     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (javaClass != other?.javaClass) return false

         other as RedditResponse

         if (data != other.data) return false

         return true
     }

     override fun hashCode(): Int {
         return data.hashCode()
     }
 }