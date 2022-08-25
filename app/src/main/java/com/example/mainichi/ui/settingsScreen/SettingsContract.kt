package com.example.mainichi.ui.settingsScreen

import com.example.mainichi.ui.entities.Asset
import java.util.concurrent.TimeUnit

class SettingsContract {

    data class UiState(
        val isLoading: Boolean,
        val assets: List<Asset> = emptyList(),
        val notifications: List<AssetNotification> = emptyList(),
        val categoryMap : Map<String,List<CategoryItem>>
    )

     sealed class SettingsEvent {

         data class CreateCustomNotification(val asset : String) : SettingsEvent()

         data class SelectCategoryItem(
             val categoryType : CategoryValues,
         ) : SettingsEvent()
     }

     sealed class SettingsEffect {

     }
}

