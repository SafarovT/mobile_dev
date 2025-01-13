package com.example.project

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.project.databinding.FragmentTaskEditBinding
import java.util.Calendar

class FragmentTaskEdit : Fragment(R.layout.fragment_task_edit) {
    private val viewModel: DiaryViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskEditBinding
    private var dueToDate: Long? = null // Переменная для хранения выбранной даты

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTaskEditBinding.bind(view)

        // Обработчик для кнопки "Назад"
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTaskEdit_to_fragmentDiary)
        }

        // Обработчик для выбора даты
        binding.dueToDate.setOnClickListener {
            showDatePicker()
        }

        // Проверка аргументов (редактирование или создание записи)
        if (arguments == null) {
            // Режим создания новой записи
            binding.createButton.setOnClickListener { onCreateButtonClick() }
        } else {
            // Режим редактирования существующей записи
            binding.recordTitle.setText(requireArguments().getCharSequence("TITLE"))
            binding.recordContent.setText(requireArguments().getCharSequence("CONTENT"))
            dueToDate = requireArguments().getLong("DATE") // Устанавливаем дату из аргументов
            binding.dueToDate.text = formatDate(dueToDate) // Отображаем дату

            binding.createButton.setOnClickListener {
                val uid = requireArguments().getString("ID") ?: throw IllegalArgumentException("ID is required")
                val status = requireArguments().getString("STATUS")?.let {
                    try {
                        TaskStatus.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        TaskStatus.CREATED
                    }
                } ?: TaskStatus.CREATED

                updateDiaryRecord(uid = uid, status = status)
            }
        }

        // Блокировка кнопки "галочка", если дата не выбрана
        binding.createButton.isEnabled = dueToDate != null
    }

    // Показать DatePickerDialog для выбора даты
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Сохраняем выбранную дату
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                dueToDate = selectedDate.timeInMillis

                // Отображаем выбранную дату
                binding.dueToDate.text = formatDate(dueToDate)

                // Разблокируем кнопку "галочка"
                binding.createButton.isEnabled = true
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    // Форматирование даты в строку
    private fun formatDate(date: Long?): String {
        if (date == null) return "Выберите дату выполнения"
        val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
        return dateFormat.format(date)
    }

    // Создание новой записи
    private fun onCreateButtonClick() {
        if (binding.recordTitle.text.isEmpty() || dueToDate == null) {
            return
        }

        viewModel.createDiaryRecord(
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString(),
            dueToDate!! // Используем выбранную дату
        )

        findNavController().navigate(R.id.action_fragmentTaskEdit_to_fragmentDiary)
    }

    // Обновление существующей записи
    private fun updateDiaryRecord(uid: String, status: TaskStatus) {
        if (binding.recordTitle.text.isEmpty() || dueToDate == null) {
            return
        }

        viewModel.updateDiaryRecord(
            uid,
            title = binding.recordTitle.text.toString(),
            content = binding.recordContent.text.toString(),
            dueTo = dueToDate!!,
            status
        )

        findNavController().navigate(R.id.action_fragmentTaskEdit_to_fragmentDiary)
    }
}