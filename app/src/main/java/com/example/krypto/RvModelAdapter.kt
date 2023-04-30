package com.example.krypto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.krypto.databinding.RvItemsBinding

class RvModelAdapter(val context: Context,var data:ArrayList<Model>) : RecyclerView.Adapter<RvModelAdapter.RvModelViewHolder>() {
    class RvModelViewHolder(val binding: RvItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    fun changeData(filterData:ArrayList<Model>){
        data=filterData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvModelViewHolder{
        return RvModelViewHolder(
            RvItemsBinding.inflate(LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RvModelAdapter.RvModelViewHolder, position: Int) {
        setAnimation(holder.itemView)
        val currentData=data[position]
        holder.binding.symbol.text=currentData.symbol
        holder.binding.price.text=currentData.price
        holder.binding.name.text=currentData.name
    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun setAnimation(view:View){
        val anim=AlphaAnimation(0.0f,1.0f)
        anim.duration=500
        view.startAnimation(anim)
    }
}