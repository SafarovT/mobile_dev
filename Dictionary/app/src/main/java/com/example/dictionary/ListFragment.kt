package com.example.dictionary

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentListBinding
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {
    private val viewModel: ListViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding
    private lateinit var diaryAdapter: DiaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)
        diaryAdapter = DiaryAdapter(
            deleteDiaryRecord = { diaryRecord: DiaryRecord ->
                viewModel.deleteDiaryRecord(diaryRecord)
            },
            gotoEditorFn = { args: Bundle ->
                findNavController().navigate(R.id.action_listFragment_to_editorFragment, args)
            }
        )
        binding.listView.adapter = diaryAdapter

        binding.createButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_editorFragment)
        }

        binding.viewCreateButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_editorFragment)
        }

        lifecycleScope.launch {
            viewModel.state.collect { diaryRecords ->
                diaryAdapter.diaryRecordList = diaryRecords.filteredRecords
                diaryAdapter.notifyDataSetChanged()
                updateVisibility(diaryRecords.filteredRecords.isEmpty())

                binding.toolbar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.title) {
                        resources.getString(R.string.filter) -> {
                            if (diaryRecords.isFiltered) {
                                resetFilter()
                            }
                            else {
                                showDatePicker()
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                requireActivity().moveTaskToBack(true)
//            }
//        })
    }

    private fun updateVisibility(isEmpty: Boolean) {
        binding.viewDiary.visibility = View.GONE
        binding.emptyDiary.visibility = View.GONE
        if (isEmpty) {
            binding.emptyDiary.visibility = View.VISIBLE
            binding.viewDiary.visibility = View.GONE
        } else {
            binding.emptyDiary.visibility = View.GONE
            binding.viewDiary.visibility = View.VISIBLE
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), {_, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            viewModel.filterRecordsByDate(selectedDate.time)
            binding.toolbar.menu.get(0).setIcon(R.drawable.filter_active)
        }, year, month, day).show()
    }

    private fun resetFilter() {
        viewModel.filterRecordsByDate(null)
        binding.toolbar.menu.get(0).setIcon(R.drawable.filter)
    }
}