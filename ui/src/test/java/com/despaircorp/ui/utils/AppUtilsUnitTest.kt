package com.despaircorp.ui.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AppUtilsUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    @Test
    fun `nominal case - date should return wanted format`() = testCoroutineRule.runTest {
        val result = getDate()
        
        assertThat(result).isEqualTo(
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }
}