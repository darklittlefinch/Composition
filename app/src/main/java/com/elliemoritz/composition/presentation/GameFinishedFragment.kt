package com.elliemoritz.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.elliemoritz.composition.R
import com.elliemoritz.composition.databinding.FragmentGameFinishedBinding
import com.elliemoritz.composition.domain.entities.GameResult

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setResultTextAndPic()
        setupAllStats()

        binding.buttonTryAgain.setOnClickListener { retryGame() }
    }

    private fun setResultTextAndPic() {
        if (args.gameResult.playerIsWinner) {
            binding.tvResultLabel.text = getString(R.string.results_label_win)
            Glide
                .with(requireContext())
                .load(R.drawable.ic_like)
                .into(binding.ivResultPic)
        } else {
            binding.tvResultLabel.text = getString(R.string.results_label_lose)
            Glide
                .with(requireContext())
                .load(R.drawable.ic_dislike)
                .into(binding.ivResultPic)
        }
    }

    private fun setupAllStats() {
        val scoreText = getString(
            R.string.results_score,
            "${args.gameResult.correctAnswersCount} " +
                    "/ ${args.gameResult.totalQuestionsCount}"
        )
        binding.tvAnswersCount.text = scoreText

        val percentage = if (args.gameResult.totalQuestionsCount > 0) {
            ((args.gameResult.correctAnswersCount / args.gameResult.totalQuestionsCount.toDouble())
                    * 100).toInt()
        } else {
            0
        }

        val percentageText = getString(
            R.string.results_percentage,
            "$percentage%"
        )
        binding.tvAnswersPercentage.text = percentageText

        val requiredAnswersText = getString(
            R.string.results_required,
            args.gameResult.gameSettings.minCorrectAnswersCount.toString()
        )
        binding.tvRequiredAnswersCount.text = requiredAnswersText

        val requiredPercentageText = getString(
            R.string.results_required,
            "${args.gameResult.gameSettings.minCorrectAnswersPercentage}%"
        )
        binding.tvRequiredAnswersPercentage.text = requiredPercentageText
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    companion object {
        const val FRAGMENT_NAME = "GameFinishedFragment"
        const val KEY_GAME_RESULT = "gameResult"

        @JvmStatic
        fun newInstance(gameResult: GameResult) =
            GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
    }
}
