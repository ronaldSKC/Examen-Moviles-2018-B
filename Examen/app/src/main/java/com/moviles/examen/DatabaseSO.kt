package com.moviles.examen

import android.os.StrictMode
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

class DatabaseSO{
    companion object {
        val ip = "172.29.61.125"
        val sistemasOperativos = ArrayList<OperativeSystem>()
        fun insertarSO(op:OperativeSystem){
            "http://${ip}:1337/OperativeSystema"
                .httpPost(listOf(
                    "nombre" to op.nombre,
                    "fechaLanzamiento" to op.fechaLanzamiento,
                    "peso_gigas" to op.peso_gigas,
                    "version" to op.version
                    ))
                .responseString { request, _, result ->
                    Log.d("http-ejemplo", request.toString())
                }
        }
        fun eliminarSO (id: Int){
            "${ip}/OperativeSystema/${id}".httpDelete()
                .responseString { request, response, result ->
                    Log.d("http-ejemplo", request.toString())
        }
        fun actualizarSO(so: OperativeSystem){
            "${ip}/OperativeSystema/${so.id}".httpPut(listOf(
                    "nombre" to op.nombre,
                    "fechaLanzamiento" to op.fechaLanzamiento,
                    "peso_gigas" to op.peso_gigas,
                    "version" to op.version
                    ))
                .responseString { request, _, result ->
                    Log.d("http-ejemplo", request.toString())
                }
        }
        fun getList():ArrayList<OperativeSystem>{
            val so: ArrayList<OperativeSystem> = ArrayList()
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val (request, response, result) ="${ip}/OperativeSystema".httpGet().responseString()
            
            val jsonStringSo = result.get()
            val parser = Parser()
            val stringBuilder = StringBuilder(jsonStringSo)
            val array = parser.parse(stringBuilder) as JsonArray<JsonObject>

            array.forEach {
                val id = it["id"] as Int
                val nombreA = it["nombre"] as String
                val fechaP = it["fechaLanzamiento"] as String
                val pesoGb = it["peso_gigas"]as String
                val versionS = it["version"]as String

                val operativeS = OperativeSystem(id,nombreA,fechaP,pesoGb,versionS)
                so.add(operativeS)
            }
            return so
        }
        fun buscarSO(nombre:String): ArrayList<OperativeSystem> {
            val so: ArrayList<OperativeSystem> = ArrayList()
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val (request, response, result) = "${ip}/OperativeSystema?nombres=${nombre}".httpGet().responseString()
            val jsonStringEstudiante = result.get()
c
            val jsonStringSo = result.get()
            val parser = Parser()
            val stringBuilder = StringBuilder(jsonStringSo)
            val array = parser.parse(stringBuilder) as JsonArray<JsonObject>

            array.forEach {
                val id = it["id"] as Int
                val nombreA = it["nombre"] as String
                val fechaP = it["fechaLanzamiento"] as String
                val pesoGb = it["peso_gigas"]as String
                val versionS = it["version"]as String

                val operativeS = OperativeSystem(id,nombreA,fechaP,pesoGb,versionS)
                so.add(operativeS)
            }
            return so
        }

    }
    }

}