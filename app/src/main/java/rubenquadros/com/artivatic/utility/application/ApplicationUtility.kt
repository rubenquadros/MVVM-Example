package rubenquadros.com.artivatic.utility.application

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import java.util.regex.Pattern

class ApplicationUtility {

    companion object {

        fun showSnack(msg: String, view: View, action: String){
            val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(action) {
                snackBar.dismiss()
            }
            snackBar.show()
        }

        fun regexValidator(pattern: Pattern, value: String): Boolean{
            if(!pattern.matcher(value).matches()){
                return false
            }
            return true
        }

        fun showToastMsg(msg: String, context: Context) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}