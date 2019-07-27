package rubenquadros.com.artivatic.model


data class LoginModel(var email : String, var pass: String) {
    override fun toString(): String {
        return "LoginModel(email='$email', pass='$pass')"
    }
}