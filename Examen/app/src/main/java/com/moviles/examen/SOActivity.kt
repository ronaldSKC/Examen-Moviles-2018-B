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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_so)
        boton_cancelar_reg_so.setOnClickListener {
             this.irAMain()
        }
        boton_registrar_so.setOnClickListener {
            this.irListaSO()
        }
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
            var so = OperativeSystem(0,nombre,fecha,peso,version)
            DatabaseSO.insertarSO(so)
            Toasty.success(this, "Datos Registrados", Toast.LENGTH_LONG, true).show()
            val intent = Intent(this, ListaSystemOActivity::class.java)
            startActivity(intent)
        }

    }
    fun irAMain(){
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
