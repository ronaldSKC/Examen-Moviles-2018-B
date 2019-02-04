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
        val ip = "https://moviles-choco179520.c9users.io:8080"
        val sistemasOperativos = ArrayList<OperativeSystem>()
        fun insertarSO(op:OperativeSystem){
            "${ip}/OperativeSystema"
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
        fun getList(url:String):ArrayList<OperativeSystem>{
            val so: ArrayList<OperativeSystem> = ArrayList()
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val (request, response, result) =url.httpGet().responseString()
            Log.d("http-get",result.get())
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