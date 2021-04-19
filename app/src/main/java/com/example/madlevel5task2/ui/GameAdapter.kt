package com.example.madlevel5task2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.R
import com.example.madlevel5task2.databinding.ItemGameBinding
import com.example.madlevel5task2.model.Game
import java.text.SimpleDateFormat

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemGameBinding.bind(itemView)

        // Fill the RecyclerView with the title, platform and release date of the game.
        fun databind(game: Game) {
            binding.tvTitle.text = game.title
            binding.tvPlatform.text = game.platform
            val releaseDate = SimpleDateFormat("dd MMMM yyyy").format(game.releaseDate)
            binding.tvReleaseDate.text = "Release: $releaseDate"
        }
    }

    // Create and return a ViewHolder object, inflate a standard layout called simple_list_item_1.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false))
    }

    // Return the size of the list.
    override fun getItemCount(): Int {
        return games.size
    }

    // Display the data at the specified position, called by RecyclerView.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(games[position])
    }
}