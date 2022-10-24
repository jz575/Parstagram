package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.GetCallback
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File


private val TAG = "PictureFragment"
class PictureFragment : ComposeFragment() {
    val PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1038
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivPhoto = view.findViewById(R.id.ivPhoto)
        val user = ParseUser.getCurrentUser()
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            if (photoFile != null) {
                setPfp(user, photoFile!!)
            } else {
                Toast.makeText(requireContext(), "Please take a photo", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.btnCamera).setOnClickListener{
            onLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnPick).setOnClickListener{
            var intent: Intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/*");
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true)
            intent.putExtra("outputX", 256)
            intent.putExtra("outputY", 256)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("return-data", true)
            startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == AppCompatActivity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                ivPhoto.setImageBitmap(takenImage)
            } else {
                Toast.makeText(requireContext(), "Problem taking picture", Toast.LENGTH_SHORT).show()
            }
        }
        if(requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data?.data
                if (uri != null){
                    val selectedImage = uriToBitmap(uri)
                    ivPhoto.setImageBitmap(selectedImage)
                    photoFile = uriToImageFile(uri)
                    setPfp(ParseUser.getCurrentUser(), photoFile!!)
                }
            } else {
                Toast.makeText(requireContext(), "Problem picking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }
    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
    }
    fun setPfp(user: ParseUser, picture: File){
        user.put("profilePicture", ParseFile(picture))
        user.saveInBackground { e ->
            if (e != null) {
                Log.e(TAG, e.printStackTrace().toString())
                Toast.makeText(requireContext(),"Error saving profile picture", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully saved profile picture")
                Toast.makeText(requireContext(),"Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}