package com.test.thecocktaildb.ui.dialog

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object NetworkDialogType: DialogType<SingleDialogButton>()
object DayPickerDialogType: DialogType<SingleDialogButton>()
object DatePickerDialogType: DialogType<SingleDialogButton>()
object SexDialogType: DialogType<ListDialogButton>()
object SupportDialogType: DialogType<ListDialogButton>()
object LanguageDialogType: DialogType<ListDialogButton>()
object ClinicServiceDialogType: DialogType<ListDialogButton>()
object DoctorSortDialogType: DialogType<ListDialogButton>()
object ClinicSortDialogType: DialogType<ListDialogButton>()
object ClinicDialogType: DialogType<ListDialogButton>()