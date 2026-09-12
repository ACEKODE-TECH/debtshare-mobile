package acekode.debtshare.di

import acekode.debtshare.AppContext
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

private const val SECURE_PREFS_FILE = "debtshare_secure_prefs"

actual val localSettingsModule: Module = module {
    single<Settings> {
        val isDebugBuild = AppContext.context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        val prefsName = if (isDebugBuild) {
            "${SECURE_PREFS_FILE}_debug"
        } else {
            "${SECURE_PREFS_FILE}_release"
        }

        val fallbackSettings by lazy {
            val fallbackPrefs = AppContext.context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            fallbackPrefs.edit(commit = true) { clear() }
            SharedPreferencesSettings(fallbackPrefs)
        }

        try {
            val masterKey = MasterKey.Builder(AppContext.context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            SharedPreferencesSettings(
                EncryptedSharedPreferences.create(
                    AppContext.context,
                    prefsName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
                ),
            )
        } catch (_: Exception) {
            fallbackSettings
        }
    }
}
