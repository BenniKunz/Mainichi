//package ninja.droiddojo.destiny.feature.inventory.navigation
//
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavType
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import androidx.navigation.navigation
//import ninja.droiddojo.destiny.core.navigation.DestinyNavigationDestination
//import ninja.droiddojo.destiny.feature.inventory.overview.InventoryOverviewScreen
//import ninja.droiddojo.destiny.feature.inventory.search.InventorySearchScreen
//import ninja.droiddojo.destiny.feature.inventory.transfer.TransferWeaponScreen
//
//object InventoryDestination : DestinyNavigationDestination {
//    override val route = "inventory"
//    override val destination = "inventory_overview"
//}
//
//object InventorySearchDestination : DestinyNavigationDestination {
//    override val route = "inventory/search"
//    override val destination = "inventory_search"
//}
//
//object TransferItemDestination : DestinyNavigationDestination {
//    const val instanceIdArg = "instanceId"
//
//    override val route = "inventory/transfer/{$instanceIdArg}"
//    override val destination = "transfer_destination"
//
//    fun createNavigationRoute(instanceId: Long): String {
//        return "inventory/transfer/$instanceId"
//    }
//}
//
//@OptIn(ExperimentalComposeUiApi::class)
//fun NavGraphBuilder.inventoryGraph(
//    onBackClick: () -> Unit,
//    navigateToSearch: () -> Unit,
//    navigateToTransfer: (Long) -> Unit
//) {
//    navigation(
//        route = InventoryDestination.route,
//        startDestination = InventoryDestination.destination
//    ) {
//        composable(route = InventoryDestination.destination) {
//            InventoryOverviewScreen(
//                navigateToSearch = navigateToSearch,
//                transferWeapon = { navigateToTransfer(it.instanceId!!) }
//            )
//        }
//        composable(route = InventorySearchDestination.destination) {
//            InventorySearchScreen(onWeaponSelected = { navigateToTransfer(it.instanceId!!) })
//        }
//        composable(
//            route = TransferItemDestination.route,
//            arguments = listOf(
//                navArgument(TransferItemDestination.instanceIdArg) { type = NavType.LongType }
//            )
//        ) {
//            TransferWeaponScreen(closeScreen = onBackClick)
//        }
//    }
//}
