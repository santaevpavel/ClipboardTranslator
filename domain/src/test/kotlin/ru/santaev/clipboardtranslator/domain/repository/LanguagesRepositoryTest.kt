package ru.santaev.clipboardtranslator.domain.repository

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.repository.ILanguageRepository
import com.example.santaev.domain.repository.ILanguageRepository.LanguagesState.*
import com.example.santaev.domain.repository.LanguageRepository
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class LanguagesRepositoryTest {

    @Test
    fun testRequestLanguagesError() {
        val languagesDao = Mockito.mock(ILanguageDao::class.java)
        val apiService = Mockito.mock(IApiService::class.java)

        val error: Single<LanguagesResponseDto> = Single
                .error<LanguagesResponseDto>(Exception())
                .delaySubscription<LanguagesResponseDto>(100, TimeUnit.MILLISECONDS)
        Mockito
                .`when`(apiService.getLanguages())
                .thenReturn(error)

        val repository: ILanguageRepository = LanguageRepository(languagesDao, apiService)

        val testObserver = repository
                .requestLanguages()
                .test()

        testObserver.awaitCount(1)
        testObserver.assertValue(ERROR)
    }

    @Test
    fun testRequestLanguagesSuccess() {
        val languagesDao = Mockito.mock(ILanguageDao::class.java)
        val apiService = Mockito.mock(IApiService::class.java)

        val success: Single<LanguagesResponseDto> = Single
                .just(LanguagesResponseDto(listOf(), listOf()))
                .delaySubscription<LanguagesResponseDto>(100, TimeUnit.MILLISECONDS)
        Mockito
                .`when`(apiService.getLanguages())
                .thenReturn(success)

        val repository: ILanguageRepository = LanguageRepository(languagesDao, apiService)

        val testObserver = repository
                .requestLanguages()
                .test()

        testObserver.awaitCount(1)
        testObserver.assertValue(SUCCESS)
    }

    @Test
    fun testRequestLanguagesDoubleCall() {
        val languagesDao = Mockito.mock(ILanguageDao::class.java)
        val apiService = Mockito.mock(IApiService::class.java)

        val error: Single<LanguagesResponseDto> = Single
                .error<LanguagesResponseDto>(Exception())
                .delaySubscription<LanguagesResponseDto>(100, TimeUnit.MILLISECONDS)
        val success: Single<LanguagesResponseDto> = Single
                .just(LanguagesResponseDto(listOf(), listOf()))
                .delaySubscription<LanguagesResponseDto>(100, TimeUnit.MILLISECONDS)
        Mockito
                .`when`(apiService.getLanguages())
                .thenReturn(error)

        val repository: ILanguageRepository = LanguageRepository(languagesDao, apiService)

        val testObserver = repository
                .requestLanguages()
                .test()

        testObserver.awaitCount(1)

        // Retry to load languages
        Mockito
                .`when`(apiService.getLanguages())
                .thenReturn(success)

        repository
                .requestLanguages()
                .subscribe()

        testObserver.awaitCount(3)
        testObserver.assertValues(ERROR, LOADING, SUCCESS)
    }
}