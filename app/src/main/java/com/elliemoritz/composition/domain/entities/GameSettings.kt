package com.elliemoritz.composition.domain.entities

import java.io.Serializable

data class GameSettings(
    val maxSumValue: Int,
    val minRightAnswersCount: Int,
    val minRightAnswersPercentage: Int,
    val gameTimeInSeconds: Int
) : Serializable