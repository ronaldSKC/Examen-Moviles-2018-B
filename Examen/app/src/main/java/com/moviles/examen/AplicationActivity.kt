package com.moviles.examen

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_aplication.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AplicationActivity : AppCompatActivity() {

    var pathActualFoto = ""
    var respuestaBarcode = ArrayList<String>()
    var id_so=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplication)
        btn_tomar_foto.setOnClickListener {
            tomarFoto()
        }
        val aplicacion = intent.getParcelableExtra<Aplicaciones?>("aplicacion")
        val sistemaOperativo = intent.getParcelableExtra<OperativeSystem?>("sistema")


        id_so=intent.getIntExtra("id_so",0)
        var esnuevo = true

        if (aplicacion != null) {
            txt_id_app.setText(aplicacion.id.toString())
            txt_nombre_app.setText(aplicacion.nombre)
            txt_version_app.setText(aplicacion.version)
            txt_fecha_app.setText(aplicacion.fechaLanzamiento)
            txt_peso_app.setText(aplicacion.peso_gigas)
            txt_costo_app.setText(aplicacion.costo)
            txt_url_download.setText(aplicacion.url_descargar)
            txt_codigo.setText(aplicacion.codigo_barras)
            id_so=aplicacion.sistemaOperativo
            esnuevo = false
        }



        btn_guardar_app.setOnClickListener {
            if(esnuevo){
                crearActualizarAPP(true)
            }else{
                crearActualizarAPP(false)
            }
        }

        btn_cancelar_app.setOnClickListener {
            val redire = "http://http://localhost:1337/Aplication/$id_so"
            DatabaseAplication.getList(redire)
            this.irlistarApp()
        }


    }
    fun irlistarApp(){
        finish()
        val intent = Intent(
            this,
            ListaAplicacionesActivity::class.java
        )
        intent.putExtra("id_so",id_so)
        startActivity(intent)
    }

    fun tomarFoto(){
        val archivoImagen = crearArchivo("JPEG_", Environment.DIRECTORY_PICTURES, ".jpg")
        pathActualFoto = archivoImagen.absolutePath
        enviarIntentFoto(archivoImagen)
    }

    private fun crearArchivo(prefijo: String, directorio: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = prefijo + timeStamp + "_"
        val storageDir = getExternalFilesDir(directorio)

        // Crear archivo temporal
        return File.createTempFile(
            imageFileName, /* prefix */
            extension, /* suffix */
            storageDir      /* directory */
        )
    }

    private fun enviarIntentFoto(archivo: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.moviles.examen",
            archivo)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOMAR_FOTO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    mostrarFotoImagenView()
                    obtenerInfoCodigoBarras(obtenerBitmapDeArchivo(pathActualFoto))
                }
            }

        }
    }

    fun mostrarFotoImagenView(){
        img_barras.setImageBitmap(obtenerBitmapDeArchivo(pathActualFoto))
    }

    fun obtenerBitmapDeArchivo(path: String): Bitmap {
        val archivoImagen = File(path)
        return BitmapFactory.decodeFile(archivoImagen.getAbsolutePath())
    }

    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
            .visionBarcodeDetector
        Log.i("info", "------- Entro a detectar")
        val result = detector.detectInImage(image)
            .addOnSuccessListener { barCodes ->
                Log.i("info", "------- tamano del barcode ${barCodes.size}")
                respuestaBarcode.add("Ejemplo")
                for (barcode in barCodes) {
                    val bounds = barcode.getBoundingBox()
                    val corners = barcode.getCornerPoints()

                    val rawValue = barcode.getRawValue()

                    Log.i("ml", "------- $bounds")
                    Log.i("ml", "------- $corners")
                    Log.i("ml", "------- $rawValue")

                    respuestaBarcode.add(rawValue.toString())
                }
                txt_codigo.setText(respuestaBarcode[1])
            }
            .addOnFailureListener {
                Log.i("info", "------- No reconocio nada")
            }
    }

    fun crearActualizarAPP(es_nuevo:Boolean){

        val id = txt_id_app.text.toString()
        val nombre = txt_nombre_app.text.toString()
        val version = txt_version_app.text.toString()
        val fecha = txt_fecha_app.text.toString()
        val peso = txt_peso_app.text.toString()
        val costo = txt_costo_app.text.toString()
        val url_descargar = txt_url_download.text.toString()
        val codigo_barras = txt_codigo.text.toString()

        val app:Aplicaciones
        if(es_nuevo){
            app = Aplicaciones(id=null,nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso,costo = costo,url_descargar = url_descargar,codigo_barras = codigo_barras,sistemaOperativo = id_so)
        }else{
            app = Aplicaciones(id=id.toInt(),nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso,costo = costo,url_descargar = url_descargar,codigo_barras = codigo_barras,sistemaOperativo = id_so)
        }

        //Crear objeto
        val parametros = listOf(
            "nombre" to app.nombre,
            "version" to app.version,
            "fechaLanzamiento" to app.fechaLanzamiento,
            "peso_gigas" to app.peso_gigas,
            "costo" to app.costo,
            "url_descargar" to app.url_descargar,
            "codigo_barras" to app.codigo_barras,
            "sistemaOperativo" to app.sistemaOperativo
        )
        Log.i("htpp",parametros.toString())
        var direccion = ""
        if(es_nuevo){
            direccion = "http://localhost:1337/Aplication"
            val url = direccion
                .httpPost(parametros)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.i("http-p", ex.toString())
                            mensaje(this,"error","Datos no validos")

                        }
                        is Result.Success -> run {
                            val data = result.get()
                            Log.i("http-p", data)
                            mensaje(this,"Aceptado","Datos validos, espere...")
                            val redire = "http://http://localhost:1337/Aplication/$id_so"
                            DatabaseAplication.getList(redire)
                            this.irlistarApp()
                        }
                    }
                }
        }else{
            direccion = "//http://localhost:1337/Aplication/$id_so"
            val url = direccion
                .httpPut(parametros)
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.i("http-p", ex.toString())
                            mensaje(this,"error","Datos no validos")

                        }
                        is Result.Success -> run {
                            val data = result.get()
                            Log.i("http-p", data)
                            mensaje(this,"Aceptado","Datos validos, espere...")
                            val redire = "//http://localhost:1337/Aplication/$id_so"
                            DatabaseAplication.getList(redire)
                            this.irlistarApp()
                        }
                    }
                }
        }
        Log.i("http",direccion)

    }



    companion object {
        val TOMAR_FOTO_REQUEST = 1;
    }
}
fun mensaje(actividad:Activity,tipo: String,contenido:String){

    com.tapadoo.alerter.Alerter.create(actividad)
        .setTitle(tipo)
        .setText(contenido)
        .show()
}


fun mensaje_dialogo(actividad:Activity,contenido:String,funcion: () -> Unit){
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