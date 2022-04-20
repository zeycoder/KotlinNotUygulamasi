package com.zeyneparslan.notuygulamasi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_tum_ayrintilar.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.jar.Manifest


@Suppress("DEPRECATION")
class TumAyrintilarFragment : Fragment() {
    var secilenGorsel : Uri?=null
    var secilenBitmap : Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tum_ayrintilar, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener{
            kaydet(it)
        }
        imageView.setOnClickListener{
            gorselSec(it)
        }
        arguments?.let {
            var gelenBilgi = TumAyrintilarFragmentArgs.fromBundle(it).bilgi
            if (gelenBilgi.equals("menudengeldim")){
                //yeni bir yemek eklemeye geldi
                ayrintiIsmiText.setText("")
                ayrintiAciklamaText.setText("")
                button.visibility = View.VISIBLE

                val gorselSecmeArkaPlani = BitmapFactory.decodeResource(context?.resources,R.drawable.resim)
                imageView.setImageBitmap(gorselSecmeArkaPlani)
            }else{
                //daha önce oluşturulan bir yemeği oluşturmaya geldi
                button.visibility = View.INVISIBLE

                val secilenId = TumAyrintilarFragmentArgs.fromBundle(it).id
                context?.let {
                    try {
                        val db = it.openOrCreateDatabase("Notlar",Context.MODE_PRIVATE,null)
                        val cursor = db.rawQuery("SELECT * FROM notlar WHERE id = ?", arrayOf(secilenId.toString()))
                        val notIsmiIndex = cursor.getColumnIndex("notismi")
                        val notAciklamasiIndex = cursor.getColumnIndex("notaciklamasi")
                        val notGorseli = cursor.getColumnIndex("gorsel")

                        while (cursor.moveToNext()){
                            ayrintiIsmiText.setText(cursor.getString(notIsmiIndex))
                            ayrintiAciklamaText.setText(cursor.getString(notAciklamasiIndex))

                            val byteDizisi = cursor.getBlob(notGorseli)
                            val bitmap = BitmapFactory.decodeByteArray(byteDizisi,0,byteDizisi.size)
                            imageView.setImageBitmap(bitmap)
                        }
                        cursor.close()


                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }
        }
    }


    fun kaydet(view: View){
        //SQLite'a kaydetme
        val notIsmı = ayrintiIsmiText.text.toString()
        val notAciklamasi = ayrintiAciklamaText.text.toString()

        if (secilenBitmap != null){
            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteDizisi = outputStream.toByteArray()

            try {
                println("try içi sql kodlarının kısmı")
                context?.let {

                    val database = it.openOrCreateDatabase("Notlar", Context.MODE_PRIVATE,null)
                    //Burayı algılamıyo
                    database.execSQL("CREATE TABLE IF NOT EXISTS notlar (id INTEGER PRIMARY KEY, notismi VARCHAR, notaciklamasi VARCHAR , gorsel BLOB)")
                    val sqlString = "INSERT INTO notlar ( notismi , notaciklamasi , gorsel ) VALUES(?,?,?)"
                    val statement = database.compileStatement(sqlString)
                    println("try içi sql kodlarının kısmı2")
                    statement.bindString(1,notIsmı)
                    statement.bindString(2,notAciklamasi)
                    statement.bindBlob(3,byteDizisi)
                    statement.execute()

                }

            }catch (e : Exception){
                e.printStackTrace()
            }
            val action = TumAyrintilarFragmentDirections.actionTumAyrintilarFragmentToNotListesiFragment()
            Navigation.findNavController(view).navigate(action)
            println("şwldğsşpwl")
        }


    }


    fun gorselSec(view: View){
        activity?.let{
            if(ContextCompat.checkSelfPermission(it.applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //izin verilmedi, izin istememiz gerekiyo
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }else{
                //izin zaten verilmiş, tekrar istemeden galeriye git
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
                println("izin")
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //İzni aldık
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data!=null){
            secilenGorsel = data.data
            println("sadfghjkg")
            try{
                context?.let {
                    if (secilenGorsel!=null ){
                        if (Build.VERSION.SDK_INT>=28){
                            val source = ImageDecoder.createSource(it.contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            imageView.setImageBitmap(secilenBitmap)

                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel)
                            imageView.setImageBitmap(secilenBitmap)
                        }
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun kucukBitmapOlustur(kullanicininSectigiBitmap:Bitmap,maximumBoyut:Int) : Bitmap {
        var width = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        val bitmapOrani : Double = width.toDouble() / height.toDouble()

        if (bitmapOrani>1){
            //Gorsel yatay
            width = maximumBoyut
            val kisaltilmisHeight = width/bitmapOrani
            height = kisaltilmisHeight.toInt()
        }else{
            //Gorsel dikey
            height = maximumBoyut
            val kisaltilmisWidth = height*bitmapOrani
            width = kisaltilmisWidth.toInt()

        }

        return Bitmap.createScaledBitmap(kullanicininSectigiBitmap,width,height,true)
    }
}