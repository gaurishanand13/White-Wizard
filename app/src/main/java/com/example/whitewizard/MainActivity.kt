package com.example.whitewizard
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whitewizard.Room.Daos.imageDao
import com.example.whitewizard.Room.Database.myDatabase
import com.example.whitewizard.Room.Models.imageModel
import com.example.whitewizard.recycler_view.BackgroundAdapter
import com.example.whitewizard.recycler_view.DashboardRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog_box.*
import kotlinx.android.synthetic.main.alert_dialog_box.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var dashboardList : LiveData<List<imageModel>> ? =null
    var dao : imageDao? = null

    var list = ArrayList<imageModel>()

    fun initialize(){
        //Setting up the dashboardRecyclerView
        dashboardRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =  DashboardRecyclerView(this,list)
        dashboardRecyclerView.adapter = adapter


        dao = myDatabase.getDatabase(this).myDao()
        dashboardList = dao!!.getAllPosts()

        dashboardList!!.observe(this, Observer {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        })


        //Adding a post
        fab.setOnClickListener {
            showUpAlertDialog()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==123 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            initialize()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
            Toast.makeText(this,"Grant Permissions to continue!",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this ,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
        }
        else{
            initialize()
        }



    }


    fun showUpAlertDialog(){
        val alertDialog = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_box,null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)

        //Default background
        dialogView.alert_dialog_image_view.setImageResource(R.drawable.image_one)

        //Setting up the background recycler View
        dialogView.BackgroundRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        dialogView.BackgroundRecyclerView.adapter = BackgroundAdapter(this,dialogView.alert_dialog_image_view)


        val dialog = alertDialog.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation


        dialogView.addPostButton.setOnClickListener {
            dialogView.alert_editTextView.isFocusable = false
            dialog.dismiss()
            dialogView.alert_editTextView.setBackgroundColor(resources.getColor(R.color.Transparent))
            saveImage(loadBitmapFromView(dialogView.dialogViewFrameLayout)!!,this)
        }
        dialog.show()
    }



    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }
    private fun saveImage(bitmap: Bitmap, context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "WhiteWizard")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
                dao?.insertImage(imageModel(list.size,uri.toString()))

            }
            else{
                Toast.makeText(this,"Error in saving image",Toast.LENGTH_SHORT).show()
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + "/" + "WhiteWizard")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                dao?.insertImage(imageModel(list.size,Uri.fromFile(file).toString()))
            }
            else{
                Toast.makeText(this,"Error in saving image",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }
    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}