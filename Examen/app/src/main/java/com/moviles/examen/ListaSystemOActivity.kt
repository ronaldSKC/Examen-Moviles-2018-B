package com.moviles.examen

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_lista_system_o.*


class ListaSystemOActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_system_o)

        val layoutManager = LinearLayoutManager(this)
        val rv = rv_so
        val adaptador = SOAdaptador(DatabaseSO.sistemasOperativos, this, rv)

        rv_so.layoutManager = layoutManager
        rv_so.itemAnimator = DefaultItemAnimator()
        rv_so.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }
    fun refrescar(){
        finish()
        val dirccion="http://${DatabaseSO.ip}:1337/OperativeSystema"
        DatabaseSO.getList(dirccion)
        startActivity(getIntent())
    }
    fun compartir(contenido:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,contenido)
            type ="text/plain"
        }
        startActivity(sendIntent)
    }
    fun irActualizar(os: OperativeSystem){
        val intentActividadIntent = Intent(this,SOActivity::class.java)
        intentActividadIntent.putExtra("Sistema",os)
        startActivity(intentActividadIntent)
    }
    fun irAListarHijos(os:OperativeSystem){
        finish()
        val intentActividadIntent = Intent(
            this,
            ListaAplicacionesActivity::class.java
        )
        intentActividadIntent.putExtra("Sistema", os)
        startActivity(intentActividadIntent)
    }
}

class SOAdaptador(private val listaSistemaOperativos: List<OperativeSystem>,
                  private val contexto: ListaSystemOActivity,
                  private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<SOAdaptador.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTextView: TextView
        var versionTextView: TextView
        var idSOTextView: TextView
        var opciones:Button

        init {
            nombreTextView = view.findViewById(R.id.txt_nombre_so) as TextView
            versionTextView = view.findViewById(R.id.txt_version_so) as TextView
            idSOTextView = view.findViewById(R.id.txt_so_id) as TextView
            opciones = view.findViewById(R.id.btn_opciones) as Button

            val layout = view.findViewById(R.id.relative_layout_so) as RelativeLayout

            layout
                .setOnClickListener {
                    val nombreActual = it.findViewById(R.id.txt_nombre_so) as TextView

                    Log.i("recycler-view",
                        "El nombre actual es: ${nombreActual.text}")

                }



        }
    }
    // Definimos el layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): MyViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_so_item,
                parent,
                false
            )

        return MyViewHolder(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sistema = listaSistemaOperativos[position]

        holder.nombreTextView.setText(sistema.nombre)
        holder.versionTextView.setText(sistema.version)
        holder.idSOTextView.setText(sistema.id.toString())
        holder.opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.idSOTextView)
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.eliminar_so ->{
                        //handle menu1 click
                        mensaje_dialogo(contexto,"Eliminar el SO?",
                            fun (){
                                val id = holder.idSOTextView.text.toString()
                                Log.i("Eliminar SO->",id)

                                val direccion = "http://${DatabaseSO.ip}:1337/OperativeSystema"
                                Log.i("http",direccion)
                                val parametros = listOf("nombre" to id)
                                val url = "http://${DatabaseSO.ip}:1337/OperativeSystema/$id"
                                    .httpDelete(parametros)
                                    .responseString { request, response, result ->
                                        when (result) {
                                            is Result.Failure -> {
                                                val ex = result.getException()
                                                Log.i("http-p", ex.toString())
                                                mensaje(contexto,"error","Datos no validos")

                                            }
                                            is Result.Success -> run {
                                                val data = result.get()
                                                Log.i("http-p", data)
                                                mensaje(contexto,"Aceptado","Datos validos, espere...")
                                                contexto.refrescar()
                                            }
                                        }
                                    }




                            }
                        )



                        true
                    }

                    R.id.editar_so ->{
                        val id = holder.idSOTextView.text.toString()
                        mensaje_dialogo(contexto,"Desea editar el SO?",

                            fun(){
                                val so = DatabaseSO.sistemasOperativos.filter { it.id==id.toInt() }[0]
                                Log.i("Actualizar SO->",so.fechaLanzamiento)
                                val soSerializado = OperativeSystem(
                                    id.toInt(),
                                    nombre = so.nombre,
                                    version = so.version,
                                    fechaLanzamiento = so.fechaLanzamiento,
                                    peso_gigas = so.peso_gigas
                                )
                                contexto.irActualizar(soSerializado)
                            }

                        )

                        //handle menu2 click
                        true
                    }

                    R.id.compartir_so ->{
                        val nombre = holder.nombreTextView.text.toString()
                        contexto.compartir(nombre)
                        //handle menu3 click
                        true
                    }

                    R.id.hijos_so ->{
                        var direccion = ""
                        val id = holder.idSOTextView.text.toString()
                        val so = DatabaseSO.sistemasOperativos.filter { it.id==id.toInt() }[0]
                        Log.i("Listar SO->",so.fechaLanzamiento)
                        val soSerializado = OperativeSystem(
                            id.toInt(),
                            nombre = so.nombre,
                            version = so.version,
                            fechaLanzamiento = so.fechaLanzamiento,
                            peso_gigas = so.peso_gigas
                        )
                        contexto.irAListarHijos(soSerializado)
                        true
                    }


                    else -> false
                }
            }
            //displaying the popup
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return listaSistemaOperativos.size
    }
    fun mensaje(actividad: Activity, tipo: String, contenido:String){

        com.tapadoo.alerter.Alerter.create(actividad)
            .setTitle(tipo)
            .setText(contenido)
            .show()
    }


    fun mensaje_dialogo(actividad: Activity, contenido:String, funcion: () -> Unit){
        val builder = AlertDialog.Builder(actividad)

        builder
            .setMessage(contenido)
            .setPositiveButton(
                "Si",
                DialogInterface.OnClickListener { dialog, which ->
                    funcion()
                }
            )
            .setNegativeButton(
                "No",null
            )


        val dialogo = builder.create()
        dialogo.show()
        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
        dialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
    }
}