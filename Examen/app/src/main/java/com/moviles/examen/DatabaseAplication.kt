package com.moviles.examen

import android.os.StrictMode
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

class DatabaseAplication{
    companion object {
        val aplicacion = ArrayList<Aplicaciones>()
        fun insertarSO(op:Aplicaciones){
            "http://${DatabaseSO.ip}:1337/Aplication"
                .httpPost(listOf(
                    "nombre" to op.nombre,
                    "fechaLanzamiento" to op.fechaLanzamiento,
                    "peso_gigas" to op.peso_gigas,
                    "version" to op.version,
                    "costo" to op.costo,
                    "url_descargar" to op.url_descargar,
                    "codigo_barras" to op.codigo_barras,
                    "sistemaOperativo" to op.sistemaOperativo
                ))
                .responseString { request, _, result ->
                    Log.d("http-ejemplo", request.toString())
                }
        }
        fun getList(url:String):ArrayList<Aplicaciones>{
            val so: ArrayList<Aplicaciones> = ArrayList()
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val (request, response, result) ="http://${DatabaseSO.ip}:1337/Aplication".httpGet().responseString()
            val jsonStringSo = result.get()
            val parser = Parser()
            val stringBuilder = StringBuilder(jsonStringSo)
            val array = parser.parse(stringBuilder) as JsonArray<JsonObject>

            array.forEach {
                val id = it["id"] as Int
                val nombreSO = it["nombre"] as String
                val fechaP = it["fechaLanzamiento"] as String
                val pesoGb = it["peso_gigas"]as String
                val versionS = it["version"]as String
                val costo = it["costo"]as String
                val url_descargar = it["url_descargar"]as String
                val codigo_barras = it["codigo_barras"]as String
                val soperative = it["sistemaOperativo"]as Int
                val operativeS = Aplicaciones(id,nombreSO,fechaP,pesoGb,versionS,costo,url_descargar,codigo_barras,soperative)
                so.add(operativeS)
            }
            return so
        }
    }
}