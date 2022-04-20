package com.zeyneparslan.notuygulamasi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_not_listesi.*
import java.lang.Exception

class NotListesiFragment : Fragment() {
    var notIsmiListesi = ArrayList<String>()
    var notIdListesi = ArrayList<Int>()
    private lateinit var listeAdapter : ListeRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_listesi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        listeAdapter = ListeRecyclerAdapter(notIsmiListesi,notIdListesi)
        recyclerView2.layoutManager = LinearLayoutManager(context)
        recyclerView2.adapter = listeAdapter

        sqlVeriAlma()
    }
    fun sqlVeriAlma(){
        try {
            activity?.let {
                val database = it.openOrCreateDatabase("Notlar", Context.MODE_PRIVATE,null)
                val cursor=database.rawQuery("SELECT * FROM notlar",null)
                val notIsmiIndex = cursor.getColumnIndex("notismi")
                val notIdIndex = cursor.getColumnIndex("id")

                notIsmiListesi.clear()
                notIdListesi.clear()
                while (cursor.moveToNext()){

                    notIsmiListesi.add(cursor.getString(notIsmiIndex))
                    notIdListesi.add(cursor.getInt(notIdIndex))
                }

                listeAdapter.notifyDataSetChanged()
                cursor.close()
            }


        }catch (e:Exception){

        }
    }


}