package com.example.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.MovieItemBinding

class MovieViewHolder(view: View): RecyclerView.ViewHolder(view)

class MovieAdapter(
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {
    var movieList = listOf<Movie>()

    override fun getItemCount(): Int = movieList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = MovieItemBinding.inflate(inflater, parent, false)

        return MovieViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int, ) {
        val itemBinding = MovieItemBinding.bind(holder.itemView)
        val movie = movieList[position]

        itemBinding.title.text = movie.title
        itemBinding.score.text = "${movie.score.format(1)}"
        itemBinding.description.text = movie.description
        itemBinding.cover.setImageResource(movie.imageResId)

        itemBinding.container.setOnClickListener {
            onItemClick(movie)
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)