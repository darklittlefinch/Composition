package com.elliemoritz.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.elliemoritz.composition.R
import com.elliemoritz.composition.databinding.FragmentGameFinishedBinding
import com.elliemoritz.composition.domain.entities.GameResult

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        gameResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)
                ?: throw RuntimeException("Object with key $KEY_GAME_RESULT not found")
        } else {
            requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT) as GameResult
        }
    }

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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true
            ) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        })

        binding.buttonTryAgain.setOnClickListener { retryGame() }
    }

    private fun setResultTextAndPic() {
        if (gameResult.playerIsWinner) {
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
            "${gameResult.correctAnswersCount} / ${gameResult.totalQuestionsCount}"
        )
        binding.tvAnswersCount.text = scoreText

        val percentage = if (gameResult.totalQuestionsCount > 0) {
            (gameResult.correctAnswersCount / gameResult.totalQuestionsCount) * 100
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
            gameResult.gameSettings.minCorrectAnswersCount.toString()
        )
        binding.tvRequiredAnswersCount.text = requiredAnswersText

        val requiredPercentageText = getString(
            R.string.results_required,
            "${gameResult.gameSettings.minCorrectAnswersPercentage}%"
        )
        binding.tvRequiredAnswersPercentage.text = requiredPercentageText
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.FRAGMENT_NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        const val FRAGMENT_NAME = "GameFinishedFragment"

        private const val KEY_GAME_RESULT = "gameResult"

        @JvmStatic
        fun newInstance(gameResult: GameResult) =
            GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
    }
}
