package com.test.thecocktaildb.presentationNew.mapper

import com.test.thecocktaildb.dataNew.repository.model.UserRepoModel
import com.test.thecocktaildb.presentationNew.mapper.base.BaseModelMapper
import com.test.thecocktaildb.presentationNew.model.UserModel
import javax.inject.Inject

class UserModelMapper @Inject constructor() : BaseModelMapper<UserModel, UserRepoModel>() {

    override fun mapFrom(model: UserModel) = with(model) {
        UserRepoModel(
            id = id,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapTo(model: UserRepoModel)= with(model) {
        UserModel(
            id = id,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }
}
