package com.despaircorp.ui.main.loan_simulator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.despaircorp.ui.databinding.ActivityLoanSimulatorBinding
import com.despaircorp.ui.main.main_activity.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanSimulatorActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityLoanSimulatorBinding.inflate(it) }
    private val viewModel: LoanSimulatorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.activityLoanSimulatorToolbarRoot)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        binding.activityLoanSimulatorToolbarRoot.setNavigationOnClickListener {
            finish()
        }
        
        binding.activityLoanSimulatorTextInputEditTextTotalLoanAmount.addTextChangedListener {
            viewModel.onLoanAmountTextChanged(it.toString())
        }
        
        binding.activityLoanSimulatorTextInputEditTextDownPayment.addTextChangedListener {
            viewModel.onDownPaymentTextChanged(it.toString())
        }
        
        binding.activityLoanSimulatorSliderLoanTerm.addOnChangeListener { _, value, _ ->
            viewModel.onLoanTermValueChanged(value)
        }
        
        viewModel.viewState.observe(this) {
            binding.activityLoanSimulatorTextViewTotalLoanPriceValue.text = it.totalLoanAmount
            binding.activityLoanSimulatorTextViewLoanTermValue.text = it.loanTerm
            binding.activityLoanSimulatorTextViewMonthlyPaymentValue.text = it.monthlyPayment
            binding.activityLoanSimulatorTextViewInterestRateValue.text = it.fixedInterestRate
            binding.activityLoanSimulatorTextViewTotalRepayment.text = it.totalRepayment
            binding.activityLoanSimulatorTextInputEditTextTotalLoanAmount.setText(it.loanAmountInputValue)
            binding.activityLoanSimulatorTextInputEditTextDownPayment.setText(it.downPaymentInputValue)
            binding.activityLoanSimulatorSliderLoanTerm.setValues(it.loanTermSliderValue)
        }
    }
    
    companion object {
        
        fun navigate(context: Context) = Intent(
            context,
            LoanSimulatorActivity::class.java
        )
    }
}