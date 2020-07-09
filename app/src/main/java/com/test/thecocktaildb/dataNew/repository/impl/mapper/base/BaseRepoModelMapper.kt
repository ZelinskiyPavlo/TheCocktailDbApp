package com.test.thecocktaildb.dataNew.repository.impl.mapper.base

abstract class BaseRepoModelMapper<RepoModel, DbModel, NetModel> {

    open fun mapDbToRepo(db: DbModel): RepoModel {
        throw NotImplementedError("provide mapping for model ${db!!::class.java.simpleName}")
    }

    open fun mapRepoToDb(repo: RepoModel): DbModel {
        throw NotImplementedError("provide mapping for model ${repo!!::class.java.simpleName}")
    }

    open fun mapDbToRepo(db: List<DbModel>): List<RepoModel> = db.map(::mapDbToRepo)
    open fun mapRepoToDb(repo: List<RepoModel>): List<DbModel> = repo.map(::mapRepoToDb)
}