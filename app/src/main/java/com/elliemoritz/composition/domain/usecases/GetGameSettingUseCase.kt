package com.elliemoritz.composition.domain.usecases

import com.elliemoritz.composition.domain.entities.Question
import com.elliemoritz.composition.domain.repository.GameRepository

class GetGameSettingUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, OPTIONS_COUNT)
    }

    private companion object {
        private const val OPTIONS_COUNT = 6
    }
}