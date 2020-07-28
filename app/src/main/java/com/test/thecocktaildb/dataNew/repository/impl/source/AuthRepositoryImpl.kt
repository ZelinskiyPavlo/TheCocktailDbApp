package com.test.thecocktaildb.dataNew.repository.impl.source

import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import com.test.thecocktaildb.dataNew.local.source.TokenLocalSource
import com.test.thecocktaildb.dataNew.network.source.AuthNetSource
import com.test.thecocktaildb.dataNew.network.source.UserNetSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.UserRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.source.AuthRepository
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
}