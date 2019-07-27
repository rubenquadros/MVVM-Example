package rubenquadros.com.artivatic.view

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.facebook.login.LoginManager
import com.intsig.csopen.sdk.CSOpenAPI
import com.intsig.csopen.sdk.CSOpenAPIParam
import com.intsig.csopen.sdk.CSOpenApiFactory
import com.intsig.csopen.sdk.CSOpenApiHandler
import kotlinx.android.synthetic.main.toolbar_layout.*
import rubenquadros.com.artivatic.BuildConfig
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.custom.ImageViewModelFactory
import rubenquadros.com.artivatic.databinding.ActivityImageBinding
import rubenquadros.com.artivatic.utility.application.ApplicationConstants
import rubenquadros.com.artivatic.utility.application.ApplicationUtility
import rubenquadros.com.artivatic.utility.callbacks.ImageCallBacks
import rubenquadros.com.artivatic.viewmodel.ImageViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("PrivatePropertyName", "SpellCheckingInspection")
class ImageActivity : AppCompatActivity(), ImageCallBacks {

    private lateinit var imageViewModel: ImageViewModel
    private lateinit var mApi: CSOpenAPI
    private lateinit var photoFile: File
    private lateinit var mView: View
    private val ACCESS_GALLERY = 10211
    private val ACCESS_CAMERA = 10210
    private val GALLERY_RC = 10001
    private val CAMERA_RC = 10201
    private val CAMSCANNER_RC = 10301
    private lateinit var DIR: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupToolBar()
        init()
    }

    private fun setupBinding() {
        val activityImageBinding = DataBindingUtil.setContentView<ActivityImageBinding>(this, R.layout.activity_image)
        imageViewModel =
            ViewModelProviders.of(this, ImageViewModelFactory(this, application)).get(ImageViewModel::class.java)
        activityImageBinding.imageHandler = imageViewModel
        mView = findViewById(R.id.parent)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbarTitle.text = this.getString(R.string.welcome)
    }

    private fun init() {
        DIR = Environment.getExternalStorageDirectory().absolutePath + ApplicationConstants.MAIN_DIR
        mApi = CSOpenApiFactory.createCSOpenApi(this, ApplicationConstants.API_KEY, null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        LoginManager.getInstance().logOut()
        startActivity(Intent(this, Login::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun dispatchGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_RC)
    }

    private fun dispatchCameraIntent() {
        try {
            photoFile = imageViewModel.createImageFile()
        } catch (ex: IOException) {
            ApplicationUtility.showSnack(this.getString(R.string.generic_err), mView, this.getString(R.string.ok))
            return
        }
        val photoURI =
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ApplicationConstants.PROVIDER, photoFile)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(cameraIntent, CAMERA_RC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_RC -> {
                    if (data != null) {
                        val mURI = data.data
                        if (mURI != null) {
                            val c = contentResolver.query(mURI, arrayOf("_data"), null, null, null)
                            if (c == null || !c.moveToFirst()) {
                                return
                            }
                            val mSourceImagePath = c.getString(0)
                            c.close()
                            goToCamScanner(mSourceImagePath)
                        }
                    }
                }
                CAMERA_RC -> {
                    goToCamScanner(photoFile.absolutePath)
                }
                CAMSCANNER_RC -> {
                    mApi.handleResult(requestCode, resultCode, data, object : CSOpenApiHandler {
                        override fun onSuccess() {
                            ApplicationUtility.showToastMsg(getString(R.string.cancelled), this@ImageActivity)
                        }

                        override fun onCancel() {
                            ApplicationUtility.showToastMsg(getString(R.string.cancelled), this@ImageActivity)
                        }

                        override fun onError(p0: Int) {
                            imageViewModel.handleErrResponse(p0)
                        }

                    })
                }
            }
        } else {
            ApplicationUtility.showToastMsg(this.getString(R.string.not_selected), this)
        }
    }

    private fun goToCamScanner(inputPath: String) {
        val mOutputImagePath = DIR + ApplicationConstants.SCANNED_IMG
        val mOutputPDFPath = DIR + ApplicationConstants.SCANNED_PDF
        val mOutputOrgPath = DIR + ApplicationConstants.ORIGINAL_IMG
        try {
            val fileOutputStream = FileOutputStream(mOutputOrgPath)
            fileOutputStream.write(3)
            fileOutputStream.close()
        } catch (e: Exception) {
            ApplicationUtility.showSnack(this.getString(R.string.generic_err), mView, this.getString(R.string.ok))
            return
        }
        val param = CSOpenAPIParam(inputPath, mOutputImagePath, mOutputPDFPath, mOutputOrgPath, mApi.openApiVersion)
        mApi.scanImage(this, CAMSCANNER_RC, param)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_GALLERY -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ApplicationUtility.showSnack(
                        this.getString(R.string.gallery_permission),
                        mView,
                        this.getString(R.string.ok)
                    )
                } else {
                    imageViewModel.checkDir(DIR)
                    dispatchGalleryIntent()
                }
            }
            ACCESS_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ApplicationUtility.showSnack(
                        this.getString(R.string.camera_permission),
                        mView,
                        this.getString(R.string.ok)
                    )
                } else {
                    imageViewModel.checkDir(DIR)
                    dispatchCameraIntent()
                }
            }
        }
    }

    override fun onFailed(msg: Int) {
        ApplicationUtility.showSnack(this.getString(msg), mView, this.getString(R.string.ok))
    }

    override fun requestPermAndProcess(permission: String) {
        when (permission) {
            ApplicationConstants.GALLERY_PERMISSION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), ACCESS_GALLERY
                    )
                } else {
                    imageViewModel.checkDir(DIR)
                    dispatchGalleryIntent()
                }
            }
            ApplicationConstants.CAMERA_PERMISSION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        ACCESS_CAMERA
                    )
                } else {
                    imageViewModel.checkDir(DIR)
                    dispatchCameraIntent()
                }
            }
        }
    }

    override fun onPause() {
        LoginManager.getInstance().logOut()
        super.onPause()

    }

    override fun onStop() {
        LoginManager.getInstance().logOut()
        super.onStop()
    }

    override fun onDestroy() {
        LoginManager.getInstance().logOut()
        super.onDestroy()
    }
}
