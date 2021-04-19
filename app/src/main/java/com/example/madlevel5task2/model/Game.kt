package com.example.madlevel5task2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Game(
        var title: String,
        var platform: String,
        var releaseDate: Date,
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
)