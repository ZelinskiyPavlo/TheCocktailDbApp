package com.test.repositoryimpl.mapper

import com.test.database.model.cocktail.LocalizedStringDbModel
import com.test.network.model.cocktail.LocalizedStringNetModel
import com.test.repository.model.LocalizedStringRepoModel
import com.test.repositoryimpl.mapper.base.BaseRepoModelMapper
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