package com.despaircorp.domain.splash_screen

import assertk.assertThat
import assertk.assertions.isTrue
import com.despaircorp.domain.utils.TestCoroutineRule
import org.junit.Rule
import org.junit.Test

class CountDownSplashScreenUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val useCase = CountDownSplashScreenUseCase(
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )
    
    @Test
    fun `nominal case - count down`() = testCoroutineRule.runTest {
        val result = useCase.invoke()
        
        assertThat(result).isTrue()
        
    }
}