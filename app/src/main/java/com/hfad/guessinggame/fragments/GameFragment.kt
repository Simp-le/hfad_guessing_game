package com.hfad.guessinggame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hfad.guessinggame.databinding.FragmentGameBinding
import com.hfad.guessinggame.viewmodels.GameViewModel

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel
        observeLiveData(view)

        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData(view: View) {
        // viewLifecycleOwner refers to the lifecycle of the fragment's views.
        // viewLifecycleOwner is tied to when fragment has access to its UI:
        // from when it's created in the fragment's onCreateView() method, to when its destroyed, and onDestroyView() gets called.

        // Observer is a class that can receive live data. It's tied to the viewLifecycleOwner
        // so it's only active - and able to receive live data notifications - when the fragment has access to its views.
        // This stops the fragment from trying to update views when they're not available, which might cause the app to crash.

        // When data binding is not enabled. The fragment is responsible for updating data in views.
        // viewModel.secretWordDisplay.observe(
        //     viewLifecycleOwner,
        //     Observer { newValue -> binding.word.text = newValue })

        // Lets the layout respond to live data updates
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.gameOver.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    val action =
                        GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.resultMessage())
                    view.findNavController().navigate(action)
                }
            }
        )
    }
}