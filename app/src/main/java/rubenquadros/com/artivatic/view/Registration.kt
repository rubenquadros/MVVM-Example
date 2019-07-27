package rubenquadros.com.artivatic.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.toolbar_layout.*
import rubenquadros.com.artivatic.R
import rubenquadros.com.artivatic.custom.RegistrationViewModelFactory
import rubenquadros.com.artivatic.databinding.ActivityRegistrationBinding
import rubenquadros.com.artivatic.utility.application.ApplicationUtility
import rubenquadros.com.artivatic.utility.callbacks.CallBacks
import rubenquadros.com.artivatic.viewmodel.RegistrationViewModel

class Registration : AppCompatActivity(), CallBacks {

    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupToolBar()
    }

    private fun setupBinding() {
        val activityRegistrationBinding = DataBindingUtil.setContentView<ActivityRegistrationBinding>(this, R.layout.activity_registration)
        registrationViewModel = ViewModelProviders.of(this, RegistrationViewModelFactory(this)).get(RegistrationViewModel::class.java)
        activityRegistrationBinding.regHandler = registrationViewModel
        mView = findViewById(R.id.parent)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTitle.text = this.getString(R.string.register_btn_label)
    }

    override fun onFailed(message: Int) {
        ApplicationUtility.showSnack(this.getString(message), mView, this.getString(R.string.ok))
    }

    override fun onSuccess(response: String) {

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }
}
