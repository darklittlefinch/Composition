package com.elliemoritz.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elliemoritz.composition.databinding.FragmentChooseDifficultyBinding
import com.elliemoritz.composition.domain.entities.Difficulty

class ChooseDifficultyFragment : Fragment() {

    private var _binding: FragmentChooseDifficultyBinding? = null
    private val binding: FragmentChooseDifficultyBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseDifficultyBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseDifficultyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnDifficultyClickListeners()
    }

    private fun setOnDifficultyClickListeners() {
        with(binding) {
            buttonTest.setOnClickListener { launchGameFragment(Difficulty.TEST) }
            buttonEasy.setOnClickListener { launchGameFragment(Difficulty.EASY) }
            buttonMedium.setOnClickListener { launchGameFragment(Difficulty.MEDIUM) }
            buttonHard.setOnClickListener { launchGameFragment(Difficulty.HARD) }
        }
    }

    private fun launchGameFragment(difficulty: Difficulty) {
        findNavController().navigate(
            ChooseDifficultyFragmentDirections
                .actionChooseDifficultyFragmentToGameFragment(difficulty)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}