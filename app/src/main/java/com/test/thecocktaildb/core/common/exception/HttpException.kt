package com.test.thecocktaildb.core.common.exception

class HttpException {

    companion object HttpCode {
        const val BAD_REQUEST = 400
        const val UNAUTHORIZED  = 401
        const val TOKEN_REQUIRED  = 10009
    }
}