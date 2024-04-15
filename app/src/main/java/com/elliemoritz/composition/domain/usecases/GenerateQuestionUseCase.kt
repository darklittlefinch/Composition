package com.elliemoritz.composition.domain.usecases

import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(difficulty: Difficulty): GameSettings {
        return repository.getGameSettings(difficulty)
    }
}