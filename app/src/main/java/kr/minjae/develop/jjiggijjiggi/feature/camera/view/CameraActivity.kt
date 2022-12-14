package kr.minjae.develop.jjiggijjiggi.feature.camera.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kr.minjae.develop.jjiggijjiggi.R
import kr.minjae.develop.jjiggijjiggi.databinding.ActivityCameraBinding
import kr.minjae.develop.jjiggijjiggi.feature.camera.viewmodel.CameraViewModel
import kr.minjae.develop.jjiggijjiggi.feature.main.view.MainActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var actionResult: ActivityResultLauncher<Intent>

    private var selectedUri: Uri? = null
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()

        actionResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Glide.with(binding.ivImage.context)
                    .load(selectedUri)
                    .error(R.drawable.dummy)
                    .into(binding.ivImage)

                binding.layoutSolve.visibility = View.VISIBLE
                binding.viewBackground.visibility = View.VISIBLE
                binding.layoutAppDesc.visibility = View.GONE
            }
        }

        onClickCapture()
        onClickSolve()
        bindUploadPhotoState()
    }

    private fun onClickCapture() = binding.btnCapture.setOnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI = FileProvider.getUriForFile(this@CameraActivity, FILE_AUTHORITY, createImageFile())

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        actionResult.launch(takePictureIntent)

        selectedUri = Uri.fromFile(createImageFile())
    }

    private fun onClickSolve() = binding.layoutSolve.setOnClickListener {
        selectedUri?.let { cameraViewModel.uploadPhoto(storage, it) } ?: return@setOnClickListener
    }

    private fun bindUploadPhotoState() = lifecycleScope.launchWhenStarted {
        cameraViewModel.uploadPhotoState.collect { state ->

            if (state.url.isNotBlank()) {
                binding.progressCircular.visibility = View.GONE
                binding.ivImage.visibility = View.VISIBLE
                binding.btnSolve.visibility = View.VISIBLE
                binding.btnCapture.visibility = View.VISIBLE
                binding.lottieCamera.visibility = View.GONE
                binding.viewBackground.visibility = View.VISIBLE
                binding.ivCamera.visibility = View.VISIBLE
                binding.layoutSolve.visibility = View.VISIBLE

                Toast.makeText(this@CameraActivity, "????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this@CameraActivity, MainActivity::class.java)
                intent.putExtra(INTENT_IMAGE_NAME, state.url)
                startActivity(intent)

            }

            if (state.isLoading) {
                binding.progressCircular.visibility = View.VISIBLE
                binding.ivImage.visibility = View.GONE
                binding.viewBackground.visibility = View.GONE
                binding.layoutSolve.visibility = View.GONE
                binding.btnCapture.visibility = View.GONE
                binding.lottieCamera.visibility = View.GONE
                binding.layoutAppDesc.visibility = View.GONE
                binding.ivCamera.visibility = View.GONE
            }

            if (state.error.isNotBlank()) {
                Toast.makeText(this@CameraActivity, state.error, Toast.LENGTH_SHORT).show()
                binding.progressCircular.visibility = View.GONE
                binding.ivImage.visibility = View.VISIBLE
                binding.viewBackground.visibility = View.VISIBLE
                binding.btnSolve.visibility = View.VISIBLE
                binding.btnCapture.visibility = View.VISIBLE
            }
        }

    }

    private fun initActivity() {
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "PHOTO_${timeStamp}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, imageFileName)
    }

    companion object {
        private const val FILE_AUTHORITY = "kr.minjae.develop.jjiggijjiggi.fileprovider"
        const val INTENT_IMAGE_NAME = "url"
    }

}