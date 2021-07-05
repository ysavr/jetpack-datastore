package com.savr.datastore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.savr.datastore.databinding.ActivityMainBinding
import com.savr.datastore.databinding.ContentMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contentMainBinding: ContentMainBinding
    private var storeManager = DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        contentMainBinding = binding.contentMain
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupObserver()
        contentMainBinding.btnLogin.setOnClickListener {
            CoroutineScope(IO).launch {
                val name = contentMainBinding.etUsername.text.toString()
                storeManager.saveValue("isLogin", true, dataStore)
                storeManager.saveValue("name", name, dataStore)
                withContext(Main) {
                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                }
            }
        }

    }

    private fun setupObserver() {
//        lifecycleScope.launch {
//            val value = DataStoreManager.getBooleanValue(this@MainActivity, "isLogin", false)
//            if (value) {
//                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
//            }
//        }
//        lifecycleScope.launch {
//            val result = storeManager.getBoolean(dataStore, "isLogin").first()
//            if (result) {
//                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
//            }
//        }

        storeManager.getBoolean(dataStore, "isLogin").asLiveData().observe(this, {
            if (it) {
                startActivity(Intent(this, SecondActivity::class.java))
            }
        })

    }

}