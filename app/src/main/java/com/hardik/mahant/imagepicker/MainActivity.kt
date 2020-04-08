package com.hardik.mahant.imagepicker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button);

        button.setOnClickListener {
            if(hasPermission()){
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            }
        }
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED && requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val selectedImage: Uri = data.data!!
                val filePathColumn: Array<String> =
                    Array<String>(1) { MediaStore.Images.Media.DATA }
                val cursor: Cursor = contentResolver.query(
                    selectedImage,
                    filePathColumn, null, null, null
                )!!
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val picturePath: String = cursor.getString(columnIndex)
                val bitmap: Bitmap = BitmapFactory.decodeFile(picturePath)
                imageView.setImageBitmap(bitmap)
                cursor.close()
            }
        }
    }
}
