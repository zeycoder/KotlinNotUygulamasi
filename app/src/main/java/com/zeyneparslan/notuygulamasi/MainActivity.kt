package com.zeyneparslan.notuygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.not_ekle,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.not_ekleme_item){
            val action=NotListesiFragmentDirections.actionNotListesiFragmentToTumAyrintilarFragment("menudengeldim",0)
            Navigation.findNavController(this,R.id.fragment).navigate(action)
        }


        return super.onOptionsItemSelected(item)
    }






}