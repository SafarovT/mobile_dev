package com.example.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.ItemBinding
import java.util.Calendar
import java.util.Date

class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view)

class DiaryAdapter(
    private val gotoEditorFn: (arguments: Bundle) -> Unit,
    private val deleteDiaryRecord: (diaryRecord: DiaryRecord) -> Unit
) : RecyclerView.Adapter<DiaryViewHolder>() {
    var diaryRecordList = listOf<DiaryRecord>()

    override fun getItemCount() = diaryRecordList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)

        return DiaryViewHolder(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val itemBinding = ItemBinding.bind(holder.itemView)
        val diaryRecord = diaryRecordList[position]

        itemBinding.recordTitle.text = diaryRecord.title
        itemBinding.recordContent.text = diaryRecord.content

        val createdAt = Date(diaryRecord.createdAt)
        val calendar = Calendar.getInstance()
        calendar.time = createdAt
        itemBinding.recordDate.text = "${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.YEAR)}"

        holder.itemView.setOnClickListener {
            val arguments = Bundle().apply {
                putString("TITLE", diaryRecord.title)
                putString("CONTENT", diaryRecord.content)
                putString("ID", diaryRecord.uid)
                putLong("DATE", diaryRecord.createdAt)
            }

            gotoEditorFn(arguments)
        }

        itemBinding.recordDeleteButton.setOnClickListener {
            deleteDiaryRecord(diaryRecord)
        }
    }
}