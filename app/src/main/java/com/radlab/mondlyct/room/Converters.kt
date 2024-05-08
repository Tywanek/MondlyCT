package com.radlab.mondlyct.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.radlab.mondlyct.models.ImageInfo

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromImageInfo(imageInfo: ImageInfo): String {
        return gson.toJson(imageInfo)
    }

    @TypeConverter
    fun toImageInfo(json: String): ImageInfo {
        return gson.fromJson(json, ImageInfo::class.java)
    }

}