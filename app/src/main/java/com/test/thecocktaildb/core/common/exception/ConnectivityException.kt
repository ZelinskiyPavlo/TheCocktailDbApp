package com.test.thecocktaildb.core.common.exception

import java.io.IOException

sealed class ConnectivityException(details: String): IOException(details)

class NoInternetConnectionException(
    details: String = "no internet connection") : ConnectivityException(details)