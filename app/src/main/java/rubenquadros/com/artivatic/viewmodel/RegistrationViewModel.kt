package rubenquadros.com.artivatic.viewmodel

import android.arch.lifecycle.ViewModel
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.model.RegistrationModel
import rubenquadros.com.artivatic.utility.application.ApplicationConstants
import rubenquadros.com.artivatic.utility.application.ApplicationUtility
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import java.util.regex.Pattern

@Suppress("JoinDeclarationAndAssignment", "UNUSED_PARAMETER")
class RegistrationViewModel(private var registrationCallBacks: CallBacks): ViewModel() {

    private var registrationModel:RegistrationModel

    init {
        this.registrationModel = RegistrationModel("", "", "", "", "")
    }

    fun afterNameTextChanged(s: Editable?) {
        registrationModel.name = s.toString()
    }

    fun afterEmailTextChanged(s: Editable?) {
        registrationModel.email = s.toString()
    }

    fun afterMobileTextChanged(s: Editable?) {
        registrationModel.mobile = s.toString()
    }

    fun afterPassTextChanged(s: Editable?) {
        registrationModel.pass = s.toString()
    }

    fun afterConfPassTextChanged(s: Editable?) {
        registrationModel.confPass = s.toString()
    }

    fun onRegBtnClicked(view: View) {
        when {
            (TextUtils.isEmpty(registrationModel.name) || TextUtils.isEmpty(registrationModel.email) ||
                    TextUtils.isEmpty(registrationModel.pass) || TextUtils.isEmpty(registrationModel.confPass)) ->
                registrationCallBacks.onFailed(R.string.empty_field_reg)

            !ApplicationUtility.regexValidator(Pattern.compile(
                ApplicationConstants.EMAIL_REGEX, Pattern.CASE_INSENSITIVE), registrationModel.email) ->
                registrationCallBacks.onFailed(R.string.ent_valid_mail)

            registrationModel.mobile.length < ApplicationConstants.NUM_LENGTH -> registrationCallBacks.onFailed(R.string.mobile_length)

            (registrationModel.pass.length < ApplicationConstants.MIN_PIN_LENGTH ||
                    registrationModel.confPass.length < ApplicationConstants.MIN_PIN_LENGTH) ->
                registrationCallBacks.onFailed(R.string.pass_length)

            registrationModel.pass != registrationModel.confPass -> registrationCallBacks.onFailed(R.string.pass_no_match)

            else -> Log.d("@@@", "SUCCESS REG")
        }
    }
}