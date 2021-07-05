package com.savr.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.savr.datastore.databinding.ActivitySecondBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private var storeManager = DataStoreManager
    private var isDarkMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserver()
        setupView()

    }

    private fun setupView() {
        binding.btnBack.setOnClickListener {
            lifecycleScope.launch {
//                storeManager.saveValue("isLogin", false, dataStore)
                storeManager.removeData(dataStore)
            }
            finish()
        }

        binding.imageButton.setOnClickListener {
            setupUi()
        }
    }

    private fun setupUi() {
        lifecycleScope.launch {
            when(isDarkMode) {
                true -> storeManager.setUiMode(dataStore, UiMode.LIGHT)
                false -> storeManager.setUiMode(dataStore, UiMode.DARK)
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch{
            val value = storeManager.getStringValue(dataStore, "name")
            binding.textView.text = "Welcome to second activity $value"
        }

        storeManager.uiModeFlow(dataStore).asLiveData().observe(this,{ uiMode ->
            uiMode.let {
                when(uiMode) {
                    UiMode.DARK -> onDarkMode()
                    UiMode.LIGHT -> onLightMode()
                }
            }
        })
    }

    private fun onLightMode() {
        isDarkMode = false
        binding.layoutMain.setBackgroundColor(
            ContextCompat.getColor(this, android.R.color.white)
        )
        binding.imageButton.setImageResource(R.drawable.ic_moon)
    }

    private fun onDarkMode() {
        isDarkMode = true
        binding.layoutMain.setBackgroundColor(
            ContextCompat.getColor(this, android.R.color.black)
        )
        binding.textView.setTextColor(
            ContextCompat.getColor(this, android.R.color.white)
        )
        binding.imageButton.setImageResource(R.drawable.ic_sun)
    }
}