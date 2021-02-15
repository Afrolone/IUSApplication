package com.afrolone.iusapp.db.userdata

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.afrolone.iusapp.app.IUSApplication


@RequiresApi(Build.VERSION_CODES.M)
object UserPrefs {
    private const val PREFS_NAME = "prefs_name"
    private const val ENCRYPTED_PREFS_NAME = "encrypted_$PREFS_NAME"
    private val  applicationContext = IUSApplication.getAppContext()

    val REMEMBER_CODE: String = "0"
    val USERNAME_CODE: String = "1"
    val PASSWORD_CODE: String = "2"
    val REMEMBER_ME: String = "true"
    val DONT_REMEMBER_ME: String = "false"

    private val sharedPrefs by lazy {
        applicationContext.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE)  //IUSApplication.getAppContext()
    }

    private val encryptedSharedPrefs by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
                ENCRYPTED_PREFS_NAME,
                masterKeyAlias,
                applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun put(key: String, value: String, encrypted: Boolean = false) {
        val prefs = if (encrypted) sharedPrefs else encryptedSharedPrefs
        with(prefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun get(key: String, default: String? = null, encrypted: Boolean = false): String? {
        val prefs = if (encrypted) sharedPrefs else encryptedSharedPrefs
        return prefs.getString(key, default)
    }

    fun checkPrefs(): Boolean {
        val remember: String? = get(REMEMBER_CODE)
        return remember == "true"
    }
}