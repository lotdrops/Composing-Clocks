package com.pose.composingclocks.app

import android.app.Application
import com.pose.composingclocks.common.commonModule
import com.pose.composingclocks.feature.add.addModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            commonModule.single { applicationScope }
            modules(appModule, addModule, commonModule)
        }
    }
}
