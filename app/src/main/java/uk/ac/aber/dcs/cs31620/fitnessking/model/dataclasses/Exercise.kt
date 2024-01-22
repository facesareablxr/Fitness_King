package uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses

data class Exercise(
    var name: String,
    var sets: Int,
    var reps: Int,
    var weight: Int,
    var isDropSet: Boolean,
    var length: Int,
    var image: String,
    var isFavourite: Boolean,
    var restTime: Int,
    var focus: String,
)