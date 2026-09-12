package acekode.debtshare.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class, DataModule::class, RemoteModule::class, LocalModule::class])
@ComponentScan("acekode.debtshare.presentation")
class AppModule
