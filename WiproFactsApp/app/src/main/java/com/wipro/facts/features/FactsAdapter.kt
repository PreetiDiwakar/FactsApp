package com.wipro.facts.features

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wipro.facts.data.Facts
import com.wipro.facts.databinding.FactlistItemBinding

class FactsAdapter : ListAdapter<Facts, FactsAdapter.FactsViewHolder>(FactsComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
       val binding =
           FactlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FactsViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class FactsViewHolder(private val binding: FactlistItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(factslist: Facts) {
            binding.apply {
                    factTitle.text = factslist.title
                    factDescription.text = factslist.description
                    Glide.with(context).load(factslist.imageHref)
                        .into(factImage)
                }
            }
        }


    class FactsComparator : DiffUtil.ItemCallback<Facts>() {
        override fun areItemsTheSame(oldItem: Facts, newItem: Facts) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Facts, newItem: Facts) =
            oldItem == newItem
    }
}
