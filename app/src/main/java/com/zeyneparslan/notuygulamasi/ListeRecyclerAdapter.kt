package com.zeyneparslan.notuygulamasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row.view.*

class ListeRecyclerAdapter(val notListesi:ArrayList<String>,val idListesi:ArrayList<Int>) : RecyclerView.Adapter<ListeRecyclerAdapter.NotHolder>() {
    class NotHolder(itemview:View) : RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return NotHolder(view)
    }

    override fun onBindViewHolder(holder: NotHolder, position: Int) {
        holder.itemView.recycler_row_text.text= notListesi[position]
        holder.itemView.setOnClickListener{
            val action = NotListesiFragmentDirections.actionNotListesiFragmentToTumAyrintilarFragment("recyclerdangeldim", idListesi[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return notListesi.size
    }
}