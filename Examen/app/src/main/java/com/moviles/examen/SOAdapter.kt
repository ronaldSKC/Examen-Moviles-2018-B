package com.moviles.examen

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.TextView


class SOAdapter(private val conductorList: List<OperativeSystem>) :  RecyclerView.Adapter<SOAdapter.MyViewHolder>(){


    private var position: Int = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        var nombre: TextView
        var fechaLanzamiento: TextView
        var peso_gigas: TextView
        var version: TextView
        var btn_opciones: Button

        lateinit var operativeSystem: OperativeSystem

        init {
            nombre = view.findViewById(R.id.txt_nombre_so) as TextView
            fechaLanzamiento = view.findViewById(R.id.txt_fecha_so) as TextView
            peso_gigas = view.findViewById(R.id.txt_peso_so) as TextView
            version = view.findViewById(R.id.txt_version_so) as TextView
            btn_opciones = view.findViewById(R.id.btn_opciones) as Button
            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(Menu.NONE, R.id.editar_so, Menu.NONE, "Editar")
            menu?.add(Menu.NONE, R.id.eliminar_so, Menu.NONE, "Eliminar")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_so_item, parent, false)

        return MyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val operativeSystem = conductorList[position]
        holder.nombre.text = operativeSystem.nombre
        holder.fechaLanzamiento.text = operativeSystem.fechaLanzamiento
        holder.peso_gigas.text = operativeSystem.peso_gigas
        holder.version.text = operativeSystem.version
        holder.operativeSystem = operativeSystem
        holder.btn_opciones.setOnClickListener{
                v: View ->
            val intent = Intent(v.context, ListaAplicacionesActivity::class.java)
            intent.putExtra("detallesSO", operativeSystem)

            v.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            setPosition(holder.adapterPosition)
            false
        }
    }

    override fun getItemCount(): Int {
        return conductorList.size
    }


}

