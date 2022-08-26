package com.WM.runny.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun TypeConvertorFromBitmap(img:Bitmap):ByteArray{
        val outputStream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 100,outputStream)
        return outputStream.toByteArray()

    }
@TypeConverter
    fun TypeConvertorToBitmap(bytes:ByteArray):Bitmap{
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }
}