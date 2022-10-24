package com.example.parstagram.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loading = view.findViewById<ProgressBar>(R.id.pbLoading)
        ivPhoto = view.findViewById(R.id.ivPhoto)
        val user = ParseUser.getCurrentUser()
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            if (photoFile != null) {
                loading.setVisibility(ProgressBar.VISIBLE)
                if(user != null) setPfp(user, photoFile!!)
                else Log.e(TAG, "User is null")
                //creates a delay so loading bar is visible for at least 1 second
                val handler = Handler()
                handler.postDelayed(object: Runnable {
                    override fun run() {
                        loading.setVisibility(ProgressBar.INVISIBLE)
                    }
                }, 1000)
            } else {
                Toast.makeText(requireContext(), "Please take a photo", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.btnCamera).setOnClickListener{
            onLaunchCamera()
        }
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