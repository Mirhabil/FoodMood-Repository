package com.example.foodapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapplication.databinding.AiitemRecievedBinding
import com.example.foodapplication.databinding.AiitemSentBinding

class AiAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LEFT = 1
    private val VIEW_TYPE_RIGHT = 2

    private var list = mutableListOf<String>()

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_LEFT else VIEW_TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LEFT) {
            val binding = AiitemSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AiViewHolderSent(binding)
        } else {
            val binding = AiitemRecievedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AiViewHolderReceived(binding)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder is AiViewHolderSent) {
            holder.bind(message)
        } else if (holder is AiViewHolderReceived) {
            holder.bind(message)
        }
    }

    fun updateList(newList: MutableList<String>) {
        list = newList
        notifyDataSetChanged()
    }
}

class AiViewHolderSent(private val binding: AiitemSentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: String) {
        binding.tvSent.text = message  // Ensure you have a TextView with id `textViewSent`
    }
}

class AiViewHolderReceived(private val binding: AiitemRecievedBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: String) {
        binding.tvRecieved.text = message  // Ensure you have a TextView with id `textViewReceived`
    }
}