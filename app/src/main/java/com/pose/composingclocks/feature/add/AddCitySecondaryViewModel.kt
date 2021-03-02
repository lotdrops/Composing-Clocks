package com.pose.composingclocks.feature.add

import com.pose.composingclocks.core.scopednav.ViewController

class AddCitySecondaryViewModel(
    private val parentViewModel: AddCityFlowViewModel,
    private val goBack: () -> Unit,
) : ViewController() {
    val description = parentViewModel.description
    val photoUrl = parentViewModel.photoUrl

    fun onSaveClicked() {
        parentViewModel.onSave()
    }

    fun onBack() {
        goBack()
    }
}
