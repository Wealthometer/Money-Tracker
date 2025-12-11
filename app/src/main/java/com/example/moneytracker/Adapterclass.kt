package com.example.moneytracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter


class Adapterclass(val list: List<data>) : RecyclerView.Adapter<Adapterclass.myViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
       val infalter = LayoutInflater.from(parent.context).inflate(R.layout.history_recycler_view , parent, false)
        return myViewholder(infalter)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        holder.textAmt.text = list[position].amt
        holder.Reasonrv.text = list[position].description


    }

    class myViewholder (itemView: View): RecyclerView.ViewHolder(itemView){
        val textAmt = itemView.findViewById<TextView>(R.id.amountrv)
        val Reasonrv = itemView.findViewById<TextView>(R.id.reasonrv)
    }
}

