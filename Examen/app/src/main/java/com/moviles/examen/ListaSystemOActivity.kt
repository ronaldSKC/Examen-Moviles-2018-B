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
<<<<<<< HEAD
=======
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
>>>>>>> parent of c066d74... back en la web

        registerForContextMenu(rv_so)
    }
<<<<<<< HEAD
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
=======

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
>>>>>>> parent of c066d74... back en la web
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


