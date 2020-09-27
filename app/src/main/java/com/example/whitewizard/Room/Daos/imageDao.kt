package com.example.whitewizard.Room.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.whitewizard.Room.Models.imageModel

@Dao
interface imageDao{
    @Insert
    fun insertImage(imageModel: imageModel)

    @Query("SELECT * FROM Image_Table ORDER BY id")
    fun getAllPosts() : LiveData<List<imageModel>>
}