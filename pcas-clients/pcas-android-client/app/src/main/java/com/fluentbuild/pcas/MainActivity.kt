package com.fluentbuild.pcas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.actions.ClearLogAction
import com.fluentbuild.pcas.actions.DryRunEngineAction
import com.fluentbuild.pcas.actions.SetupNetworkAction
import com.fluentbuild.pcas.actions.SoftStopEngineAction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val hostConfig get() = appComponent.hostConfigStore.getPartial()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBarView.setOnMenuItemClickListener {
            onActionClicked(it.itemId)
            true
        }

        DryRunEngineAction.perform(this)

        if(hostConfig.audioPeripheral == null) {
            SetupNetworkAction.perform(this)
        }
    }

    private fun onActionClicked(itemId: Int) {
        when(itemId) {
            R.id.actionClearConsole -> {
                ClearLogAction.perform(this)
            }
            R.id.actionSetup -> {
                SetupNetworkAction.perform(this)
            }
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