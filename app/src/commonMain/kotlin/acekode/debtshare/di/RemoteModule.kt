package acekode.debtshare.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("acekode.debtshare.data.datasource")
class RemoteModule
