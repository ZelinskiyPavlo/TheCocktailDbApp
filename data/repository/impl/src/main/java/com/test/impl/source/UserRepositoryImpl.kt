package com.test.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.database.source.UserDbSource
import com.test.impl.mapper.UserRepoModelMapper
import com.test.network.source.UserNetSource
import com.test.network.source.UserUploadNetSource
import com.test.repository.model.UserRepoModel
import com.test.repository.source.UserRepository
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
        userNetSource.updateUser(user.run(userModelMapper::mapRepoToNet))
        userDbSource.saveUser(user.run(userModelMapper::mapRepoToDb))
    }

    override suspend fun updateUserAvatar(avatar: File, onUploadProgress: (Float) -> Unit): String {
        return userUploadNetSource
            .updateUserAvatar(avatar) { percent, _, _ -> onUploadProgress(percent) }
        // Commented only due to the lack of a real API
//            .apply{ refreshUser() }
    }

    override suspend fun deleteUser() {
        userDbSource.deleteUser()
    }
}