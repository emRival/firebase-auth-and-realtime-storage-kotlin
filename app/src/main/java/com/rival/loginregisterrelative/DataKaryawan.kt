package com.rival.loginregisterrelative

class DataKaryawan(val id: String,val email:String, val namalengkap: String?, val alamat: String?,val jenisKelamin:String?, val tanggalLahir: String?, val pendidikanTerakhir:String?) {

    constructor() : this("","","","","","",""){

    }
    // setter and getter

}