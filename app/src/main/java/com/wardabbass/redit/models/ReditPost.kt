package com.wardabbass.redit.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

const val DEFAULT_THUMB="default"
 class ReditPost(
        @SerializedName("title") val title: String = "",
        @SerializedName("subreddit_name_prefixed") val subredditNamePrefixed: String = "",
        @SerializedName("thumbnail_height") val thumbnailHeight: Int = -1,
        @SerializedName("name") val name: String = "",
        @SerializedName("thumbnail_width") val thumbnailWidth: Int = -1,
        @SerializedName("thumbnail") val thumbnail: String = "",
        @SerializedName("author_flair_text") val authorFlairText: String = "",
        @SerializedName("id") val id: String = "",
        @SerializedName("permalink") val permalink: String = "",
        @SerializedName("created_utc") val createdUtc: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subredditNamePrefixed)
        parcel.writeInt(thumbnailHeight)
        parcel.writeString(name)
        parcel.writeInt(thumbnailWidth)
        parcel.writeString(thumbnail)
        parcel.writeString(authorFlairText)
        parcel.writeString(id)
        parcel.writeString(permalink)
        parcel.writeInt(createdUtc)
    }

    override fun describeContents(): Int {
        return 0
    }

     override fun equals(other: Any?): Boolean {
         if (this === other) return true
         if (javaClass != other?.javaClass) return false

         other as ReditPost

         if (id != other.id) return false

         return true
     }

     override fun hashCode(): Int {
         return id.hashCode()
     }

     companion object CREATOR : Parcelable.Creator<ReditPost> {
        override fun createFromParcel(parcel: Parcel): ReditPost {
            return ReditPost(parcel)
        }

        override fun newArray(size: Int): Array<ReditPost?> {
            return arrayOfNulls(size)
        }
    }

}