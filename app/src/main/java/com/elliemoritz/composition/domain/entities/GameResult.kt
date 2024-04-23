package com.elliemoritz.composition.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val playerIsWinner: Boolean,
    val correctAnswersCount: Int,
    val totalQuestionsCount: Int,
    val gameSettings: GameSettings
) : Parcelable
