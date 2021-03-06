package com.test.database.model.cocktail

import androidx.room.ColumnInfo

data class LocalizedStringDbModel (
    @ColumnInfo(name = "defaults")
    var defaults: String? = null,

    @ColumnInfo(name = "default_alternate")
    var defaultAlternate: String? = null,

    @ColumnInfo(name = "es")
    var es: String? = null,

    @ColumnInfo(name = "de")
    var de: String? = null,

    @ColumnInfo(name = "fr")
    var fr: String? = null,

    @ColumnInfo(name = "zn_hans")
    var zhHans: String? = null,

    @ColumnInfo(name = "zn_hant")
    var zhHant: String? = null
)