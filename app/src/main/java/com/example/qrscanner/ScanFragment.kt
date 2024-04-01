package com.example.qrscanner

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.lang.Exception


class ScanFragment : Fragment() {
    private lateinit var cameraBtn: MaterialButton
    private lateinit var galleryBtn: MaterialButton
    private lateinit var imageIv: ImageView
    private lateinit var scanBtn: MaterialButton
    private lateinit var resultTv: TextView
    companion object{
        const val TAG="MAIN_TAG"
        //        to handle the result of Camera/Gallery permissions in onRequestPermissionResult
        const val CAMERA_REQUEST_CODE=100
        const val STORAGE_REQUEST_CODE=101
        //        Tag for debugging, print values in log

    }
    //    array of permissions required to pick image from Camera/Gallery
    private lateinit var cameraPermissions:Array<String>
    private lateinit var storagePermissions:Array<String>
    //    uri of the image that we will take from Camera/Gallery
    private var imageUri: Uri?=null

    private var barcodeScannerOptions: BarcodeScannerOptions?=null
    private var barcodeScanner: BarcodeScanner?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_scan, container, false)
        cameraBtn=view.findViewById(R.id.camerabtn)
        galleryBtn=view.findViewById(R.id.gallerybtn)
        imageIv=view.findViewById(R.id.imageIv)
        scanBtn=view.findViewById(R.id.scanBtn)
        resultTv=view.findViewById(R.id.resultTv)

        cameraPermissions=arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)//image from camera
        storagePermissions=arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)//image from gallery


        barcodeScannerOptions=BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        barcodeScanner= BarcodeScanning.getClient(barcodeScannerOptions!!)
        cameraBtn.setOnClickListener {

            if(checkCameraPermissions()){

                pickImageCamera()
            }
            else{

                requestCameraPermission()
            }
        }
        galleryBtn.setOnClickListener {

            if(checkStoragePermission()){

                pickImageGallery()
            }
            else{

                requestStoragePermission()
            }
        }
        scanBtn.setOnClickListener{
            if(imageUri == null){
                showToast("Pick image first")
            }
            else{
                detectResultFromImage()
            }
        }
        // Inflate the layout for this fragment

        return view
    }
    private fun detectResultFromImage(){
        Log.d(TAG,"detectResultFromImage: ")
        try{
            val inputImage= InputImage.fromFilePath(requireContext(),imageUri!!)

            val barcodeResult=barcodeScanner!!.process(inputImage)
                .addOnSuccessListener{barcodes->
                    if (barcodes.isEmpty()) {
                        showToast("No barcodes found in the image")
                    } else {
                        extractBarcodeQrCodeInfo(barcodes)
                    }
                }
                .addOnFailureListener{e->
                    Log.e(TAG,"detectResultFromImage",e)
                    showToast("Failed scanning due to ${e.message}")
                }
        }
        catch(e: Exception){
            Log.e(TAG,"detectResultFromImage: ",e)
            showToast("Failed due to ${e.message}")
        }
    }

    private fun extractBarcodeQrCodeInfo(barcodes: List<Barcode>){

        for(barcode in barcodes){
            val bound=barcode.boundingBox
            val corners=barcode.cornerPoints

            val rawValue=barcode.rawValue
            Log.d(TAG,"extractBarcodeQrCodeInfo: rawValue: $rawValue")

            val valueType=barcode.valueType
            resultTv.text= "$rawValue"
        }
    }
    private fun pickImageGallery(){
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        galleryActivityResultLauncher.launch(intent)
    }
    private val galleryActivityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == Activity.RESULT_OK){
            val data=result.data
            imageUri=data?.data
            Log.d(TAG,"galleryActivityResultLauncher: imageUri:$imageUri")
            imageIv.setImageURI(imageUri)
        }
        else{
//            cancelled
            showToast("Cancelled...!")
        }
    }
    private fun pickImageCamera(){
        val contentValues= ContentValues()

        ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Sample Image")
        }
        ContentValues().apply {
            put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description")
        }

        imageUri=requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)
    }
    private val cameraActivityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == Activity.RESULT_OK){
            val data=result.data
            Log.d(TAG,"cameraActivityResultLauncher: imageUri: $imageUri")

            imageIv.setImageURI(imageUri)
        }
    }

    private fun checkStoragePermission():Boolean{

        val result= (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            storagePermissions,
            STORAGE_REQUEST_CODE
        )
    }
    private fun checkCameraPermissions():Boolean{

        val resultCamera=(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        val resultStorage=(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)

        return resultCamera && resultStorage
    }

    private fun requestCameraPermission(){

        ActivityCompat.requestPermissions(
            requireActivity(),
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE ->{

                if(grantResults.isNotEmpty()){

                    val cameraAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED
                    val storageAccepted=grantResults[1]== PackageManager.PERMISSION_GRANTED

                    if(cameraAccepted && storageAccepted){

                        pickImageCamera()
                    }
                    else{

                        showToast("Camera & Storage permissions are required")
                    }
                }
            }
            STORAGE_REQUEST_CODE ->{

                if(grantResults.isNotEmpty()){

                    val storageAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED

                    if(storageAccepted){

                        pickImageGallery()
                    }
                    else{

                        showToast("Storage permission is required...")
                    }
                }
            }
        }
    }
    private fun showToast(message:String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

}