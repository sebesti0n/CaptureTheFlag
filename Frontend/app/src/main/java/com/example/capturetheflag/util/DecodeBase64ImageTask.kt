package com.example.capturetheflag.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import android.util.Log


class DecodeBase64ImageTask(
    private val base64String: String,
    private val callback: (Bitmap?) -> Unit
) : AsyncTask<Void, Void, Bitmap?>() {

    override fun doInBackground(vararg params: Void?): Bitmap? {
        try {
            val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("SEBASTION", e.toString())
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        callback(result)
    }
}
