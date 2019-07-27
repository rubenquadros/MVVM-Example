package rubenquadros.com.artivatic.custom

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rubenquadros.com.artivatic.utility.callbacks.ImageCallBacks
import rubenquadros.com.artivatic.viewmodel.ImageViewModel

@Suppress("UNCHECKED_CAST")
class ImageViewModelFactory(private val imageCallBacks: ImageCallBacks, private val mApplication: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageViewModel(imageCallBacks,mApplication) as T

    }
}