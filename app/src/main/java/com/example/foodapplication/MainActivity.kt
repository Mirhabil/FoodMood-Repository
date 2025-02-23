package com.example.foodapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.foodapplication.Fragments.AiFragment
import com.example.foodapplication.Fragments.CategoriesSelectedFragment
import com.example.foodapplication.Fragments.MapsFragment
import com.example.foodapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navigationBar.itemIconTintList = null

        binding.navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mapsFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, MapsFragment()).commit()
                    true
                }

                R.id.categoriesSelectedFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, CategoriesSelectedFragment()).commit()
                    true

                }
                R.id.AiFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, AiFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}