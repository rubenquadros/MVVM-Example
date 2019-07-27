package rubenquadros.com.artivatic.custom

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import rubenquadros.com.artivatic.viewmodel.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val loginCallBacks: CallBacks, private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginCallBacks, mApplication) as T
    }
}