package com.test.thecocktaildb.data.repository.impl.mapper

import com.test.thecocktaildb.data.db.model.cocktail.LocalizedStringDbModel
import com.test.thecocktaildb.data.network.model.cocktail.LocalizedStringNetModel
import com.test.thecocktaildb.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.test.thecocktaildb.data.repository.model.LocalizedStringRepoModel
import javax.inject.Inject

class LocalizedStringRepoModelMapper @Inject constructor() :
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedStringDbModel?, LocalizedStringNetModel?>() {
    override fun mapDbToRepo(db: LocalizedStringDbModel?) = with(db) {
        this ?: return LocalizedStringRepoModel()
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

    override fun mapRepoToDb(repo: LocalizedStringRepoModel) = with(repo) {
        LocalizedStringDbModel(
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapNetToRepo(net: LocalizedStringNetModel?) = with(net) {
        this ?: return LocalizedStringRepoModel()
        LocalizedStringRepoModel(
            defaults = default,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapNetToDb(net: LocalizedStringNetModel?) = with(net) {
        this ?: return LocalizedStringDbModel()
        LocalizedStringDbModel(
            defaults = default,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }
}