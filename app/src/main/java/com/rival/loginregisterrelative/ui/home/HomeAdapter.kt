package com.rival.loginregisterrelative.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.rival.loginregisterrelative.DataKaryawan
import com.rival.loginregisterrelative.R


class HomeAdapter(val mCtx: Context, val layoutResId: Int, val list: List<DataKaryawan>) : ArrayAdapter<DataKaryawan>(mCtx, layoutResId, list)  {

    lateinit var auth: FirebaseAuth
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val item = list[position]
        auth = FirebaseAuth.getInstance()

        view.findViewById<TextView>(R.id.tv_nama_lengkap).text = item.namalengkap
        view.findViewById<TextView>(R.id.tv_email).text = item.email
        view.findViewById<TextView>(R.id.tv_alamat).text = item.alamat
        view.findViewById<TextView>(R.id.tv_jenis_kelamin).text = item.jenisKelamin
        return view
    }
}