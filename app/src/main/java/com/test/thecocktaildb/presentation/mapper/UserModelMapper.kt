package com.test.thecocktaildb.presentation.mapper

import com.test.thecocktaildb.data.repository.model.UserRepoModel
import com.test.thecocktaildb.presentation.mapper.base.BaseModelMapper
import com.test.thecocktaildb.presentation.model.UserModel
import javax.inject.Inject

class UserModelMapper @Inject constructor() : BaseModelMapper<UserModel, UserRepoModel>() {

    override fun mapFrom(model: UserModel) = with(model) {
        UserRepoModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }

    override fun mapTo(model: UserRepoModel)= with(model) {
        UserModel(
            id = id,
            email = email,
            name = name,
            lastName = lastName,
            avatar = avatar
        )
    }
}
