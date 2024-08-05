package com.hfad.guessinggame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hfad.guessinggame.R
import com.hfad.guessinggame.ui.GuessingGameTheme
import com.hfad.guessinggame.viewmodels.ResultViewModel
import com.hfad.guessinggame.viewmodels.ResultViewModelFactory

class ResultFragment : Fragment() {
    private lateinit var viewModelFactory: ResultViewModelFactory
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val result = ResultFragmentArgs.fromBundle(requireArguments()).result
        viewModelFactory = ResultViewModelFactory(result)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ResultViewModel::class.java)

        val view = ComposeView(requireContext()).apply {
            setContent {
                GuessingGameTheme {
                    Surface {
                        view?.let { ResultFragmentContent(it, viewModel) }
                    }
                }
            }
        }

        return view
    }
}

@Composable
fun ResultFragmentContent(view: View, viewModel: ResultViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ResultText(result = viewModel.result)
        NewGameButton {
            view.findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
        }
    }
}

@Composable
fun ResultText(result: String) {
    Text(text = result, fontSize = 32.sp, textAlign = TextAlign.Center, lineHeight = 1.15.em)
}

@Composable
fun NewGameButton(clicked: () -> Unit) {
    Button(onClick = clicked, modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = "Start New Game", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
    }
}