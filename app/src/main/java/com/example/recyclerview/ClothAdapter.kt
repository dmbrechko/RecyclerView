package com.example.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ListItemBinding

class ClothAdapter(private val clothes: List<Cloth>, private val onClick: (Int) -> Unit): RecyclerView.Adapter<ClothAdapter.ClothViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ClothViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int {
        return clothes.size
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        holder.bind(clothes[position])
    }

    class ClothViewHolder(private val binding: ListItemBinding, onClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                onClick(adapterPosition)
            }
        }
        fun bind(cloth: Cloth) {
            binding.apply {
                avatarIV.setImageResource(cloth.image)
                nameTV.text = cloth.name
                descTV.text = cloth.desc
            }
        }
    }
}