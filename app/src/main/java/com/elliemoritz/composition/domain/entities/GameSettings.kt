package com.elliemoritz.composition.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minCorrectAnswersCount: Int,
    val minCorrectAnswersPercentage: Int,
    val gameTimeInSeconds: Int
) : Parcelable
