package com.fluentbuild.pcas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.models.MainAction
import com.fluentbuild.pcas.models.getBackgroundColor
import com.fluentbuild.pcas.models.getIconDrawable
import com.fluentbuild.pcas.services.PeripheralService
import com.fluentbuild.pcas.services.ServiceUiModel
import com.fluentbuild.pcas.services.ServicesRenderer
import com.fluentbuild.pcas.ui.ConsoleRenderer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val cancellables = Cancellables()
    private val consoleRenderer by lazy { ConsoleRenderer(consoleCardView) }
    private val servicesRenderer by lazy { ServicesRenderer(servicesContainer, ::onServiceClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getApp().engineController.setRunCallback { engineState ->
            engineButtonView.setImageDrawable(engineState.getIconDrawable(this))
            engineButtonView.backgroundTintList = engineState.getBackgroundColor(this)
        }

        bottomAppBarView.setOnMenuItemClickListener {
            onActionClicked(MainAction.from(it.itemId))
            true
        }

        cancellables += servicesRenderer.render()
        cancellables += consoleRenderer.render()
    }

    private fun onServiceClicked(service: ServiceUiModel) {
        when(service.service) {
            PeripheralService.AUDIO -> {

            }
            PeripheralService.KEYPAD -> {

            }
            PeripheralService.MOUSE -> {

            }
        }
    }

    private fun onActionClicked(action: MainAction) {
        when(action) {
            MainAction.CLEAR_CONSOLE -> {
                consoleRenderer.clearConsole()
            }
            MainAction.SETUP -> {
                // TODO
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellables.cancel()
    }

    companion object {

        fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}