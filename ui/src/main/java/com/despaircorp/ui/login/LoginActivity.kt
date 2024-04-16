package com.despaircorp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.despaircorp.ui.databinding.ActivityLoginBinding
import com.despaircorp.ui.login.agent.AgentDropDownAdapter
import com.despaircorp.ui.login.agent.AgentDropDownListener
import com.despaircorp.ui.main.main_activity.MainActivity
import com.despaircorp.ui.main.main_activity.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), AgentDropDownListener {
    private val binding by viewBinding { ActivityLoginBinding.inflate(it) }
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        
        val agentDropDownAdapter = AgentDropDownAdapter(this)
        binding.activityLoginDropdownAgent.adapter = agentDropDownAdapter
        
        viewModel.viewActionLiveData.observe(this) {
            when (val action = it.getContentIfNotHandled()) {
                LoginViewAction.AlreadyLoggedInAgent -> {
                    startActivity(MainActivity.navigate(this))
                    finish()
                }
                
                is LoginViewAction.Error -> Toast.makeText(
                    this,
                    getString(action.message),
                    Toast.LENGTH_SHORT
                ).show()
                
                LoginViewAction.SuccessLogin -> {
                    startActivity(MainActivity.navigate(this))
                    finish()
                }
                
                else -> Unit
            }
        }
        
        viewModel.viewStateLiveData.observe(this) {
            agentDropDownAdapter.submitList(it.agentDropDownViewStateItems)
        }
        
        
    }
    
    override fun onAgentClick(agentId: Int) {
        viewModel.onAgentClicked(agentId)
    }
    
    companion object {
        
        fun navigate(context: Context) = Intent(
            context,
            LoginActivity::class.java
        )
    }
}