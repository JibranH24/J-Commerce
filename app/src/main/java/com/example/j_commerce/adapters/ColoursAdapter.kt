package com.example.j_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.j_commerce.databinding.ColourRvItemBinding

class ColoursAdapter : RecyclerView.Adapter<ColoursAdapter.ColorsViewHolder>() {


    private  var selectedPosition = -1
    inner class  ColorsViewHolder(private val binding: ColourRvItemBinding): ViewHolder(binding.root){

        fun bind(colour: Int, position: Int){
            val imageDrawable = ColorDrawable(colour)
            binding.imageColour.setImageDrawable(imageDrawable)
            if(position == selectedPosition){ // colour is selected
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            }else { //colour not selected
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ColourRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

            
    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val colour = differ.currentList[position]
       holder.bind(colour,position)

        holder.itemView.setOnClickListener{
            if(selectedPosition >= 0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(colour)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Int) -> Unit)?=null
}