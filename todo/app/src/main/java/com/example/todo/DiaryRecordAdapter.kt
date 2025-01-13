package com.example.todo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemDiaryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

	private val binding = ItemDiaryBinding.bind(view)

	fun bindData(
		diaryRecord: DiaryRecord,
		navigateToEditor: (Bundle) -> Unit,
		removeDiaryRecord: (DiaryRecord) -> Unit,
		updateStatus: (String, TaskStatus) -> Unit
	) {
		binding.recordTitle.text = diaryRecord.title
		binding.recordContent.text = diaryRecord.content

		val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(diaryRecord.dueTo)
		binding.recordDate.text = formattedDate

		binding.taskStatusToggle.tag = diaryRecord.uid

		when (diaryRecord.status) {
			TaskStatus.CREATED -> binding.taskStatusToggle.check(R.id.status_created)
			TaskStatus.IN_PROGRESS -> binding.taskStatusToggle.check(R.id.status_in_progress)
			TaskStatus.DONE -> binding.taskStatusToggle.check(R.id.status_done)
		}

		// Set the listener for the toggle group
		binding.taskStatusToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
			if (isChecked) {
				val uid = group.tag as String
				val newStatus = when (checkedId) {
					R.id.status_created -> TaskStatus.CREATED
					R.id.status_in_progress -> TaskStatus.IN_PROGRESS
					R.id.status_done -> TaskStatus.DONE
					else -> {
						return@addOnButtonCheckedListener
					}
				}
				updateStatus(uid, newStatus)
			}
		}

		binding.root.setOnClickListener {
			val arguments = Bundle().apply {
				putString("TITLE", diaryRecord.title)
				putString("CONTENT", diaryRecord.content)
				putString("ID", diaryRecord.uid)
				putLong("DATE", diaryRecord.dueTo)
				putString("STATUS", diaryRecord.status.name)
			}
			navigateToEditor(arguments)
		}

		binding.recordDeleteButton.setOnClickListener {
			removeDiaryRecord(diaryRecord)
		}
	}
}

class DiaryAdapter(
	private val gotoEditorFn: (arguments: Bundle) -> Unit,
	private val deleteDiaryRecord: (diaryRecord: DiaryRecord) -> Unit,
	private val editDiaryRecord: (diaryRecord: DiaryRecord) -> Unit,
	private val viewModel: DiaryViewModel // Добавляем viewModel в адаптер
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var groupedTasks: List<Any> = emptyList()

	companion object {
		private const val TYPE_HEADER = 0
		private const val TYPE_TASK = 1
	}

	fun setData(newList: List<DiaryRecord>) {
		groupedTasks = viewModel.groupTasksByDate(newList)
		notifyDataSetChanged()
	}

	override fun getItemViewType(position: Int): Int {
		return when (groupedTasks[position]) {
			is String -> TYPE_HEADER
			is DiaryRecord -> TYPE_TASK
			else -> throw IllegalArgumentException("Invalid type")
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			TYPE_HEADER -> {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_header, parent, false)
				HeaderViewHolder(view)
			}
			TYPE_TASK -> {
				val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
				DiaryViewHolder(view)
			}
			else -> throw IllegalArgumentException("Invalid view type")
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is HeaderViewHolder -> {
				val header = groupedTasks[position] as String
				holder.bind(header)
			}
			is DiaryViewHolder -> {
				val task = groupedTasks[position] as DiaryRecord
				holder.bindData(task, gotoEditorFn, deleteDiaryRecord) { uid, newStatus ->
					val index = groupedTasks.indexOfFirst { it is DiaryRecord && it.uid == uid }
					if (index != -1) {
						val updatedRecord = (groupedTasks[index] as DiaryRecord).copy(status = newStatus)
						editDiaryRecord(updatedRecord)
					}
				}
			}
		}
	}

	override fun getItemCount(): Int = groupedTasks.size

	class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val binding = view as TextView

		fun bind(header: String) {
			binding.text = header
		}
	}
}
