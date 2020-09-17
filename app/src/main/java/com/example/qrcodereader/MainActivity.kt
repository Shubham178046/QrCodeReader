package com.example.qrcodereader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bitmap = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.barcode)
        imageView.setImageBitmap(bitmap)
        btnCapture.setOnClickListener {
            var barcodeDetector : BarcodeDetector = BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()
            var frame : Frame = Frame.Builder().setBitmap(bitmap).build()
            var sparseArray : SparseArray<Barcode> = barcodeDetector.detect(frame)

            var result : Barcode = sparseArray.valueAt(0)
            txtRead.setText(result.rawValue)
        }
    }
}