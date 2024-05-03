package com.elliemoritz.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elliemoritz.composition.data.GameRepositoryImpl
import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameResult
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.entities.Question
import com.elliemoritz.composition.domain.usecases.GenerateQuestionUseCase
import com.elliemoritz.composition.domain.usecases.GetGameSettingUseCase

class GameViewModel : ViewModel() {
    private val repository = GameRepositoryImpl

    private val getGameSettingUseCase = GetGameSettingUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private lateinit var gameSettings: GameSettings
    private var currentCorrectAnswer = 0
    private var totalAnswersCount = 0
    private var correctAnswersCount = 0

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _correctAnswersCounter = MutableLiveData(0)
    val correctAnswersCounter: LiveData<Int>
        get() = _correctAnswersCounter

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _shouldFinishGame = MutableLiveData(false)
    val shouldFinishGame: LiveData<Boolean>
        get() = _shouldFinishGame

    fun startGame(difficulty: Difficulty) {
        gameSettings = getGameSettingUseCase(difficulty)
        setupTimer(gameSettings.gameTimeInSeconds)
        generateQuestion(gameSettings.maxSumValue)
    }

    private fun setupTimer(initialTimeInSeconds: Int) {
        val countDownTimer = object : CountDownTimer(
            initialTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                _shouldFinishGame.value = true
            }
        }
        countDownTimer.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val timeInSeconds = millisUntilFinished / MILLIS_IN_SECOND

        val minutesLeft = timeInSeconds / SECONDS_IN_MINUTE
        val secondsLeft = timeInSeconds - (minutesLeft * SECONDS_IN_MINUTE)

        return String.format(TIMER_TEMPLATE, minutesLeft, secondsLeft)
    }

    private fun generateQuestion(maxSumValue: Int) {
        val questionValue = generateQuestionUseCase(maxSumValue)

        for (option in questionValue.options) {
            if (option == questionValue.sum - questionValue.visibleNumber) {
                currentCorrectAnswer = option
                break
            }
        }

        _question.value = questionValue
    }

    fun chooseOption(option: String) {
        increaseTotalAnswersCounter()

        if (option.toInt() == currentCorrectAnswer) {
            increaseCorrectAnswersCounter()
        }

        generateQuestion(gameSettings.maxSumValue)
    }

    private fun increaseTotalAnswersCounter() {
        totalAnswersCount++
    }

    private fun increaseCorrectAnswersCounter() {
        correctAnswersCount++
        _correctAnswersCounter.value = correctAnswersCount
    }

    fun getGameResult(): GameResult {
        return GameResult(
            isPlayerWinner(),
            correctAnswersCount,
            totalAnswersCount,
            gameSettings
        )
    }

    private fun isPlayerWinner(): Boolean {
        val minCorrectAnswersCount = gameSettings.minCorrectAnswersCount
        val playerGotEnoughCorrectAnswers = correctAnswersCount >= minCorrectAnswersCount

        val percentage = if (totalAnswersCount == 0) {
            0
        } else {
            ((correctAnswersCount / totalAnswersCount.toDouble()) * 100).toInt()
        }
        val minCorrectAnswersPercentage = gameSettings.minCorrectAnswersPercentage
        val playerGotEnoughPercentage = percentage >= minCorrectAnswersPercentage

        return playerGotEnoughCorrectAnswers && playerGotEnoughPercentage
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val TIMER_TEMPLATE = "%02d:%02d"
    }
}
