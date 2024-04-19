package com.elliemoritz.composition.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minRightAnswersCount: Int,
    val minRightAnswersPercentage: Int,
    val gameTimeInSeconds: Int
) : Parcelable
