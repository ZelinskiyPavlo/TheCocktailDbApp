package com.test.impl.source

import com.test.database.source.UserDbSource
import com.test.impl.mapper.UserRepoModelMapper
import com.test.local.source.TokenLocalSource
import com.test.network.source.AuthNetSource
import com.test.network.source.UserNetSource
import com.test.repository.source.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authNetSource: AuthNetSource,
    private val userNetSource: UserNetSource,
    private val userDbSource: UserDbSource,
    private val userModelMapper: UserRepoModelMapper,
    private val tokenLocalSource: TokenLocalSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return authNetSource.signIn(email, password)
            .let {
                tokenLocalSource.authToken = it

                //refresh user
                userNetSource.getUser()
                    .run(userModelMapper::mapNetToDb)
                    .run { userDbSource.saveUser(this) }

                tokenLocalSource.authToken != null
            }
    }

    override suspend fun signUp(name: String, lastName: String, email: String, password: String) {
        return authNetSource
            .signUp(name, lastName, email, password)
            .let {
                tokenLocalSource.authToken = it

                //refresh user
                userNetSource.getUser()
                    .run(userModelMapper::mapNetToDb)
                    .run { userDbSource.saveUser(this) }

                tokenLocalSource.authToken != null
            }
    }
}