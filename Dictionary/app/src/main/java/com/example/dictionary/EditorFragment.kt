package com.example.dictionary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentEditorBinding

class EditorFragment : Fragment(R.layout.fragment_editor) {
    private val viewModel: ListViewModel by activityViewModels()
    private lateinit var binding: FragmentEditorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditorBinding.bind(view)

        val navController = findNavController()

        binding.toolbar.setNavigationOnClickListener {
            navController.navigate(R.id.action_editorFragment_to_listFragment)
        }

        if (arguments == null) {
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                     resources.getString(R.string.save_record) -> {
                        onCreateButtonClick()
                        true
                    }
                    else -> false
                }
            }
        } else {
            binding.recordTitle.setText(requireArguments().getCharSequence("TITLE"))
            binding.recordContent.setText(requireArguments().getCharSequence("CONTENT"))
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    resources.getString(R.string.save_record) -> {
                        updateDiaryRecord(
                            uid = requireArguments().getString("ID") as String,
                            requireArguments().getLong("DATE")
                        )
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun updateDiaryRecord(uid: String, createdAt: Long) {
        if (binding.recordTitle.text.isEmpty()) {
            return
        }

        viewModel.updateDiaryRecord(
            uid,
            title = binding.recordTitle.text.toString(),
            content = binding.recordContent.text.toString(),
            createdAt
        )

        findNavController().navigate(R.id.action_editorFragment_to_listFragment)
    }

    private fun onCreateButtonClick() {
        if (binding.recordTitle.text.isEmpty()) {
            return
        }

        viewModel.createDiaryRecord(
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString()
        )

        findNavController().navigate(R.id.action_editorFragment_to_listFragment)
    }
}