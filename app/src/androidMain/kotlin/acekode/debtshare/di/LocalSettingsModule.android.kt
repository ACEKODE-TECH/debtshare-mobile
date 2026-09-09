package acekode.debtshare.di

import acekode.debtshare.AppContext
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

actual val localSettingsModule: Module = module {
    single<Settings> {
        val masterKey = MasterKey.Builder(AppContext.context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                AppContext.context,
                "debtshare_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            ),
        )
    }
}
