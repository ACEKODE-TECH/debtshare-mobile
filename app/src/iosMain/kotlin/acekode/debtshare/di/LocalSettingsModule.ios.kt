package acekode.debtshare.di

import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

actual val localSettingsModule: Module = module {
    single<Settings> {
        KeychainSettings("debtshare_auth")
    }
}
