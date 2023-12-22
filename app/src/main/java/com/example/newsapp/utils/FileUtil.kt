package com.example.newsapp.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtil {

    fun uriToFile(uri: Uri,context: Context, createFile: File): File? {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            val contentResolver: ContentResolver = context.contentResolver
            inputStream = contentResolver.openInputStream(uri)

            val file = createFile
            outputStream = FileOutputStream(file)

            if (inputStream != null) {
                val buffer = ByteArray(4 * 1024) // 4k buffer
                var read: Int
                while ((inputStream.read(buffer).also { read = it }) != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
                return file
            }
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                // Handle the exception
                e.printStackTrace()
            }
        }
        return null
    }
}