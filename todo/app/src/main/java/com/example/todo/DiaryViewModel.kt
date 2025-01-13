package com.example.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class RecordsState(
	val allRecords: List<DiaryRecord>,
	val filteredRecords: List<DiaryRecord>,
	val isFiltered: Boolean
)

class DiaryViewModel : ViewModel() {
	private val _state = MutableStateFlow(RecordsState(emptyList(), emptyList(), false))
	val state: StateFlow<RecordsState> = _state

	private var currentFilterDate: Date? = null
	private var currentSearchQuery: String = ""
	private var currentFilterStatus: TaskStatus? = null


	init {
		viewModelScope.launch {
			StorageApp.db.diaryRecordDao().getAllAsFlow().collect { allRecords ->
				_state.value = _state.value.copy(
					allRecords = allRecords,
					filteredRecords = applyFilters(allRecords),
					isFiltered = currentFilterDate != null || currentSearchQuery.isNotEmpty()
				)
			}
		}
	}

	fun filterRecordsByDate(date: Date?) {
		currentFilterDate = date
		_state.value = _state.value.copy(
			filteredRecords = applyFilters(_state.value.allRecords),
			isFiltered = currentFilterDate != null || currentSearchQuery.isNotEmpty()
		)
	}

	fun filterRecordsByQuery(query: String) {
		currentSearchQuery = query
		_state.value = _state.value.copy(
			filteredRecords = applyFilters(_state.value.allRecords),
			isFiltered = currentFilterDate != null || currentSearchQuery.isNotEmpty()
		)
	}

	fun filterByStatus(status: TaskStatus?) {
		currentFilterStatus = status
		_state.value = _state.value.copy(
			filteredRecords = applyFilters(_state.value.allRecords),
			isFiltered = currentFilterDate != null || currentSearchQuery.isNotEmpty() || status != null
		)
	}

	init {
		viewModelScope.launch {
			StorageApp.db.diaryRecordDao().getAllAsFlow().collect { allRecords ->
				_state.value = _state.value.copy(
					allRecords = allRecords,
					filteredRecords = applyFilters(allRecords)
				)
			}
		}
	}

	private fun applyFilters(records: List<DiaryRecord>): List<DiaryRecord> {
		// Фильтрация по дате
		val dateFilteredRecords = if (currentFilterDate == null) {
			records
		} else {
			val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
			records.filter { diaryRecord ->
				formatter.format(diaryRecord.dueTo) == formatter.format(currentFilterDate)
			}
		}

		// Фильтрация по поисковому запросу
		val searchFilteredRecords = if (currentSearchQuery.isBlank()) {
			dateFilteredRecords
		} else {
			dateFilteredRecords.filter { record ->
				record.title.contains(currentSearchQuery, ignoreCase = true) ||
						record.content.contains(currentSearchQuery, ignoreCase = true)
			}
		}

		// Фильтрация по статусу
		val statusFilteredRecords = if (currentFilterStatus == null) {
			searchFilteredRecords
		} else {
			searchFilteredRecords.filter { it.status == currentFilterStatus }
		}

		// Сортировка по дате выполнения (dueTo)
		return statusFilteredRecords.sortedBy { it.dueTo }
	}

	fun createDiaryRecord(title: String, content: String, dueTo: Long) {
		viewModelScope.launch {
			val diaryRecordDao = StorageApp.db.diaryRecordDao()
			val randomUid = UUID.randomUUID().toString()
			val newDiaryRecord = DiaryRecord(
				randomUid,
				title,
				content,
				dueTo,
				TaskStatus.CREATED
			)
			diaryRecordDao.insertAll(newDiaryRecord)
		}
	}

	fun updateDiaryRecord(uid: String, title: String, content: String, dueTo: Long, status: TaskStatus) {
		viewModelScope.launch {
			val diaryRecordDao = StorageApp.db.diaryRecordDao()
			val diaryRecord = DiaryRecord(
				uid,
				title,
				content,
				dueTo,
				status
			)
			diaryRecordDao.updateAll(diaryRecord)
		}
	}

	fun groupTasksByDate(tasks: List<DiaryRecord>): List<Any> {
		val today = Calendar.getInstance().apply {
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)
			set(Calendar.MILLISECOND, 0)
		}.timeInMillis

		val nextWeek = Calendar.getInstance().apply {
			add(Calendar.DAY_OF_YEAR, 7)
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.SECOND, 0)
			set(Calendar.MILLISECOND, 0)
		}.timeInMillis

		val todayTasks = tasks.filter { it.dueTo in today until today + 86400000 } // Сегодня
		val weekTasks = tasks.filter { it.dueTo in today + 86400000 until nextWeek } // На неделе
		val otherTasks = tasks.filter { it.dueTo >= nextWeek || it.dueTo < today } // Остальные

		val groupedTasks = mutableListOf<Any>()
		if (todayTasks.isNotEmpty()) {
			groupedTasks.add("Сегодня")
			groupedTasks.addAll(todayTasks)
		}
		if (weekTasks.isNotEmpty()) {
			groupedTasks.add("На неделе")
			groupedTasks.addAll(weekTasks)
		}
		if (otherTasks.isNotEmpty()) {
			groupedTasks.add("Остальные")
			groupedTasks.addAll(otherTasks)
		}

		return groupedTasks
	}

	fun deleteDiaryRecord(diaryRecord: DiaryRecord) {
		viewModelScope.launch {
			val diaryRecordDao = StorageApp.db.diaryRecordDao()
			diaryRecordDao.delete(diaryRecord)
		}
	}
}


