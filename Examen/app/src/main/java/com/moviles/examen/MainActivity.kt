package com.moviles.examen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_crear_so.setOnClickListener {
            irAPantallaSO()
        }

    }

    fun irAPantallaSO(){
        val intent = Intent(this,SOActivity::class.java)
        startActivity(intent)
    }
    fun irAListarSO(){
        finish()
        val intent = Intent(this,ListaSystemOActivity::class.java)
        startActivity(intent)
    }
}
