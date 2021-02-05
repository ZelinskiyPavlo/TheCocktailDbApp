package com.test.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import com.test.common.constant.BaseUrl
import com.test.dagger.DiConstant
import com.test.impl.deserializer.BooleanDeserializer
import com.test.impl.deserializer.Iso8601DateDeserializer
import com.test.impl.deserializer.model.CocktailNetModelDeserializer
import com.test.impl.extension.deserializeType
import com.test.impl.interceptor.*
import com.test.impl.service.AuthApiService
import com.test.impl.service.CocktailApiService
import com.test.impl.service.UserApiService
import com.test.impl.service.UserUploadService
import com.test.impl.source.AuthNetSourceImpl
import com.test.impl.source.CocktailNetSourceImpl
import com.test.impl.source.UserNetSourceImpl
import com.test.impl.source.UserUploadNetSourceImpl
import com.test.network.model.cocktail.CocktailNetModel
import com.test.network.source.AuthNetSource
import com.test.network.source.CocktailNetSource
import com.test.network.source.UserNetSource
import com.test.network.source.UserUploadNetSource
import com.test.repository.source.TokenRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    private val baseGsonBuilder: GsonBuilder
        get() = GsonBuilder()
            .registerTypeAdapter(deserializeType<Boolean>(), BooleanDeserializer(false))
            .registerTypeAdapter(deserializeType<Date>(), Iso8601DateDeserializer())
            .setPrettyPrinting()
            .serializeNulls()

    @Singleton
    @Provides
    @Named(DiConstant.AUTH_RETROFIT)
    fun provideAuthRetrofit(
        tokenRepository: TokenRepository,
        app: Application
    ): Retrofit {
        val okHttpClientBuilder = provideOkHttpClientBuilder()

        okHttpClientBuilder.addInterceptor(TokenInterceptor { tokenRepository.token })
        okHttpClientBuilder.addInterceptor(AppVersionInterceptor())
        okHttpClientBuilder.addInterceptor(PlatformInterceptor())
        okHttpClientBuilder.addInterceptor(PlatformVersionInterceptor())
        okHttpClientBuilder.addInterceptor(
            NetworkConnectionInterceptor(
                app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            )
        )

        configureOkHttpInterceptors(okHttpClientBuilder)

        with(Retrofit.Builder()) {
            addConverterFactory(GsonConverterFactory.create(baseGsonBuilder.create()))
            client(okHttpClientBuilder.build())
            baseUrl(BaseUrl.Auth)

            return build()
        }
    }

    @Singleton
    @Provides
    @Named(DiConstant.COCKTAIL_RETROFIT)
    fun provideCocktailRetrofit(app: Application): Retrofit {
        val okHttpClientBuilder = provideOkHttpClientBuilder()

        okHttpClientBuilder.addInterceptor(
            NetworkConnectionInterceptor(
                app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            )
        )

        configureOkHttpInterceptors(okHttpClientBuilder)

        with(Retrofit.Builder()) {
            addConverterFactory(
                GsonConverterFactory.create(
                    baseGsonBuilder.registerTypeAdapter(
                        deserializeType<CocktailNetModel>(), CocktailNetModelDeserializer()
                    ).create()
                )
            )
            client(okHttpClientBuilder.build())
            baseUrl(BaseUrl.COCKTAIL)

            return build()
        }
    }

    @Singleton
    @Provides
    @Named(DiConstant.UPLOAD_RETROFIT)
    fun provideUploadRetrofit(
        tokenRepository: TokenRepository,
        app: Application
    ): Retrofit {
        val okHttpClientBuilder =
            provideOkHttpClientBuilder(writeTimeoutSeconds = TimeUnit.MINUTES.toSeconds(5L))

        okHttpClientBuilder.addInterceptor(TokenInterceptor { tokenRepository.token })
        okHttpClientBuilder.addInterceptor(AppVersionInterceptor())
        okHttpClientBuilder.addInterceptor(PlatformInterceptor())
        okHttpClientBuilder.addInterceptor(PlatformVersionInterceptor())
        okHttpClientBuilder.addInterceptor(
            NetworkConnectionInterceptor(
                app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            )
        )

        configureOkHttpInterceptors(okHttpClientBuilder)

        with(Retrofit.Builder()) {
            addConverterFactory(GsonConverterFactory.create(baseGsonBuilder.create()))
            client(okHttpClientBuilder.build())
            baseUrl(BaseUrl.Auth)

            return build()
        }
    }


    @Singleton
    @Provides
    fun provideAuthApiService(
        @Named(DiConstant.AUTH_RETROFIT) retrofit: Retrofit
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideCocktailApiService(
        @Named(DiConstant.COCKTAIL_RETROFIT) retrofit: Retrofit
    ): CocktailApiService = retrofit.create(CocktailApiService::class.java)

    @Singleton
    @Provides
    fun provideUserApiService(
        @Named(DiConstant.AUTH_RETROFIT) retrofit: Retrofit
    ): UserApiService = retrofit.create(UserApiService::class.java)

    @Singleton
    @Provides
    fun provideUserUploadService(
        @Named(DiConstant.UPLOAD_RETROFIT) retrofit: Retrofit
    ): UserUploadService = retrofit.create(UserUploadService::class.java)

    @Singleton
    @Provides
    fun provideAuthNetSource(
        authApiService: AuthApiService
    ): AuthNetSource = AuthNetSourceImpl(authApiService)

    @Singleton
    @Provides
    fun provideCocktailNetSource(
        cocktailApiService: CocktailApiService
    ): CocktailNetSource = CocktailNetSourceImpl(cocktailApiService)

    @Singleton
    @Provides
    fun provideUserNetSource(
        userApiService: UserApiService
    ): UserNetSource = UserNetSourceImpl(userApiService)

    @Singleton
    @Provides
    fun provideUserUploadNetSource(
        app: Application,
        userUploadService: UserUploadService
    ): UserUploadNetSource = UserUploadNetSourceImpl(app, userUploadService)

    private fun provideOkHttpClientBuilder(
        readTimeoutSeconds: Long = 120,
        writeTimeoutSeconds: Long = 120
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        try {
            val trustAllCerts = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<X509Certificate?> = emptyArray()
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS"/*TlsVersion.TLS_1_3.javaName*//*"SSL"*/)
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)

        } catch (e: Exception) { // should never happen
            e.printStackTrace()
        }

        return builder
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
    }

    private fun configureOkHttpInterceptors(
        okHttpClientBuilder: OkHttpClient.Builder
    ) {

        // OkHttp Logger
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addInterceptor(logger)

        // Postman Mock
        okHttpClientBuilder.addInterceptor(PostmanMockInterceptor())

//        // Gander I think applicationContext will also suit
//        okHttpClientBuilder.addInterceptor(
//            GanderInterceptor(context).apply {
//                showNotification(true)
//            }
//        )
    }
}