package com.example.j_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.j_commerce.R
import com.example.j_commerce.databinding.ActivityShopingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityShopingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}