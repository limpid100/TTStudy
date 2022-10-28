package com.dxl.ttstudy.util

object PinyinUtil {

    fun String?.toPinyinVoiceFileName(): String {
        this ?: return ""
        val tones = listOf(
            mapOf("a" to "āáǎà"),
            mapOf("o" to "ōóǒò"),
            mapOf("e" to "ēéěè"),
            mapOf("i" to "īíǐì"),
            mapOf("u" to "ūúǔù"),
            mapOf("v" to "ǖǘǚǜ")
        )

        val stringList = this.split(" ")
        val sb = StringBuilder()
        stringList.forEach {
            var t = 0  //几声
            var pinyin = ""
            //tōng
            for (c in it) {
                // c = ō
                tones.forEach { map ->
                    map.forEach { (key, value) ->
                        if (value.contains(c)) {
                            t = value.indexOf(c) + 1  //1声
                            pinyin = it.replace(c.toString(), key)
                        }
                    }
                }
            }
            sb.append(pinyin + (if (t > 0) t else "")).append(" ")
        }

        return sb.trim().toString()
    }
}