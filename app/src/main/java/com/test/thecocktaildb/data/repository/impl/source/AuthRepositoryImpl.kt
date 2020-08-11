package com.test.thecocktaildb.data.repository.impl.source

import com.test.thecocktaildb.data.db.source.UserDbSource
import com.test.thecocktaildb.data.local.source.TokenLocalSource
import com.test.thecocktaildb.data.network.source.AuthNetSource
import com.test.thecocktaildb.data.network.source.UserNetSource
import com.test.thecocktaildb.data.repository.impl.mapper.UserRepoModelMapper
import com.test.thecocktaildb.data.repository.source.AuthRepository
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
                tokenLocalSource.token = it

                //refresh user
                userNetSource.getUser()
                    .run(userModelMapper::mapNetToDb)
                    .run { userDbSource.saveUser(this) }

                tokenLocalSource.token != null
            }
    }

    override suspend fun signUp(name: String, lastName: String, email: String, password: String) {
        return authNetSource
            .signUp(name, lastName, email, password)
            .let {
                tokenLocalSource.token = it

                //refresh user
                userNetSource.getUser()
                    .run(userModelMapper::mapNetToDb)
                    .run { userDbSource.saveUser(this) }

                tokenLocalSource.token != null
            }
    }
}