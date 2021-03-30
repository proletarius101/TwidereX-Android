package moe.tlaster.precompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Creates a [Navigator] that controls the [NavHost].
 *
 * @see NavHost
 */
@Composable
fun rememberNavController(): NavController {
    return remember { NavController() }
}

class NavController {
    internal lateinit var stackManager: RouteStackManager

    /**
     * Navigate to a route in the current RouteGraph.
     *
     * @param route route for the destination
     * @param options navigation options for the destination
     */
    fun navigate(route: String, options: NavOptions? = null) {
        stackManager.navigate(route, options)
    }

    suspend fun navigateForResult(route: String, options: NavOptions? = null): Any? {
        stackManager.navigate(route, options)
        val currentEntry = stackManager.currentEntry ?: return null
        return stackManager.waitingForResult(currentEntry)
    }

    /**
     * Attempts to navigate up in the navigation hierarchy. Suitable for when the
     * user presses the "Up" button marked with a left (or start)-facing arrow in the upper left
     * (or starting) corner of the app UI.
     */
    fun goBack() {
        stackManager.goBack()
    }

    fun goBackWith(result: Any? = null) {
        stackManager.goBack(result)
    }

    /**
     * Compatibility layer for Jetpack Navigation
     */
    fun popBackStack() {
        goBack()
    }

    /**
     * Check if navigator can navigate up
     */
    val canGoBack: Boolean
        get() = stackManager.canGoBack
}

// @Composable
// fun NavController.currentBackStackEntryAsState(): State<BackStackEntry?> {
//     val currentNavBackStackEntry = remember { mutableStateOf(stackManager.currentBackStackEntry) }
//     // setup the onDestinationChangedListener responsible for detecting when the
//     // current back stack entry changes
//     DisposableEffect(this) {
//         addOnDestinationChangedListener(callback)
//         onDispose {
//             removeOnDestinationChangedListener(callback)
//         }
//     }
//     return currentNavBackStackEntry
// }