package com.despaircorp.ui.main.loan_simulator

data class LoanSimulatorViewState(
    val loanAmountInputValue: String,
    val downPaymentInputValue: String,
    val loanTermSliderValue: Float,
    val totalLoanAmount: String,
    val loanTerm: String,
    val monthlyPayment: String,
    val fixedInterestRate: String,
    val totalRepayment: String
)