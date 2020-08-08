package com.test.thecocktaildb.dataNew.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import com.test.thecocktaildb.dataNew.network.source.UserNetSource
import com.test.thecocktaildb.dataNew.network.source.UserUploadNetSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.UserRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.model.UserRepoModel
import com.test.thecocktaildb.dataNew.repository.source.UserRepository
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDbSource: UserDbSource,
    private val userNetSource: UserNetSource,
    private val userUploadNetSource: UserUploadNetSource,
    private val userModelMapper: UserRepoModelMapper
) : UserRepository {

    override val userLiveData: LiveData<UserRepoModel?> = userDbSource.userLiveData
        .map {
            when {
                it != null -> userModelMapper.mapDbToRepo(it)
                else -> null
            }
        }

    override suspend fun hasUser(): Boolean = userDbSource.hasUser()

    override suspend fun getUser() = userDbSource.getUser()?.run(userModelMapper::mapDbToRepo)

    override suspend fun refreshUser() {
        userNetSource.getUser()
            .run(userModelMapper::mapNetToDb)
            .run { userDbSource.saveUser(this) }
    }

    override suspend fun updateUser(user: UserRepoModel) {
        userDbSource.saveUser(user.run(userModelMapper::mapRepoToDb))
        userNetSource.updateUser(user.run(userModelMapper::mapRepoToNet))
    }

    override suspend fun updateUserAvatar(avatar: File, onUploadProgress: (Float) -> Unit): String {
        return userUploadNetSource
            .updateUserAvatar(avatar) { percent, _, _ -> onUploadProgress(percent) }
            .apply{ refreshUser() }
    }

    override suspend fun deleteUser() {
        userDbSource.deleteUser()
    }
}