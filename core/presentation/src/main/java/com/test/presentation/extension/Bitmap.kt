package com.test.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class BitmapHelper {

    companion object {
        fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        imageUri
                    )
                )
            } else {
                context
                    .contentResolver
                    .openInputStream(imageUri)
                    ?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
            }
        }

        fun convertMbToBinaryBytes(mb: Long): Long {
            return (mb * 2.0F.pow(20)).toLong()
        }
    }
}

fun Bitmap.convertBitmapToFile(dstFile: File) {
    //create a file to write bitmap data
    dstFile.createNewFile()
    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, bos)
    val bitmapData = bos.toByteArray()
    //write the bytes in file
    FileOutputStream(dstFile).use {
        it.write(bitmapData)
        it.flush()
    }
}

fun Bitmap.scaleToSize(maxBytes: Long): Bitmap {
    val currentPixels = width * height
    val maxPixels = maxBytes / config.bytePerPixel
    return if (currentPixels <= maxPixels) this
    else {
        // Scaling factor when maintaining aspect ratio is the square root since x and y have a relation
        val downScaleFactor = sqrt(maxPixels / currentPixels.toDouble())
        val newWidth = floor(width * downScaleFactor).toInt()
        val newHeight = floor(height * downScaleFactor).toInt()
        Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    }
}

val Bitmap.Config.bytePerPixel: Int
    get() {
        return if (Build.VERSION.SDK_INT >= 26) {
            when (this) {
                Bitmap.Config.ALPHA_8 -> 1
                Bitmap.Config.RGB_565 -> 2
                Bitmap.Config.ARGB_4444 -> 2
                Bitmap.Config.ARGB_8888 -> 4
                Bitmap.Config.RGBA_F16 -> 8
                Bitmap.Config.HARDWARE -> 8
            }
        } else {
            when {
                this == Bitmap.Config.ALPHA_8 -> 1
                this == Bitmap.Config.ALPHA_8 -> 1
                this == Bitmap.Config.RGB_565 || this == Bitmap.Config.ARGB_4444 -> 2
                this == Bitmap.Config.ARGB_8888 -> 4
                else -> 8
            }
        }
    }