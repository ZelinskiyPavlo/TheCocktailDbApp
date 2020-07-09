package com.test.thecocktaildb.presentationNew.mapper

import com.test.thecocktaildb.dataNew.repository.model.LocalizedStringRepoModel
import com.test.thecocktaildb.presentationNew.mapper.base.BaseModelMapper
import com.test.thecocktaildb.presentationNew.model.LocalizedStringModel
import javax.inject.Inject

/**
 * You could sometimes create object instead of class in case there is no args
 */
class LocalizedStringModelMapper @Inject constructor() : BaseModelMapper<LocalizedStringModel,
        LocalizedStringRepoModel>() {

    override fun mapTo(model: LocalizedStringRepoModel) = with(model) {
        LocalizedStringModel(
            default = default,
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
            default = default,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }
}