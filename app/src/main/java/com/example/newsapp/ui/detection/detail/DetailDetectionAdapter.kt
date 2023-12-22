package com.example.newsapp.ui.detection.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.model.food.ListInformation
import com.example.newsapp.databinding.LayoutDetailDetectionItemBinding

class DetailDetectionAdapter :
    ListAdapter<ListInformation, DetailDetectionAdapter.ViewHolder>(MyDiffUtil()) {

    inner class ViewHolder(private val binding: LayoutDetailDetectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListInformation) {
            with(binding) {
                dataItemBind = item
            }
        }
    }

    private class MyDiffUtil : DiffUtil.ItemCallback<ListInformation>() {
        override fun areItemsTheSame(oldItem: ListInformation, newItem: ListInformation): Boolean {
            return oldItem.information == newItem.information
        }

        override fun areContentsTheSame(
            oldItem: ListInformation,
            newItem: ListInformation
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutDetailDetectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}