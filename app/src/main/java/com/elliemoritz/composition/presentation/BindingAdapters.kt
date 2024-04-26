package com.elliemoritz.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.elliemoritz.composition.R
import com.elliemoritz.composition.domain.entities.GameResult

@BindingAdapter("image")
fun bindImage(imageView: ImageView, playerWins: Boolean) {
    imageView.setImageResource(getImageResId(playerWins))
}

private fun getImageResId(playerWins: Boolean): Int {
    return if (playerWins) {
        R.drawable.ic_like
    } else {
        R.drawable.ic_dislike
    }
}

@BindingAdapter("congrats")
fun bindCongrats(textView: TextView, playerWins: Boolean) {
    textView.text = if (playerWins) {
        textView.context.getString(R.string.results_label_win)
    } else {
        textView.context.getString(R.string.results_label_lose)
    }
}

@BindingAdapter("score")
fun bindScore(textView: TextView, gameResult: GameResult) {
    textView.text = textView.context.getString(
        R.string.results_score,
        gameResult.correctAnswersCount,
        gameResult.totalQuestionsCount
    )
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = textView.context.getString(
        R.string.results_required_answers,
        count
    )
}

@BindingAdapter("percentage")
fun bindPercentage(textView: TextView, gameResult: GameResult) {
    textView.text = textView.context.getString(
        R.string.results_percentage,
        getPercentageFromGameResult(gameResult)
    )
}

private fun getPercentageFromGameResult(gameResult: GameResult): Int {
    val totalQuestionsCount = gameResult.totalQuestionsCount
    val correctAnswersCount = gameResult.correctAnswersCount

    return if (totalQuestionsCount > 0) {
        ((correctAnswersCount / totalQuestionsCount.toDouble()) * 100).toInt()
    } else {
        0
    }
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int) {
    textView.text = textView.context.getString(
        R.string.results_required_percentage,
        percentage
    )
}
