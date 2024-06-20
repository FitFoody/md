package com.example.fitfoody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfoody.R
import com.example.fitfoody.databinding.ItemHistoryBinding
import com.example.fitfoody.model.HistoryItem

class HistoryItemAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryItemAdapter.ListViewHolder>() {

    // Inner class ListViewHolder
    class ListViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.tvFoodName.text = historyItem.foodmname
            binding.tvNutritionFacs.text = historyItem.nutritionfacts

            Glide.with(itemView.context)
                .load(yourImageUrl)
                .skipMemoryCache(true)
                .into(binding.ivListPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.food.text = historyItem.title
        holder.descriptionTextView.text = historyItem.description
    }

    override fun getItemCount() = historyList.size
}
