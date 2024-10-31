package com.example.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentFifthBinding

class FifthFragment : Fragment(R.layout.fragment_fifth) {
    private lateinit var binding: FragmentFifthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFifthBinding.bind(view)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}