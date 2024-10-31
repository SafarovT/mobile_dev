package com.example.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentFourthBinding

class FourthFragment : Fragment(R.layout.fragment_fourth) {
    private lateinit var binding: FragmentFourthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFourthBinding.bind(view)

        binding.next.setOnClickListener {
            findNavController()
                .navigate(R.id.action_fourthFragment_to_fifthFragment)
        }

        binding.back.setOnClickListener {
            findNavController()
                .popBackStack()
        }
    }
}