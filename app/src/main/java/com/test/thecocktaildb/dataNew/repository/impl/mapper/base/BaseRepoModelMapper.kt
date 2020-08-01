package com.test.thecocktaildb.dataNew.repository.impl.mapper.base

abstract class BaseRepoModelMapper<RepoModel, DbModel, NetModel> {

    open fun mapDbToRepo(db: DbModel): RepoModel {
        throw NotImplementedError("provide mapping for model ${db!!::class.java.simpleName}")
    }

    open fun mapRepoToDb(repo: RepoModel): DbModel {
        throw NotImplementedError("provide mapping for model ${repo!!::class.java.simpleName}")
    }

    open fun mapNetToRepo(net: NetModel): RepoModel {
        throw NotImplementedError("provide mapping for model ${net!!::class.java.simpleName}")
    }

    open fun mapRepoToNet(repo: RepoModel): NetModel {
        throw NotImplementedError("provide mapping for model ${repo!!::class.java.simpleName}")
    }

    open fun mapNetToDb(net: NetModel): DbModel {
        throw NotImplementedError("provide mapping for model ${net!!::class.java.simpleName}")
    }

    open fun mapDbToRepo(db: List<DbModel>): List<RepoModel> = db.map(::mapDbToRepo)
    open fun mapRepoToDb(repo: List<RepoModel>): List<DbModel> = repo.map(::mapRepoToDb)
    open fun mapNetToRepo(repo: List<NetModel>): List<RepoModel> = repo.map(::mapNetToRepo)
}