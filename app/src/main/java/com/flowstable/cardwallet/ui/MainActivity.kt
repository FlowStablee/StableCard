package com.flowstable.cardwallet.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.core.view.WindowCompat
import com.flowstable.cardwallet.ui.navigation.FlowstableNavHost
import com.flowstable.cardwallet.ui.theme.FlowstableTheme
import com.flowstable.cardwallet.viewmodel.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FlowstableTheme {
                FlowstableNavHost(sessionViewModel = sessionViewModel)
            }
        }
    }

    fun disableScreenshots(disable: Boolean) {
        if (disable) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

