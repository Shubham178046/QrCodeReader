package com.example.qrcodereader

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.SparseArray
import android.view.ContextMenu
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr_code_using_camera.*
import java.io.IOException
import java.lang.Exception

class QrCodeUsingCamera : AppCompatActivity() {
    var cameraSource: CameraSource? = null
    var REQUETSPERMISSIONID: Int = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_using_camera)
        var barcodeDetector: BarcodeDetector = BarcodeDetector.Builder(applicationContext)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .build()

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        this@QrCodeUsingCamera,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@QrCodeUsingCamera,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUETSPERMISSIONID
                    )
                    return
                }
                try {
                    cameraSource!!.start(cameraView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource!!.stop()
            }

        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detector: Detector.Detections<Barcode>?) {
                var qrcode: SparseArray<Barcode> = detector!!.detectedItems
                if (qrcode.size() != 0) {
                    txtReadText.post(object : Runnable {
                        override fun run() {
                            var vibrator =
                                applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(300)
                            txtReadText.setText(qrcode.valueAt(0).displayValue)
                        }
                    })
                }
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUETSPERMISSIONID -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this@QrCodeUsingCamera,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    try {
                        cameraSource!!.start(cameraView.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}