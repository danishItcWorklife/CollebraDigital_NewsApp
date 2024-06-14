package com.collebra.digital.newsapptask.data.local

import androidx.room.TypeConverter
import com.collebra.digital.newsapptask.data.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}