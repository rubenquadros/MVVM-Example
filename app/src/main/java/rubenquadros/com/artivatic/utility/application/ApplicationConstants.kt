package rubenquadros.com.artivatic.utility.application

class ApplicationConstants {
    companion object {
        const val MIN_PIN_LENGTH = 8
        const val MAX_PIN_LENGTH = 12
        const val NUM_LENGTH = 10
        const val EMAIL_REGEX = "[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
        const val REG_CLICKED = "Register button clicked"
        const val LOGIN_SUCCESS = "Successful login"
        const val API_KEY = "CS_API_KEY"
        const val GALLERY_PERMISSION = "Request Gallery Permission"
        const val CAMERA_PERMISSION = "Request Camera Permission"
        const val MAIN_DIR = "/Artivatic"
        const val SCANNED_IMG = "/scanned.jpg"
        const val SCANNED_PDF = "/scanned.pdf"
        const val ORIGINAL_IMG = "/original.jpg"
        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val IMAGE_PREFIX = "IMG_"
        const val IMAGE_SUFFIX = "_"
        const val JPG_SUFFIX = ".jpg"
        const val PROVIDER = ".provider"
        const val FACEBOOK_CLICKED = "Facebook button clicked"
    }
}