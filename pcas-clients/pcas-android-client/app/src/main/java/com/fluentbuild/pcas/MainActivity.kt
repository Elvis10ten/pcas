package com.fluentbuild.pcas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.actions.*
import com.fluentbuild.pcas.services.ServiceClass
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    private val appStateObservable = appComponent.appStateObservable
    private val currentHostConfig get() = appStateObservable.currentAppState.hostConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBarView.setOnMenuItemClickListener {
            onActionClicked(it.itemId)
            true
        }

        DryRunEngineAction.perform(this)

        if(currentHostConfig.audioPeripheral == null) {
            SelectPeripheralAction(ServiceClass.AUDIO).perform(this)
        }
    }

    private fun onActionClicked(itemId: Int) {
        when(itemId) {
            R.id.actionClearConsole -> ClearLogAction.perform(this)
            R.id.actionSetupSecurity -> SetupSecurityAction.perform(this)
        }
    }

    override fun onDestroy() {
        SoftStopEngineAction.perform(this)
        super.onDestroy()
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}