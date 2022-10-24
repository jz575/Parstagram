package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.HomeFragment
import com.example.parstagram.fragments.PictureFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        val homeFragment : Fragment = HomeFragment()
        val composeFragment: Fragment = ComposeFragment()
        val profileFragment: Fragment = ProfileFragment()

        val bottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.menu_bottom)

        bottomNavigationView.setOnItemSelectedListener {
            item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_home -> fragment = homeFragment
                R.id.nav_add -> fragment = composeFragment
                R.id.nav_profile -> fragment = profileFragment
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit()
            true
        }
        bottomNavigationView.selectedItemId = R.id.nav_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            var fragment = PictureFragment()
            supportFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit()
        }
        if (item.itemId == R.id.menu_logout) {
            ParseUser.logOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}