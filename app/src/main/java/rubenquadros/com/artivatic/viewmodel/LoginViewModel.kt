package rubenquadros.com.artivatic.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.intsig.csopen.sdk.CSOpenAPI
import com.intsig.csopen.sdk.CSOpenApiFactory
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.model.LoginModel
import rubenquadros.com.artivatic.utility.application.ApplicationConstants
import rubenquadros.com.artivatic.utility.application.ApplicationUtility
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import java.util.regex.Pattern

@Suppress("JoinDeclarationAndAssignment", "UNUSED_PARAMETER")
class LoginViewModel(private var loginCallBacks: CallBacks, mApplication: Application) : AndroidViewModel(mApplication) {

    private var loginModel: LoginModel
    private var mCamScanner: CSOpenAPI

    init {
        this.loginModel = LoginModel("", "")
        mCamScanner = CSOpenApiFactory.createCSOpenApi(mApplication, ApplicationConstants.API_KEY, null)
    }

    fun afterEmailTextChanged(s: Editable?) {
        loginModel.email = s.toString()
    }

    fun afterPassTextChanged(s: Editable?) {
        loginModel.pass = s.toString()
    }

    fun facebookBtnClicked(view: View) {
        loginCallBacks.onSuccess(ApplicationConstants.FACEBOOK_CLICKED)
    }

    fun loginBtnClicked(view: View) {
        when {
            TextUtils.isEmpty(loginModel.email) -> loginCallBacks.onFailed(R.string.empty_mail)

            TextUtils.isEmpty(loginModel.pass) -> loginCallBacks.onFailed(R.string.empty_pass)

            !ApplicationUtility.regexValidator(Pattern.compile(
                ApplicationConstants.EMAIL_REGEX, Pattern.CASE_INSENSITIVE), loginModel.email) -> loginCallBacks.onFailed(R.string.ent_valid_mail)

            loginModel.pass.length < ApplicationConstants.MIN_PIN_LENGTH -> loginCallBacks.onFailed(R.string.pass_length)

            else -> loginCallBacks.onSuccess(ApplicationConstants.LOGIN_SUCCESS)
        }
    }

    fun regBtnClicked(view: View) {
        loginCallBacks.onSuccess(ApplicationConstants.REG_CLICKED)
    }

    fun checkCamScanner(): Boolean {
        return mCamScanner.isCamScannerInstalled
    }

    fun getDownloadLink(): String {
        return mCamScanner.downloadLink
    }
}