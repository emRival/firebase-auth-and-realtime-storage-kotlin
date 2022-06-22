package com.rival.loginregisterrelative.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import com.rival.loginregisterrelative.DataKaryawan
import com.rival.loginregisterrelative.R

import com.rival.loginregisterrelative.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var list: MutableList<DataKaryawan>

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ref = FirebaseDatabase.getInstance().getReference("Data_Karyawan")
        list = mutableListOf()



        showData()

        return root
    }

    private fun showData() {
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {
                    showY(snapshot)
                    list.clear()
                    for (i in snapshot.children) {
                        val user = i.getValue(DataKaryawan::class.java)
                        list.add(user!!)

                    }
                    binding.lvData.adapter =
                        HomeAdapter(requireActivity(), R.layout.item_data, list)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showY(snapshot: DataSnapshot) {
        for (i in snapshot.children) {
            val user = i.getValue(DataKaryawan::class.java)
            binding.tvJenisKelamin.text = user?.jenisKelamin


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}