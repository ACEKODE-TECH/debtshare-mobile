package acekode.debtshare.di

import acekode.debtshare.AppContext
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

actual val localSettingsModule: Module = module {
    single<Settings> {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                "debtshare_secure_prefs",
                masterKeyAlias,
                AppContext.context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            ),
        )
    }
}
