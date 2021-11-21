package com.test.repository.source

import com.test.repository.model.UserRepoModel
import com.test.repository.source.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository : BaseRepository {


    val userFlow: Flow<UserRepoModel?>
    /**
     * @return true - if user has already its profile data filled (go to Main)
     * Otherwise user must fill profile data
     */
    suspend fun hasUser(): Boolean

    suspend fun getUser(): UserRepoModel?

    suspend fun refreshUser()

    suspend fun updateUser(user: UserRepoModel)

    suspend fun updateUserAvatar(avatar: File, onUploadProgress: (Float) -> Unit = { _ -> }): String

    suspend fun deleteUser()
}