package com.test.thecocktaildb.ui.dialog

sealed class DialogButton

sealed class RegularDialogButton: DialogButton()
object LeftDialogButton: RegularDialogButton()
object RightDialogButton: RegularDialogButton()

sealed class ListDialogButton: RegularDialogButton()
object ItemListDialogButton: ListDialogButton()
object LeftListDialogButton: ListDialogButton()
object RightListDialogButton: ListDialogButton()

sealed class SingleDialogButton: DialogButton()
object ActionSingleDialogButton: SingleDialogButton()