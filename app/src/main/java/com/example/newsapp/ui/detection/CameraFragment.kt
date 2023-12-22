package com.example.newsapp.ui.detection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.R
import com.example.newsapp.data.model.detection.DetectionResponse
import com.example.newsapp.data.remote.detection.detectionApiService
import com.example.newsapp.databinding.FragmentCameraBinding
import com.example.newsapp.ui.detection.detail.DetailDetectionActivity
import com.example.newsapp.utils.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!


    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    //private val cameraViewModel : DetectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){


            cameraExecutor = Executors.newSingleThreadExecutor()

            actionOpenCamera.setOnClickListener {
                checkPermissionCamera()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUIStatusBar()
    }

    private fun takePhoto(){
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("Image", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return  FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", tmpFile)
    }

    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    val file = FileUtil.uriToFile(uri = uri,requireContext(),createFile())

                    if (file != null)
                        detectionObject(file)
                }
            }
        }

    private fun createFile(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdirs() } }

        val outputDirectory = if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir

        return File(
            outputDirectory,
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )
    }

    fun detectionObject(file:File){
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )


        detectionApiService.detectionObject(filePart).enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(call: Call<DetectionResponse>, response: Response<DetectionResponse>) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()

                    if (uploadResponse != null) {
                        Log.d("uploadResponse", uploadResponse.prediction)

                        val intent = Intent(requireContext(), DetailDetectionActivity::class.java)
                        intent.putExtra(DetailDetectionActivity.ARGS_TITLE,uploadResponse.prediction)
                        startActivity(intent)
                    }
                    // Handle the success response
                } else {
                    Toast.makeText(requireContext(),response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }


    //PERMISSION
    private fun checkPermissionCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.CAMERA
            ), CAMERA_PERMISSION_CODE)
        } else {
            takePhoto()
        }
    }

    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (allPermissionsGranted()){
                takePhoto()
            }
            else{
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance to avoid memory leaks
        _binding = null
        cameraExecutor.shutdown()
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUIStatusBar(){
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

    companion object{
        const val RESULT_OK = 200
        const val CAMERA_PERMISSION_CODE = 20
    }
}