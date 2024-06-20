package com.example.fitfoody.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitfoody.databinding.ItemHistoryBinding
import com.example.fitfoody.model.HistoryItem

class HistoryItemAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryItemAdapter.ListViewHolder>() {

    // Inner class ListViewHolder
    class ListViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.tvFoodName.text = historyItem.foodName
            binding.tvNutritionFacts.text = historyItem.nutritionFacts

            Glide.with(itemView.context)
                .load(historyItem.imageUrl)
                .skipMemoryCache(true)
                .into(binding.ivListPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount() = historyList.size
}
