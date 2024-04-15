package com.despaircorp.domain.loan_simulator

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.loan_simulator.model.LoanEntity
import com.despaircorp.domain.utils.TestCoroutineRule
import org.junit.Rule
import org.junit.Test

class GetCalculatedLoanEntityUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val useCase = GetCalculatedLoanEntityUseCase()
    
    companion object {
        private const val DEFAULT_LOAN_AMOUNT = 250_000
        private const val DEFAULT_DOWN_PAYMENT = 50_000
        private const val DEFAULT_TERM_YEAR = 25
        private const val DEFAULT_MONTHLY_PAYMENT = 1_045
        private const val DEFAULT_FIXED_INTEREST_RATE = 3.91
        private const val DEFAULT_TOTAL_REPAYMENT = 313_500
    }
    
    @Test
    fun `nominal case - calculate the entity`() = testCoroutineRule.runTest {
        val result = useCase.invoke(
            loanAmount = DEFAULT_LOAN_AMOUNT,
            downPayment = DEFAULT_DOWN_PAYMENT,
            termYears = DEFAULT_TERM_YEAR
        )
        
        assertThat(result).isEqualTo(
            LoanEntity(
                totalLoanAmount = DEFAULT_LOAN_AMOUNT,
                loanTerm = DEFAULT_TERM_YEAR,
                downPayment = DEFAULT_DOWN_PAYMENT,
                monthlyPayment = DEFAULT_MONTHLY_PAYMENT,
                fixedInterestRate = DEFAULT_FIXED_INTEREST_RATE,
                totalRepayment = DEFAULT_TOTAL_REPAYMENT
            )
        )
        
    }
}