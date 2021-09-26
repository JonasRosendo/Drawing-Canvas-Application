package com.jonasrosendo.drawingcanvas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BitmapStorageWriter(private val context: Context, private val bitmap: Bitmap) {

    companion object {
        private val MIME_TYPE = "image/jpg"
        private val RELATIVE_PATH = "Pictures/DrawingApp"
    }

    fun saveBitmapOnExternalStorage() {
        try {
            var fos: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH)
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
                    put(MediaStore.Images.Media.WIDTH, "${bitmap.width}")
                    put(MediaStore.Images.Media.HEIGHT, "${bitmap.height}")
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                if (uri != null) {
                    fos = context.contentResolver.openOutputStream(uri)
                }

            } else {
                val fileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + "/DrawingApp"
                val imageDir = File(fileDir)

                if (imageDir.exists().not()) {
                    imageDir.mkdir()
                }

                val imageFile = File(imageDir, "${System.currentTimeMillis()}.jpg")
                fos = FileOutputStream(imageFile)
            }


            val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

            fos?.flush()
            fos?.close()

            if (success) {
                Toast.makeText(
                    context,
                    "Arquivo criado com sucesso.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Não foi possível criar o arquivo.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
