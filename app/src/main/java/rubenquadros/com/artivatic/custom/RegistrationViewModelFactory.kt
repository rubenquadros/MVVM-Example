package rubenquadros.com.artivatic.custom

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import rubenquadros.com.artivatic.viewmodel.RegistrationViewModel

@Suppress("UNCHECKED_CAST")
class RegistrationViewModelFactory(private val registrationCallBacks: CallBacks): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistrationViewModel(registrationCallBacks) as T
    }
}