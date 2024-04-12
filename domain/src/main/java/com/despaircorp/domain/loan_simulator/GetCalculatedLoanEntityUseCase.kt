package com.despaircorp.domain.loan_simulator

import com.despaircorp.domain.loan_simulator.model.LoanEntity
import javax.inject.Inject
import kotlin.math.pow

class GetCalculatedLoanEntityUseCase @Inject constructor(

) {
    suspend fun invoke(loanAmount: Int, downPayment: Int, termYears: Int): LoanEntity {
        val interestRate = 3.91
        val principal = loanAmount - downPayment
        val monthlyInterestRate = interestRate / 12 / 100
        val numberOfPayments = termYears * 12
        
        val monthlyRateFactor = (1 + monthlyInterestRate).pow(numberOfPayments.toDouble())
        val monthlyPayment =
            ((principal * monthlyInterestRate * monthlyRateFactor) / (monthlyRateFactor - 1)).toInt()
        
        val totalRepayment = monthlyPayment * numberOfPayments
        
        return LoanEntity(
            totalLoanAmount = loanAmount,
            downPayment = downPayment,
            loanTerm = termYears,
            monthlyPayment = monthlyPayment,
            fixedInterestRate = interestRate,
            totalRepayment = totalRepayment
        )
    }
}