package com.moviles.examen

import android.os.Parcel
import android.os.Parcelable

class OperativeSystem(val id: Int?,
                      val nombre: String,
                      val fechaLanzamiento:String,
                      val peso_gigas:String,
                      val version:String):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeString(fechaLanzamiento)
        parcel.writeString(peso_gigas)
        parcel.writeString(version)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OperativeSystem> {
        override fun createFromParcel(parcel: Parcel): OperativeSystem {
            return OperativeSystem(parcel)
        }

        override fun newArray(size: Int): Array<OperativeSystem?> {
            return arrayOfNulls(size)
        }
    }
}