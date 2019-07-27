package rubenquadros.com.artivatic.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*
import rubenquadros.com.artivatic.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setTransition()
    }

    private fun setTransition() {
        textView.animate().alpha(1f).duration = 3000L
        Handler().postDelayed({
            goToLogin()
        }, 3000)
    }

    private fun goToLogin(){
        Handler().postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish()
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        Handler().removeCallbacksAndMessages(null)
    }
}
