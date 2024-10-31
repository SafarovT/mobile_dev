package com.example.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movies.databinding.MovieFragmentBinding

class MovieFragment : Fragment(R.layout.movie_fragment) {
    private lateinit var binding: MovieFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieFragmentBinding.bind(view)

        val title = arguments?.getString("TITLE")
        val description = arguments?.getString("DESCRIPTION")
        val imageResId = arguments?.getInt("IMAGE_RES_ID")

        binding.title.text = title
        binding.description.text = description
        imageResId?.let {
            binding.cover.setImageResource(imageResId)
        }

        binding.backHistoryButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}