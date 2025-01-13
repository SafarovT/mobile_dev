package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class RecordsState(
    val allRecords: List<DiaryRecord>,
    val filteredRecords: List<DiaryRecord>,
    val isFiltered: Boolean,
)

class ListViewModel : ViewModel() {
    private val _state = MutableStateFlow(RecordsState(emptyList(), emptyList(), false))
    val state: StateFlow<RecordsState> = _state
    private var filterDate: Date? = null

    init {
        viewModelScope.launch {
            StorageApp.db.diaryRecordDao().getAllAsFlow().collect { allRecords ->
                _state.value = _state.value.copy(
                    allRecords = allRecords,
                    filteredRecords = filterRecords(allRecords),
                    isFiltered = filterDate != null
                )
            }
        }
    }

    fun filterRecordsByDate(date: Date?) {
        filterDate = date
        _state.value = _state.value.copy(
            filteredRecords = filterRecords(_state.value.allRecords),
            isFiltered = date != null,
        )
    }

    private fun filterRecords(records: List<DiaryRecord>): List<DiaryRecord> {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return if (filterDate == null) {
            records
        } else {
            records.filter { diaryRecord ->
                formatter.format(diaryRecord.createdAt) == formatter.format(filterDate)
            }
        }
    }

    suspend fun getRecords(): List<DiaryRecord> {
        val deferred = viewModelScope.async {
            StorageApp.db.diaryRecordDao().getAll()
        }

        return deferred.await()
    }

    fun createDiaryRecord(title: String, content: String) {
        viewModelScope.launch {
            val diaryRecordDao = StorageApp.db.diaryRecordDao()
            if (diaryRecordDao.findByTitle(title) != null) {
                return@launch
            }

            val randomUid = UUID.randomUUID().toString()
            val newDiaryRecord = DiaryRecord(
                randomUid,
                title,
                content,
                System.currentTimeMillis(),
            )

            diaryRecordDao.insertAll(newDiaryRecord)
        }
    }

    fun updateDiaryRecord(uid: String, title: String, content: String, date: Long) {
        viewModelScope.launch {

            val diaryRecordDao = StorageApp.db.diaryRecordDao()

            val diaryRecord = DiaryRecord(
                uid,
                title,
                content,
                date,
            )

            diaryRecordDao.updateAll(diaryRecord)
        }
    }

    fun deleteDiaryRecord(diaryRecord: DiaryRecord) {
        viewModelScope.launch {
            val diaryRecordDao = StorageApp.db.diaryRecordDao()
            diaryRecordDao.delete(diaryRecord)
        }
    }
}