package com.test.presentation.mapper.cocktail

import com.test.presentation.mapper.base.BaseModelMapper
import com.test.presentation.model.cocktail.LocalizedStringModel
import com.test.repository.model.LocalizedStringRepoModel
import javax.inject.Inject

/**
 * You could sometimes create object instead of class in case there is no args
 */
class LocalizedStringModelMapper @Inject constructor() : BaseModelMapper<LocalizedStringModel,
        LocalizedStringRepoModel>() {

    override fun mapTo(model: LocalizedStringRepoModel) = with(model) {
        LocalizedStringModel(
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapFrom(model: LocalizedStringModel) = with(model) {
        LocalizedStringRepoModel(
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }
}