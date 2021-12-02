package com.test.repositoryimpl.mapper

import com.test.database.model.UserDbModel
import com.test.network.model.UserNetModel
import com.test.repository.model.UserRepoModel
import com.test.repositoryimpl.mapper.base.BaseRepoModelMapper
import javax.inject.Inject

class UserRepoModelMapper @Inject constructor():
    BaseRepoModelMapper<UserRepoModel, UserDbModel, UserNetModel>() {
    override fun mapDbToRepo(db: UserDbModel): UserRepoModel = with(db) {
        UserRepoModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapRepoToDb(repo: UserRepoModel) = with(repo) {
        UserDbModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapNetToRepo(net: UserNetModel) = with(net) {
        UserRepoModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapRepoToNet(repo: UserRepoModel) = with(repo) {
        UserNetModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapNetToDb(net: UserNetModel) = with(net) {
        UserDbModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }
}