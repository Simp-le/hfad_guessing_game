package com.hfad.guessinggame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hfad.guessinggame.R
import com.hfad.guessinggame.databinding.FragmentGameBinding
import com.hfad.guessinggame.ui.GuessingGameTheme
import com.hfad.guessinggame.viewmodels.GameViewModel

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                GuessingGameTheme {
                    Surface {
                        GameFragmentContent(viewModel)
                    }
                }
            }
        }
        val view = binding.root

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel
        observeLiveData(view)

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

@Composable
fun GameFragmentContent(viewModel: GameViewModel) {
    var guess by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        IncorrectGuessesText(viewModel = viewModel, modifier = Modifier.align(Alignment.Start))
        EnterGuess(guess = guess, modifier = Modifier.align(Alignment.Start)) {
            guess = it
        }
        GuessButton {
            viewModel.makeGuess(guess.uppercase())
            guess = ""
        }
    }
}

@Composable
fun IncorrectGuessesText(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val incorrectGuesses = viewModel.incorrectGuesses.observeAsState()

    incorrectGuesses.value?.let {
        Text(stringResource(id = R.string.incorrect_guesses, it), fontSize = 16.sp, modifier = modifier)
    }
}


@Composable
fun EnterGuess(guess: String, modifier: Modifier = Modifier, changed: (String) -> Unit) {
    TextField(
        value = guess,
        placeholder = { Text("Guess a letter", fontSize = 26.sp) },
        onValueChange = changed,
        textStyle = TextStyle(fontSize = 26.sp),
        singleLine = true,
        modifier = modifier
            .width(200.dp)
            .padding(vertical = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun GuessButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text("Guess!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}