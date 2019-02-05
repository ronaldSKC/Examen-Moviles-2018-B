package com.moviles.examen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_lista_system_o.*


class ListaSystemOActivity : AppCompatActivity() {

    lateinit var adaptador: SOAdapter
    lateinit var sysOperative: ArrayList<OperativeSystem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_system_o)

        val layoutManager = LinearLayoutManager(this)
        adaptador = SOAdapter(sysOperative)
        rv_so.layoutManager = layoutManager
        rv_so.itemAnimator = DefaultItemAnimator()
        rv_so.adapter = adaptador
        adaptador.notifyDataSetChanged()

        registerForContextMenu(rv_so)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        var position = adaptador.getPosition()
        var sysOperative = sysOperative[position]

        when (item.itemId) {

            R.id.editar_so -> {
                val intent = Intent(this, SOActivity::class.java)
                intent.putExtra("tipo", "Edit")
                intent.putExtra("operative_system", sysOperative)
                startActivity(intent)
                return true
            }
            R.id.eliminar_so -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Esta seguro de eliminar?")
                    .setPositiveButton("Si", { dialog, which ->
                        DatabaseSO.eliminarSO(sysOperative.id)
                        finish()
                        startActivity(intent)
                    }
                    )
                    .setNegativeButton("No", null)
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            else -> {
                Log.i("menu", "Todos los demas")
                return super.onOptionsItemSelected(item)
            }
        }
    }
}


