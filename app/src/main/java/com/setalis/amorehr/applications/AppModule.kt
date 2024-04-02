package com.setalis.amorehr.applications

import android.app.Application
import androidx.room.Room
import com.setalis.amorehr.BuildConfig
import com.setalis.amorehr.base.AmModule
import com.setalis.amorehr.commons.managers.DatabaseManager
import com.setalis.amorehr.commons.managers.SessionManager
import com.setalis.amorehr.commons.managers.userDao
import com.setalis.amorehr.repositories.attend.AttendRepository
import com.setalis.amorehr.repositories.auth.AuthRepository
import com.setalis.amorehr.utils.networks.Network
import com.setalis.amorehr.viewmodels.AttendViewModel
import com.setalis.amorehr.viewmodels.AuthViewModel
import com.setalis.amorehr.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppModule : AmModule {

    override var loadNeeded: Boolean = true

    override fun getModule() = module {

        single { provideDataBase(androidApplication()) }

        single { userDao(get()) }

        single(named(KoinModule.Server)) { BuildConfig.server }

        single(named(KoinModule.DispatcherIO)) { Dispatchers.IO }
        single(named(KoinModule.DispatcherMain)) { Dispatchers.Main }

        single(named(KoinModule.CoroutineIo)) { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
        single(named(KoinModule.CoroutineMain)) { CoroutineScope(Dispatchers.Main + SupervisorJob()) }

        // Shared Preference
        factory { SessionManager(get()) }

        // Network
        single { Network(androidContext()) }

        viewModel { UserViewModel(get(), get()) }
        viewModel { AuthViewModel(get(),get()) }
        viewModel { AttendViewModel(get(),get()) }

        // single { AlarmReceiver()  }

        single { AuthRepository() }
        single { AttendRepository() }

    }.also {
        loadNeeded = false
    }

    private fun provideDataBase(application: Application): DatabaseManager {
        return Room.databaseBuilder(application.applicationContext, DatabaseManager::class.java,
            BuildConfig.database
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}
