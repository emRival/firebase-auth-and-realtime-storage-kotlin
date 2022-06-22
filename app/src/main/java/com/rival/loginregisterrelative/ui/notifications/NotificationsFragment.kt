package com.rival.loginregisterrelative.ui.notifications

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.rival.loginregisterrelative.LoginActivity
import com.rival.loginregisterrelative.databinding.FragmentNotificationsBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class NotificationsFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var imgUri: Uri

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.imgProfile.setOnClickListener {
            goToCamera()
        }

        binding.btnSave.setOnClickListener {
            saveProfileName()
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            100
        )

        if (user != null) {
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(binding.imgProfile)
            } else {
                Picasso.get().load("https://bit.ly/3wc3Lcn").into(binding.imgProfile)
            }

            binding.tvNamaUser.text = user.displayName
            binding.tvEmailUser.text = user.email
        }

        binding.tvLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }

        return root
    }

    private fun saveProfileName() {
        val user = auth.currentUser

        val namaUser = binding.edtNamaPengguna.text.toString()

        UserProfileChangeRequest.Builder()
            .setDisplayName(namaUser)
            .build().also {
                user?.updateProfile(it)?.addOnCompleteListener { Task ->
                    if (Task.isSuccessful) {
                        Toast.makeText(
                            requireActivity(),
                            "Nama User Berhasil Di Setting!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            Task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun goToCamera() {
        // pick 2 items from the list of items
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        AlertDialog.Builder(requireActivity())
            .setTitle("Add Photo!")
            .setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePictureIntent, 0)
                    }
                    options[item] == "Choose From Gallery" -> {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, 1)

                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            .show()
        // open galeery and take a picture alert dialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            sendToFirebase(bitmap)

        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
            sendToFirebase(bitmap)

        }
    }

    private fun sendToFirebase(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref =
            FirebaseStorage.getInstance().getReference("profile_picture/${auth.currentUser?.email}")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener { Task ->
                        Task.result?.let { Uri ->
                            imgUri = Uri
                            binding.imgProfile.setImageURI(imgUri)

                            saveprofile()
                        }

                    }
                }
            }

    }

    private fun saveprofile() {
        val user = auth.currentUser

        val image = when {
            ::imgUri.isInitialized -> imgUri
            user?.photoUrl == null -> Uri.parse("https://bit.ly/3wc3Lcn")
            else -> user.photoUrl
        }

        UserProfileChangeRequest.Builder()
            .setPhotoUri(image)
            .build().also {
                user?.updateProfile(it)?.addOnCompleteListener { Task ->
                    if (Task.isSuccessful) {
                        Toast.makeText(
                            requireActivity(),
                            "Gambar Berhasil Di Upload !",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            Task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}