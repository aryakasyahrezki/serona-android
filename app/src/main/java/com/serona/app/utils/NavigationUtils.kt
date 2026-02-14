package com.serona.app.utils

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch

@Composable
fun rememberNavigationGuard(): Triple<Boolean, (action: () -> Unit) -> Unit, () -> Unit> {
    var isNavigating by remember { mutableStateOf(true) }
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                    isNavigating = false
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                isNavigating = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val safeAction: (action: () -> Unit) -> Unit = { action ->
        val currentTime = System.currentTimeMillis()
        if (!isNavigating && currentTime - lastClickTime > 500) {
            isNavigating = true
            lastClickTime = currentTime
            action()
        }
    }

    // Sekarang mengembalikan Triple: Status, Fungsi Safe, dan Fungsi Reset
    return Triple(isNavigating, safeAction, { isNavigating = false })
}