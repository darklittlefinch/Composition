package com.elliemoritz.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elliemoritz.composition.data.GameRepositoryImpl
import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.entities.Question
import com.elliemoritz.composition.domain.usecases.GenerateQuestionUseCase
import com.elliemoritz.composition.domain.usecases.GetGameSettingUseCase

class GameViewModel : ViewModel() {
    private val repository = GameRepositoryImpl

    private val getGameSettingUseCase = GetGameSettingUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private val _gameSettings = MutableLiveData<GameSettings>()
    val gameSettings: LiveData<GameSettings>
        get() = _gameSettings

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _correctAnswer = MutableLiveData<Int>()
    val correctAnswer: LiveData<Int>
        get() = _correctAnswer

    private val _correctAnswersCounter = MutableLiveData(0)
    val correctAnswersCounter: LiveData<Int>
        get() = _correctAnswersCounter

    private val _secondsLeft = MutableLiveData<Int>()
    val secondsLeft: LiveData<Int>
        get() = _secondsLeft

    private val _shouldFinishGame = MutableLiveData(false)
    val shouldFinishGame: LiveData<Boolean>
        get() = _shouldFinishGame

    fun getGameSettings(difficulty: Difficulty) {
        val gameSettingValue = getGameSettingUseCase(difficulty)
        _gameSettings.value = gameSettingValue
    }

    fun generateQuestion(maxSumValue: Int) {
        val questionValue = generateQuestionUseCase(maxSumValue)

        for (option in questionValue.options) {
            if (option == questionValue.sum - questionValue.visibleNumber) {
                _correctAnswer.value = option
                break
            }
        }

        _question.value = questionValue
    }

    fun increaseCorrectAnswersCounter() {
        val oldValue = _correctAnswersCounter.value ?: 0
        _correctAnswersCounter.value = oldValue + 1
    }

    fun setupTimer(initialTimeInSeconds: Int) {
        val countDownTimer = object : CountDownTimer(
            initialTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                _secondsLeft.value = seconds
            }

            override fun onFinish() {
                _shouldFinishGame.value = true
            }
        }
        countDownTimer.start()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
    }
}
