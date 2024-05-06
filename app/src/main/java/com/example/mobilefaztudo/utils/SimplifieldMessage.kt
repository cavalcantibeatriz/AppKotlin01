package com.example.mobilefaztudo.utils

import org.json.JSONException
import org.json.JSONObject

object SimplifieldMessage {
    fun get(stringMessage: String): HashMap<String, String> {
        val messages = HashMap<String, String>()
        val jsonObject = JSONObject(stringMessage)

        try {
            val jsonMessages = jsonObject.getJSONObject(("message"))
            jsonMessages.keys().forEach { messages[it] = jsonMessages.getString(it) }
        } catch (e: JSONException) {
            messages["message"] = jsonObject.getString("message")
        }

        return messages
    }

}