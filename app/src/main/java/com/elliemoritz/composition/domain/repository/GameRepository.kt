package com.elliemoritz.composition.domain.repository

import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.entities.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        optionsCount: Int
    ): Question

    fun getGameSettings(difficulty: Difficulty): GameSettings
}