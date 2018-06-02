package com.example.simileoluwaaluko.animalpictures.Models

data class AnimalResultModel(
        val totalHits : Int,
        val hits : List<Hit>,
        val total : Int
)