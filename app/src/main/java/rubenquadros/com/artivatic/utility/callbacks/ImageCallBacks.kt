package rubenquadros.com.artivatic.utility.callbacks

interface ImageCallBacks {

    fun requestPermAndProcess(permission: String)

    fun onFailed(msg: Int)
}