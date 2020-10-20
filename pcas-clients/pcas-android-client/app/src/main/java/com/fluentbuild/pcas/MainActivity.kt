package com.fluentbuild.pcas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.actions.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBarView.setOnMenuItemClickListener {
            onActionClicked(it.itemId)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == MediaProjectionRequestAction.REQUEST_CODE) {
            MediaProjectionSetAction(resultCode, data).perform(this)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onActionClicked(itemId: Int) {
        when(itemId) {
            R.id.actionEncryptionSetup -> {
                EncryptionShowSetupAction.perform(this)
            }
            R.id.actionChangeBatteryOptimization -> {
                BatteryOptimizationOpenAction.perform(this)
            }
        }
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}