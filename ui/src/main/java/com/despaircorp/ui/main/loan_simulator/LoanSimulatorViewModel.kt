package com.despaircorp.ui.main.loan_simulator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.despaircorp.domain.loan_simulator.GetCalculatedLoanEntityUseCase
import com.despaircorp.domain.loan_simulator.model.LoanEntity
import com.despaircorp.shared.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    private val getCalculatedLoanEntityUseCase: GetCalculatedLoanEntityUseCase,
    private val application: Application
) : ViewModel() {
    
    private val loanAmountMutableStateFlow = MutableStateFlow("")
    private val downPaymentMutableStateFlow = MutableStateFlow("")
    private val loanTermMutableStateFlow = MutableStateFlow(5f)
    
    val viewState = liveData<LoanSimulatorViewState> {
        combine(
            loanAmountMutableStateFlow,
            downPaymentMutableStateFlow,
            loanTermMutableStateFlow,
        ) { loanAmount, downPayment, loanTerm ->
            val loanEntity = if (loanAmount.isNotEmpty() && downPayment.isNotEmpty()) {
                getCalculatedLoanEntityUseCase.invoke(
                    loanAmount.toInt(),
                    downPayment.toInt(),
                    loanTerm.toInt()
                )
                
            } else {
                LoanEntity(
                    totalLoanAmount = 0,
                    loanTerm = 0,
                    downPayment = 0,
                    monthlyPayment = 0,
                    fixedInterestRate = 0.0,
                    totalRepayment = 0
                )
            }
            
            emit(
                LoanSimulatorViewState(
                    loanAmountInputValue = loanAmount,
                    downPaymentInputValue = downPayment,
                    loanTermSliderValue = loanTerm,
                    totalLoanAmount = "${application.getString(R.string.loan_total_amount)} $${
                        formatToCorrectNumber(
                            loanEntity.totalLoanAmount.toString()
                        )
                    }",
                    loanTerm = "${application.getString(R.string.loan_term)} ${loanEntity.loanTerm} ${
                        application.getString(
                            R.string.years
                        )
                    }",
                    monthlyPayment = "${application.getString(R.string.monthly_payment)} $${
                        formatToCorrectNumber(
                            loanEntity.monthlyPayment.toString()
                        )
                    }",
                    fixedInterestRate = "${application.getString(R.string.fixed_interest_rate)} ${loanEntity.fixedInterestRate}%",
                    totalRepayment = "${application.getString(R.string.total_repayment)} $${
                        formatToCorrectNumber(
                            loanEntity.totalRepayment.toString()
                        )
                    }",
                )
            )
            
        }.collect()
    }
    
    fun onLoanAmountTextChanged(valueInput: String) {
        loanAmountMutableStateFlow.value = valueInput
    }
    
    fun onDownPaymentTextChanged(valueInput: String) {
        downPaymentMutableStateFlow.value = valueInput
    }
    
    fun onLoanTermValueChanged(valueInput: Float) {
        loanTermMutableStateFlow.value = valueInput
    }
    
    private fun formatToCorrectNumber(price: String): String {
        val format =
            NumberFormat.getNumberInstance(Locale.US)
        return format.format(price.toInt())
    }
}
