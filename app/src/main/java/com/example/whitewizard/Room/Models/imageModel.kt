package com.example.whitewizard.Room.Models

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image_Table")
class imageModel(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "bitmap") val bitmap: String){

}