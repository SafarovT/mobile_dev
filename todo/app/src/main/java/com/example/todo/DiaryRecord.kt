package com.example.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DiaryRecord(
	@PrimaryKey
	val uid: String,

	@ColumnInfo(name = "title")
	val title: String,

	@ColumnInfo(name = "content")
	val content: String,

	@ColumnInfo(name = "due_to")
	val dueTo: Long,

	@ColumnInfo(name = "status")
	var status: TaskStatus
)