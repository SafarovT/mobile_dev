package com.example.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentThirdBinding


class ThirdFragment : Fragment(R.layout.fragment_third) {
    private lateinit var binding: FragmentThirdBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)


        val name = arguments?.getString(UserData.NAME)
        val surname = arguments?.getString(UserData.SURNAME)
        val birthDate = arguments?.getString(UserData.BIRTH_DATE)
        binding.name.text = name.toString()
        binding.surname.text = surname.toString()
        binding.birthDate.text = birthDate.toString()

        binding.back.setOnClickListener {
            findNavController()
                .navigate(R.id.action_thirdFragment_to_firstFragment)
        }
    }
}