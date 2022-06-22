package com.rival.loginregisterrelative.ui.dashboard

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rival.loginregisterrelative.databinding.FragmentDashboardBinding
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class DashboardFragment : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var _binding: FragmentDashboardBinding? = null

    lateinit var datePicker: DatePicker

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        // date picker
        tanggalLahir()

        binding.btnSaveDataDiri.setOnClickListener {
            val namaLengkap = binding.edtNamaLengkap.text.toString()
            val alamat = binding.edtAlamat.text.toString()
            val jenisKelamin = binding.jenisKelamin.selectedItem.toString()
            val tanggalLahir = binding.dateFormat.text.toString()
            val pendidikanTerakhir = binding.pendidikanTerakhir.selectedItem.toString()

            sendData(namaLengkap, alamat, jenisKelamin, tanggalLahir, pendidikanTerakhir)
        }


        return root
    }

    private fun sendData(
        namaLengkap: String,
        alamat: String,
        jenisKelamin: String,
        tanggalLahir: String,
        pendidikanTerakhir: String
    ) {
        val user = auth.currentUser
        ref = FirebaseDatabase.getInstance().reference.child("Data_Karyawan")
        val userMap = HashMap<String, Any>()
        val id = user?.uid.toString()
        val email = user?.email.toString()
        userMap["id"] = id
        userMap["namalengkap"] = namaLengkap
        userMap["email"] = email
        userMap["alamat"] = alamat
        userMap["jenisKelamin"] = jenisKelamin
        userMap["tanggalLahir"] = tanggalLahir
        userMap["pendidikanTerakhir"] = pendidikanTerakhir


        ref.child(id).setValue(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                //clear string
                Toast.makeText(requireActivity(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tanggalLahir() {
        val textView: TextView = binding.dateFormat
        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)

            }

        textView.setOnClickListener {
            DatePickerDialog(
                requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}