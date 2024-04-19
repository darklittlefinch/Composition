package com.elliemoritz.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elliemoritz.composition.R
import com.elliemoritz.composition.databinding.FragmentGameBinding
import com.elliemoritz.composition.domain.entities.Difficulty
import com.elliemoritz.composition.domain.entities.GameResult
import com.elliemoritz.composition.domain.entities.GameSettings

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var difficulty: Difficulty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
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

        // TODO: replace GameSettings and GameResult values
        binding.tvQuestionMark.setOnClickListener {
            val gameSettings = GameSettings(10, 10, 10, 10)
            val gameResult = GameResult(false, 3, 7, gameSettings)
            launchGameFinishedFragment(gameResult)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        difficulty = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(KEY_DIFFICULTY, Difficulty::class.java)
                ?: throw RuntimeException("Object with key $KEY_DIFFICULTY not found")
        } else {
            requireArguments().getSerializable(KEY_DIFFICULTY) as Difficulty
        }
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
                    putSerializable(KEY_DIFFICULTY, difficulty)
                }
            }
    }
}