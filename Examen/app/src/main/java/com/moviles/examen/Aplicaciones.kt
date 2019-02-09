package com.moviles.examen

import android.os.Parcel
import android.os.Parcelable

class Aplicaciones( var id:Int?,
                    var nombre: String,
                    var version: String,
                    var fechaLanzamiento: String,
                    var peso_gigas: String,
                    var costo:String,
                    var url_descargar:String,
                    var codigo_barras:String,
                    var sistemaOperativo: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeString(version)
        parcel.writeString(fechaLanzamiento)
        parcel.writeString(peso_gigas)
        parcel.writeString(costo)
        parcel.writeString(url_descargar)
        parcel.writeString(codigo_barras)
        parcel.writeInt(sistemaOperativo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Aplicaciones> {
        override fun createFromParcel(parcel: Parcel): Aplicaciones {
            return Aplicaciones(parcel)
        }

        override fun newArray(size: Int): Array<Aplicaciones?> {
            return arrayOfNulls(size)
        }
    }
}

