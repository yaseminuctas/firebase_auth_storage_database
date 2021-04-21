package com.yaseminuctas.arabamisat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.yaseminuctas.arabamisat.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            // called once this listener is attached and every time after the auth state changes

            firebaseAuth.currentUser?.let {
                // the user is logged in
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } ?: run {
                // the user is logged out, log him/her in
                signIn()
            }
        }
    }

    fun signIn() {
        // using Google, Email-Password, and Phone Number based authentication
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        val authIntent = AuthUI.getInstance().createSignInIntentBuilder()
            // set a custom logo to be shown on the login screen
            .setLogo(R.mipmap.ic_launcher)
            // set the login screen's theme
            //.setTheme(R.style.AppThemeNoActionbar)
            // define the providers that will be used
            .setAvailableProviders(providers)
            // disable smart lock that automatically logs in a previously logged in user
            .setIsSmartLockEnabled(false)
            // set the terms of service and private policy URL for your app
            .setTosAndPrivacyPolicyUrls("example.termsofservice.com", "example.privatepolicy.com")
            .build()

        startActivity(authIntent)
    }
}