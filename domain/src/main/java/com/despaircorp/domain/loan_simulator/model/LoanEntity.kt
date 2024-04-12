package com.despaircorp.domain.loan_simulator.model

data class LoanEntity(
    val totalLoanAmount: Int,
    val loanTerm: Int,
    val downPayment: Int,
    val monthlyPayment: Int,
    val fixedInterestRate: Double,
    val totalRepayment: Int,
)
