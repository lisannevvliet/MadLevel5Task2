package com.example.madlevel5task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel5task2.R
import com.example.madlevel5task2.databinding.FragmentAddGameBinding
import com.example.madlevel5task2.model.Game
import java.text.SimpleDateFormat

// A simple [Fragment] subclass as the second destination in the navigation.
class AddGameFragment : Fragment() {

    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enable a different AppBar for this fragment.
        setHasOptionsMenu(true)

        // Display the different AppBar and show a button to navigate back to the GameFragment.
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.add_game)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_vector_test)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.fabSave.setOnClickListener {
            onAddGame()
        }
    }

    // Navigate back to the GameBacklogFragment upon a click on the AppBar back arrow.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Add the game if all the fields are filled in and the date is valid, and navigate back to the GameBacklogFragment.
    private fun onAddGame() {
        val title = binding.etTitle.text.toString()
        val platform = binding.etPlatform.text.toString()
        val day = binding.etDay.text.toString()
        val month = binding.etMonth.text.toString()
        val year = binding.etYear.text.toString()

        if (title.isBlank()) {
            Toast.makeText(activity, R.string.missing_title, Toast.LENGTH_SHORT).show()
        } else if (platform.isBlank()) {
            Toast.makeText(activity, R.string.missing_platform, Toast.LENGTH_SHORT).show()
        } else if (day.isBlank()) {
            Toast.makeText(activity, R.string.missing_day, Toast.LENGTH_SHORT).show()
        } else if (month.isBlank()) {
            Toast.makeText(activity, R.string.missing_month, Toast.LENGTH_SHORT).show()
        } else if (year.isBlank()) {
            Toast.makeText(activity, R.string.missing_year, Toast.LENGTH_SHORT).show()
        } else if (day.toInt() > 31 || month.toInt() > 12 || year.toInt() < 1900) {
            Toast.makeText(activity, R.string.invalid_date, Toast.LENGTH_SHORT).show()
        } else {
            val releaseDate = SimpleDateFormat("dd-MM-yyyy").parse("$day-$month-$year")
            viewModel.insertGame(Game(title, platform, releaseDate))
            findNavController().popBackStack()
        }
    }
}