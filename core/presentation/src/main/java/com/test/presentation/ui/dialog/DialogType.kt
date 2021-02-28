package com.test.presentation.ui.dialog

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object LanguageDialogType: DialogType<ListDialogButton>()