package rubenquadros.com.artivatic.model

data class RegistrationModel(var name: String, var email: String, var mobile: String, var pass: String, var confPass: String) {
    override fun toString(): String {
        return "RegistrationModel(name='$name', email='$email', mobile='$mobile', pass='$pass', confPass='$confPass')"
    }
}