package rubenquadros.com.artivatic.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.toolbar_layout.*
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.custom.LoginViewModelFactory
import rubenquadros.com.artivatic.databinding.ActivityLoginBinding
import rubenquadros.com.artivatic.utility.application.ApplicationConstants
import rubenquadros.com.artivatic.utility.application.ApplicationUtility
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import rubenquadros.com.artivatic.viewmodel.LoginViewModel

class Login : AppCompatActivity(), CallBacks {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mView: View
    private lateinit var facebookBtn: LoginButton
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupToolBar()
        isCamScannerInstalled()
    }

    private fun setupBinding() {
        val activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this, application)).get(LoginViewModel::class.java)
        activityLoginBinding.loginHandler = loginViewModel
        mView = findViewById(R.id.parent)
        facebookBtn = findViewById(R.id.loginButton)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbarTitle.text = this.getString(R.string.login_btn_label)
    }

    private fun isCamScannerInstalled() {
        if(!(loginViewModel.checkCamScanner())) {
            createDownloadDialog(loginViewModel.getDownloadLink())
        }
    }

    private fun createDownloadDialog(link: String) {
        val mDialog = android.app.AlertDialog.Builder(this, R.style.YourDialogStyle)
        mDialog.setTitle(R.string.force_install)
        mDialog.setMessage(R.string.force_install_cs)
        mDialog.setPositiveButton(R.string.ok) { _, _ ->
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                finish()
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    )
                )
                finish()
            }
        }
        val alert = mDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setOnKeyListener { dialog1, keyCode, event ->
            if (KeyEvent.KEYCODE_BACK == keyCode && event.action == KeyEvent.ACTION_UP) {
                dialog1.dismiss()
                finish()
            }
            false
        }
        alert.show()
    }

    private fun doFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        facebookBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                loginSuccess()
            }

            override fun onCancel() {
                ApplicationUtility.showToastMsg(getString(R.string.cancelled), this@Login)
            }

            override fun onError(error: FacebookException?) {
                ApplicationUtility.showSnack(getString(R.string.generic_err), mView, getString(R.string.ok))
            }
        })
    }

    private fun loginSuccess() {
        startActivity(Intent(this, ImageActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onFailed(message: Int) {
        ApplicationUtility.showSnack(this.getString(message), mView, this.getString(R.string.ok))
    }

    override fun onSuccess(response: String) {
        when(response) {
            ApplicationConstants.REG_CLICKED -> startActivity(Intent(this, Registration::class.java))
            ApplicationConstants.FACEBOOK_CLICKED -> doFacebookLogin()
            ApplicationConstants.LOGIN_SUCCESS -> loginSuccess()
        }
    }

    override fun onBackPressed() {
        LoginManager.getInstance().logOut()
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        LoginManager.getInstance().logOut()
        super.onPause()

    }

    override fun onStop() {
        LoginManager.getInstance().logOut()
        super.onStop()
    }

    override fun onDestroy() {
        LoginManager.getInstance().logOut()
        super.onDestroy()
    }
}
