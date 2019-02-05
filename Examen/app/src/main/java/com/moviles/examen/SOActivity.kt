package com.moviles.examen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_so.*

class SOActivity : AppCompatActivity() {
    var OperativeSystem: OperativeSystem? = null
    var tipo = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_so)
        
        val type = intent.getStringExtra("tipo")
        
        if(type.equals("Edit")){
            textView.text= "Editar S.O."
            OperativeSystem = intent.getParcelableExtra("sistema_operativo")
            fillFields()
            tipo =  true
        }
        
        boton_registrar_so.setOnClickListener {
            this.irListaSO()
        }
    }
    fun fillFields() {
        txt_nombre.setText(OperativeSystem?.nombre)
        txt_fecha.setText(OperativeSystem?.fechaLanzamiento)
        txt_peso.setText(OperativeSystem?.peso_gigas)
        txt_version.setText(OperativeSystem?.version)

        
    }
    fun irListaSO(){
        if (txt_nombre.text.toString().isEmpty()||
            txt_fecha.text.toString().isEmpty()||
            txt_peso.text.toString().isEmpty()||
            txt_version.text.toString().isEmpty()){
            Alerter.create(this)
                .setTitle("Campos Vacios")
                .setText("Compleya la información de todos los campos")
                .setBackgroundColorRes(R.color.error_color_material_dark)
                .enableSwipeToDismiss()
                .show()
        }
        else{
            var nombre = txt_nombre.text.toString()
            var fecha = txt_fecha.text.toString()
            var peso = txt_peso.text.toString()
            var version = txt_version.text.toString()
            
            if(!tipo){
               var so = OperativeSystem(0,nombre,fecha,peso,version)        
               DatabaseSO.insertarSO(so)
            }
            Toasty.success(this, "Datos Registrados", Toast.LENGTH_LONG, true).show()
            this.irALista()
        }

    }
    fun irALista(){
    
        val intent = Intent(this, ListaSystemOActivity::class.java)
        startActivity(intent)
    }
}
