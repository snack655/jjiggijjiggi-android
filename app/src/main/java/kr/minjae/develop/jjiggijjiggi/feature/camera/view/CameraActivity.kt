package kr.minjae.develop.jjiggijjiggi.feature.camera.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
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
                Glide.with(binding.ivIamge.context)
                    .load(selectedUri)
                    .error(R.drawable.dummy)
                    .into(binding.ivIamge)
            }
        }

        bindingViewEvent()

    }

    private fun bindingViewEvent() = with(binding) {
        btnCapture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoURI = FileProvider.getUriForFile(this@CameraActivity, "kr.minjae.develop.jjiggijjiggi.fileprovider", createImageFile())

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            actionResult.launch(takePictureIntent)

            selectedUri = Uri.fromFile(createImageFile())
        }

        btnCapture.setOnClickListener {
            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = {
                        Toast.makeText(this@CameraActivity, "해석을 시작합니다.", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@CameraActivity, MainActivity::class.java)
                        startActivity(intent)
                    },
                    errorHandler = {
                        Toast.makeText(this@CameraActivity, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun uploadPhoto(uri: Uri, successHandler: () -> Unit, errorHandler: () -> Unit) {

    }

    private fun initActivity() {
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PHOTO_${timeStamp}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, imageFileName)
    }

}