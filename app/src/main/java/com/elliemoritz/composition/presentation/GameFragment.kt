package com.elliemoritz.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elliemoritz.composition.R
import com.elliemoritz.composition.databinding.FragmentGameBinding
import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameResult
import com.elliemoritz.composition.domain.entities.GameSettings
import com.elliemoritz.composition.domain.entities.Question

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var difficulty: Difficulty
    private lateinit var gameSettings: GameSettings
    private lateinit var currentCorrectAnswer: String
    private var totalAnswersCount = 0
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        difficulty = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_DIFFICULTY, Difficulty::class.java)
                ?: throw RuntimeException("Object with key $KEY_DIFFICULTY not found")
        } else {
            requireArguments().getParcelable<Difficulty>(KEY_DIFFICULTY) as Difficulty
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setOnAnswerClickListeners()

        viewModel.getGameSettings(difficulty)
    }

    private fun observeViewModel() {
        viewModel.gameSettings.observe(viewLifecycleOwner) {
            gameSettings = it
            viewModel.generateQuestion(it.maxSumValue)
            viewModel.setupTimer(gameSettings.gameTimeInSeconds)
        }

        viewModel.question.observe(viewLifecycleOwner) {
            setupQuestion(it)
        }

        viewModel.correctAnswer.observe(viewLifecycleOwner) {
            currentCorrectAnswer = it.toString()
        }

        viewModel.correctAnswersCounter.observe(viewLifecycleOwner) {
            correctAnswersCount = it

            val statsText = getString(
                R.string.game_stats,
                it.toString(),
                gameSettings.minCorrectAnswersCount.toString()
            )

            binding.tvStats.text = statsText
        }

        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.shouldFinishGame.observe(viewLifecycleOwner) {
            if (it) {
                val gameResult = getGameResult()
                launchGameFinishedFragment(gameResult)
            }
        }
    }

    private fun setupQuestion(question: Question) {
        with(binding) {
            tvSum.text = question.sum.toString()
            tvVisibleNumber.text = question.visibleNumber.toString()

            val answerTextViews = listOf(
                tvAnswer1,
                tvAnswer2,
                tvAnswer3,
                tvAnswer4,
                tvAnswer5,
                tvAnswer6
            )

            for (i in answerTextViews.indices) {
                answerTextViews[i].text = question.options[i].toString()
            }
        }
    }

    private fun getGameResult(): GameResult {
        val minCorrectAnswersCount = gameSettings.minCorrectAnswersCount
        val playerGotEnoughCorrectAnswers =
            correctAnswersCount >= minCorrectAnswersCount

        val percentage = if (totalAnswersCount == 0) {
            0
        } else {
            ((correctAnswersCount / totalAnswersCount.toDouble()) * 100).toInt()
        }
        val minCorrectAnswersPercentage = gameSettings.minCorrectAnswersPercentage
        val playerGotEnoughPercentage = percentage >= minCorrectAnswersPercentage

        val playerIsWinner = playerGotEnoughCorrectAnswers && playerGotEnoughPercentage

        val gameResult = GameResult(
            playerIsWinner,
            correctAnswersCount,
            totalAnswersCount,
            gameSettings
        )
        return gameResult
    }

    private fun setOnAnswerClickListeners() {
        val answerTextViews = listOf(
            binding.tvAnswer1,
            binding.tvAnswer2,
            binding.tvAnswer3,
            binding.tvAnswer4,
            binding.tvAnswer5,
            binding.tvAnswer6
        )

        for (answerTextView in answerTextViews) {
            answerTextView.setOnClickListener {
                totalAnswersCount++
                if (answerTextView.text.toString() == currentCorrectAnswer) {
                    viewModel.increaseCorrectAnswersCounter()
                }

                viewModel.generateQuestion(gameSettings.maxSumValue)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(GameFinishedFragment.FRAGMENT_NAME)
            .commit()
    }

    companion object {

        const val FRAGMENT_NAME = "GameFragment"

        private const val KEY_DIFFICULTY = "difficulty"

        @JvmStatic
        fun newInstance(difficulty: Difficulty) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DIFFICULTY, difficulty)
                }
            }
    }
}
