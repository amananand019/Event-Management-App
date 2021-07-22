package com.devil.premises.unicorn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.fragment_add_post.*

private const val PERMISSION_ALL = 1
private const val TAG = "AddPostFragment"

class AddPostFragment: Fragment(R.layout.fragment_add_post) {
    lateinit var postUri: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postUri = "No image"
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA)

        addPostImage.setOnClickListener {
            if(!hasPermissions(requireContext(), PERMISSIONS)){
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)
            }else{
                ImagePicker.with(this)
                    .crop(4F,5F)	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
        }

        postImage.setOnClickListener {
            ImagePicker.with(this)
                .crop(4F,5F)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        postUploadBtn.setOnClickListener {
            if(postTitle.text.isNotEmpty() && postDesc.text.isNotEmpty()){
                val post = mutableMapOf<String, String>()
                post["title"] = postTitle.text.toString()
                post["desc"] = postDesc.text.toString()
                post["image"] = postUri.toString()
                post["owner"] = FirebaseAuth.getInstance().currentUser!!.uid

                val postRef = FirebaseDatabase.getInstance().getReference("posts")
                postRef.push().setValue(post)
                    .addOnCompleteListener {
                        Log.i("Post Child Added", "onViewCreated: $post")
                    }
                Toast.makeText(context, "Post Uploaded", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Please, Enter title and description of the post", Toast.LENGTH_LONG).show()
            }

            var chipNavigationBar = activity?.findViewById<ChipNavigationBar>(R.id.adminChipBottomNavMenu)
            chipNavigationBar?.setItemSelected(R.id.bottom_nav_home, true)

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.adminHomeFragmentContainer, HomeFragment())
            fragmentTransaction.commit()

        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean{
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                upload(uri)
                addPostImage.visibility = View.GONE
                postImage.visibility = View.VISIBLE
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun upload(uri: Uri){
        Toast.makeText(context, "Wait for Image to Load", Toast.LENGTH_LONG).show()
        val riversRef = FirebaseStorage.getInstance().reference.child("Temp/" + System.currentTimeMillis() + ".png")
        // Register observers to listen for when the download is done or if it fails
        riversRef.putFile(uri).addOnFailureListener {
            Log.i(TAG, "upload: " + it.message)
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            riversRef.downloadUrl.addOnSuccessListener {
                postUri = it.toString()
                Glide.with(this).load(it).into(postImage)
            }
        }
    }
}