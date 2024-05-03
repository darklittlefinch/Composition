package com.elliemoritz.composition.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Difficulty: Parcelable {
    TEST, EASY, MEDIUM, HARD
}