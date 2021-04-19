package com.example.madlevel5task2.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.R
import com.example.madlevel5task2.databinding.FragmentGameBacklogBinding
import com.example.madlevel5task2.model.Game
import com.google.android.material.snackbar.Snackbar

// A simple [Fragment] subclass as the default destination in the navigation.
class GameBacklogFragment : Fragment() {

    private var _binding: FragmentGameBacklogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private val games = arrayListOf<Game>()
    private val backupGames = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentGameBacklogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeAddReminderResult()

        // Enable a different AppBar for this fragment.
        setHasOptionsMenu(true)

        // Ensure that the AppBar will change back after coming from the HistoryFragment.
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.game_backlog)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        // Navigate to the AddGameFragment upon a click on the floating action button.
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_GameBacklogFragment_to_AddGameFragment)
        }
    }

    // Inflate the custom AppBar.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_game_backlog, menu)
    }

    // Delete all games upon a click on the AppBar trash can, with an option to undo the action.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                backupGames.clear()
                backupGames.addAll(games)
                games.clear()
                gameAdapter.notifyDataSetChanged()

                Snackbar.make(binding.rvGames, R.string.successfully_deleted_backlog, Snackbar.LENGTH_LONG)
                        .addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(snackbar: Snackbar, event: Int) {
                                when (event) {
                                    DISMISS_EVENT_ACTION -> {
                                        games.addAll(backupGames)
                                        gameAdapter.notifyDataSetChanged()
                                    }
                                    else -> {
                                        viewModel.deleteAllGames()
                                    }
                                }
                            }
                        })
                        .setAction(R.string.undo, object : View.OnClickListener {
                            override fun onClick(v: View?) {}
                        })
                        .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Initialize the recycler view with a linear layout manager, adapter.
    private fun initViews() {
        binding.rvGames.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvGames.adapter = gameAdapter
        createItemTouchHelper().attachToRecyclerView(binding.rvGames)
    }

    // Enable touch behavior (like swipe and move) on each ViewHolder, and use callbacks to signal when a user is performing these actions.
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Create the ItemTouch helper, only enable left swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Disable the ability to move items up and down.
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Delete a game upon a swipe to the left, with an option to undo the action.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]

                backupGames.clear()
                backupGames.addAll(games)
                games.removeAt(position)
                gameAdapter.notifyDataSetChanged()

                Snackbar.make(binding.rvGames, R.string.successfully_deleted_game, Snackbar.LENGTH_LONG)
                        .addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(snackbar: Snackbar, event: Int) {
                                when (event) {
                                    DISMISS_EVENT_ACTION -> {
                                        games.clear()
                                        games.addAll(backupGames)
                                        gameAdapter.notifyDataSetChanged()
                                    } else -> {
                                        viewModel.deleteGame(gameToDelete)
                                    }
                                }
                            }
                        })
                        .setAction(R.string.undo, object : View.OnClickListener {
                            override fun onClick(v: View?) {}
                        })
                        .show()
            }
        }
        return ItemTouchHelper(callback)
    }

    // Update the games within the RecyclerView every time the LiveData changes.
    private fun observeAddReminderResult() {
        viewModel.games.observe(viewLifecycleOwner, Observer { games ->
            this@GameBacklogFragment.games.clear()
            this@GameBacklogFragment.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })
    }
}