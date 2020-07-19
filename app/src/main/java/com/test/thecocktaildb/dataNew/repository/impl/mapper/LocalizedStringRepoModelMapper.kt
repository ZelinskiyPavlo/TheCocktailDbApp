package com.test.thecocktaildb.dataNew.repository.impl.mapper

import com.test.thecocktaildb.dataNew.db.model.LocalizedStringDbModel
import com.test.thecocktaildb.dataNew.repository.impl.mapper.base.BaseRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.model.LocalizedStringRepoModel
import javax.inject.Inject

class LocalizedStringRepoModelMapper  @Inject constructor():
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedStringDbModel, Any /*CocktailNetModel*/>() {
    override fun mapDbToRepo(db: LocalizedStringDbModel) = with(db) {
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
}