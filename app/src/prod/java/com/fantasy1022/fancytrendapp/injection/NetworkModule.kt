package com.fantasy1022.fancytrendapp.injection

import com.fantasy1022.fancytrendapp.common.Constant
import com.fantasy1022.fancytrendapp.data.TrendRepository
import com.fantasy1022.fancytrendapp.data.TrendRepositoryImpl
import com.fantasy1022.fancytrendapp.data.remote.FancyTrendRestService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by fantasy_apple on 2017/6/25.
 */

@Module
object NetworkModule {
    private const val BASE_URL = Constant.GOOGLE_TREND_BASE_URL

    @Provides
    fun provideTrendRepo(googleTrendRestService: FancyTrendRestService): TrendRepository {
        return TrendRepositoryImpl(googleTrendRestService)
    }

    @Provides
    fun provideGoogleTrendRestService(retrofit: Retrofit): FancyTrendRestService {
        return retrofit.create(FancyTrendRestService::class.java)
    }

    @Provides
    fun getRetrofitInstanceForCoroutine(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .build()
    }

    @Provides
    fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}
