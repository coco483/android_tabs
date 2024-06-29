package com.example.viewactivitypractice.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.viewactivitypractice.R
import com.example.viewactivitypractice.databinding.ActivityMainBinding
import com.example.viewactivitypractice.datas.CameraConstants
/*
class CameraPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.camera_page, container, false)
        val binding = ActivityMainBinding.inflate(LayoutInflater)
        if (allPermissionGranted()){
            Log.d("CameraPermission", "We have permission")
            Toast.makeText(requireActivity(), "We have camera permission", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("CameraPermission", "We don't have permission")
            Log.d("CameraPermission", "activity ${requireActivity()}")
            ActivityCompat.requestPermissions(
                requireActivity(), CameraConstants.REQUIRED_PERMISSIONS,
                CameraConstants.REQUEST_CODE_PERMISSIONS
            )
        }
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        if (requestCode == CameraConstants.REQUEST_CODE_PERMISSIONS){
            startCamera()
        } else {
            Toast.makeText(requireActivity(), "user didn't gave us permission", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun allPermissionGranted() =
        CameraConstants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(), it
            ) == PackageManager.PERMISSION_GRANTED
        }

}
*/