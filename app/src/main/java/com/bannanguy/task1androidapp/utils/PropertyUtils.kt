package com.bannanguy.task1androidapp.utils

import android.util.Log
import java.io.FileReader
import java.util.*

class ConfigPropertiesUtils {
    companion object {

        fun getValue(key: String): String? {
            return "522995af2a0848f486105521242002"
//            return try {
//                val read = FileReader("src/config.properties")
//                val prop = Properties()
//                prop.load(read)
//
//                val value = prop.getProperty(key)
//                read.close()
//                value
//            } catch (e: Exception) {
//                Log.e("ConfigPropertiesUtils", e.message.toString())
//                null
//            }
        }

    }
}