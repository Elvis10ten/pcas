package com.fluentbuild.pcas.models

import android.content.Intent
import android.graphics.Bitmap

class EncryptionSetupModel(
    val title: String,
    val instruction: String,
    val qrCode: Bitmap?,
    val hasKey: Boolean,
    val onDisableClicked: () -> Unit,
    val onCreateKeyClicked: () -> Unit,
    val onScanCodeClicked: () -> Unit,
    val onQrCodeScanned: (Intent) -> Boolean
): Model