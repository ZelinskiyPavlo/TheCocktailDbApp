package com.test.presentation.mapper.user

import com.test.presentation.mapper.base.BaseModelMapper
import com.test.presentation.model.user.UserModel
import com.test.repository.model.UserRepoModel
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
