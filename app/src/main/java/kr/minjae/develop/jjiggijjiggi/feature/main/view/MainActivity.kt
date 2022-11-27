package kr.minjae.develop.jjiggijjiggi.feature.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_camera.iv_image
import kr.minjae.develop.jjiggijjiggi.R
import kr.minjae.develop.jjiggijjiggi.databinding.ActivityMainBinding
import kr.minjae.develop.jjiggijjiggi.feature.camera.view.CameraActivity
import kr.minjae.develop.jjiggijjiggi.feature.main.adapter.SolveAdapter
import kr.minjae.develop.jjiggijjiggi.feature.main.data.SolveData
import kr.minjae.develop.jjiggijjiggi.feature.main.viewmodel.MainViewModel
import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var url: String
    private lateinit var solveAdapter: SolveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        bindViewModel()
        bindViewEvent()
        initSolveAdapter()

        url = intent.getStringExtra(CameraActivity.INTENT_IMAGE_NAME) ?: return
        mainViewModel.getTextInImage(url)
    }

    private fun bindViewModel() = lifecycleScope.launchWhenStarted {
        mainViewModel.ocrState.collect { state ->
            if (state.ocrData != null) {
                setOcrData(state.ocrData)
                binding.scrollView.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                binding.layoutAppBar.visibility = View.VISIBLE
            }

            if (state.isLoading) {
                binding.progressCircular.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
                binding.layoutAppBar.visibility = View.GONE
            }

            if (state.error.isNotBlank()) {
                Toast.makeText(this@MainActivity, state.error, Toast.LENGTH_SHORT).show()
                binding.scrollView.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                binding.layoutAppBar.visibility = View.VISIBLE
            }
        }
    }

    private fun initActivity() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setOcrData(ocrData: OcrResponse) {
        val fieldList = ocrData.images[0].fields
        var str = ""
        fieldList.forEach {
            str += " " + it.inferText
        }
        binding.tvText.text = str

        Glide.with(binding.ivImage.context)
            .load(url)
            .error(R.drawable.dummy)
            .into(iv_image)

        setSolveData(str)
    }

    private fun setSolveData(originalText: String) {
        solveAdapter.submitList(solveContents)

        /*
        TODO 아래 solveContents 책의 내용으로 수정 후에 주석을 해제합니다.
        val solveNewList: MutableList<SolveData> = mutableListOf()
        solveContents.forEach { solveData ->
            if (originalText.contains(solveData.title)) {
                solveNewList.add(solveData)
            }
        }
        solveAdapter.submitList(solveNewList)
         */
    }

    private fun initSolveAdapter() {
        solveAdapter = SolveAdapter()
        binding.rvSolveContents.adapter = solveAdapter
    }

    private fun bindViewEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private val solveContents: List<SolveData> = listOf(
        SolveData("경복", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
        SolveData("경복궁", "이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ 이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ이것은 더미데이터 입니다. 아 진짜 너무 많아ㅏㅏㅏㅏㅏ"),
    )
}