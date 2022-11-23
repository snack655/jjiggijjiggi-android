package kr.minjae.develop.jjiggijjiggi.feature.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kr.minjae.develop.jjiggijjiggi.databinding.ActivityMainBinding
import kr.minjae.develop.jjiggijjiggi.feature.main.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        bindViewEvent()
        bindViewModel()
    }

    private fun bindViewModel() = lifecycleScope.launchWhenStarted {
        mainViewModel.ocrState.collect { state ->

            if (state.ocrData != null) {
                binding.btnTest.visibility = View.GONE
                val fieldList = state.ocrData.images[0].fields

                var string = ""
                fieldList.forEach {
                    string += " " + it.inferText
                }

                binding.tvText.text = string
            }

            if (state.error.isNotBlank()) {
                Toast.makeText(this@MainActivity, state.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initActivity() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun bindViewEvent() {
        binding.btnTest.setOnClickListener {
            mainViewModel.getTextInImage()
        }
    }
}