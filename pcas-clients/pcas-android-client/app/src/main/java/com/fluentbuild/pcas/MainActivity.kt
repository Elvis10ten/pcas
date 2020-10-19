package com.fluentbuild.pcas

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.actions.DryRunEngineAction
import com.fluentbuild.pcas.actions.SelectPeripheralAction
import com.fluentbuild.pcas.actions.SetupSecurityAction
import com.fluentbuild.pcas.actions.SoftStopEngineAction
import com.fluentbuild.pcas.services.ServiceClass
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {

    private val engineStateObservable get() = appComponent.engineStateObservable
    private val currentHostConfig get() = engineStateObservable.currentAppState.hostConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBarView.setOnMenuItemClickListener {
            onActionClicked(it.itemId)
            true
        }

        DryRunEngineAction.perform(this)

        if(currentHostConfig?.audioPeripheral == null) {
            SelectPeripheralAction(ServiceClass.AUDIO).perform(this)
        }

        /*val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(intent)

        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }*/
    }

    private fun onActionClicked(itemId: Int) {
        when(itemId) {
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