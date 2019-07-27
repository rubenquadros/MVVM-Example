package rubenquadros.com.artivatic.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.view.View
import com.intsig.csopen.sdk.CSOpenAPI
import com.intsig.csopen.sdk.CSOpenApiFactory
import com.intsig.csopen.sdk.ReturnCode
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.utility.application.ApplicationConstants
import rubenquadros.com.artivatic.utility.callbacks.ImageCallBacks
import java.io.File
import java.text.SimpleDateFormat
import android.os.Environment.DIRECTORY_PICTURES
import java.io.IOException
import java.util.*


@Suppress("JoinDeclarationAndAssignment", "UNUSED_PARAMETER")
class ImageViewModel(private var imageCallBacks: ImageCallBacks, private var mApplication: Application): AndroidViewModel(mApplication) {

    private var mCamScanner: CSOpenAPI

    init {
        mCamScanner = CSOpenApiFactory.createCSOpenApi(mApplication, ApplicationConstants.API_KEY, null)
    }

    fun galleryClicked(view: View) {
        imageCallBacks.requestPermAndProcess(ApplicationConstants.GALLERY_PERMISSION)
    }

    fun cameraClicked(view: View) {
        imageCallBacks.requestPermAndProcess(ApplicationConstants.CAMERA_PERMISSION)
    }

    fun checkDir(path: String) : Boolean {
        var result = true
        val file = File(path)
        if(!file.exists()) {
            result = file.mkdirs()
        }else if(file.isFile) {
            file.delete()
            result = file.mkdirs()
        }
        return result
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(ApplicationConstants.DATE_FORMAT, Locale.getDefault()).format(Date())
        val imageFileName = ApplicationConstants.IMAGE_PREFIX + timeStamp + ApplicationConstants.IMAGE_SUFFIX
        val storageDir = mApplication.getExternalFilesDir(DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ApplicationConstants.JPG_SUFFIX, storageDir)
    }

    fun handleErrResponse(code: Int) {
        when (code) {
            ReturnCode.OK -> imageCallBacks.onFailed(R.string.a_msg_api_success)
            ReturnCode.INVALID_APP -> imageCallBacks.onFailed(R.string.a_msg_invalid_app)
            ReturnCode.INVALID_SOURCE -> imageCallBacks.onFailed(R.string.a_msg_invalid_source)
            ReturnCode.AUTH_EXPIRED -> imageCallBacks.onFailed(R.string.a_msg_auth_expired)
            ReturnCode.MODE_UNAVAILABLE -> imageCallBacks.onFailed(R.string.a_msg_mode_unavailable)
            ReturnCode.NUM_LIMITED -> imageCallBacks.onFailed(R.string.a_msg_num_limit)
            ReturnCode.STORE_JPG_ERROR -> imageCallBacks.onFailed(R.string.a_msg_store_jpg_error)
            ReturnCode.STORE_PDF_ERROR -> imageCallBacks.onFailed(R.string.a_msg_store_pdf_error)
            ReturnCode.STORE_ORG_ERROR -> imageCallBacks.onFailed(R.string.a_msg_store_org_error)
            ReturnCode.APP_UNREGISTERED -> imageCallBacks.onFailed(R.string.a_msg_app_unregistered)
            ReturnCode.API_VERSION_ILLEGAL -> imageCallBacks.onFailed(R.string.a_msg_api_version_illegal)
            ReturnCode.DEVICE_LIMITED -> imageCallBacks.onFailed(R.string.a_msg_device_limited)
            ReturnCode.NOT_LOGIN -> imageCallBacks.onFailed(R.string.a_msg_not_login)
            else -> imageCallBacks.onFailed(R.string.generic_err)
        }
    }

}