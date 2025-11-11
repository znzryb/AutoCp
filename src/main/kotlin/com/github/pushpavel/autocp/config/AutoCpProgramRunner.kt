package com.github.pushpavel.autocp.config

import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.GenericProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.diagnostic.Logger

class AutoCpProgramRunner : GenericProgramRunner<RunnerSettings>() {

    companion object {
        private val LOG = Logger.getInstance(AutoCpProgramRunner::class.java)
    }
    
    init {
        LOG.warn("AutoCp Debug: AutoCpProgramRunner initialized")
    }
    
    override fun getRunnerId(): String {
        return "AutoCpProgramRunner"
    }
    
    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        LOG.warn("AutoCp Debug: ========== ProgramRunner.canRun() CALLED ==========")
        LOG.warn("AutoCp Debug: ProgramRunner.canRun called with executorId: '$executorId', profile: ${profile.javaClass.simpleName}")

        val isAutoConfig = profile is AutoCpConfig
        LOG.warn("AutoCp Debug: profile is AutoCpConfig = $isAutoConfig")
        
        if (isAutoConfig) {
            val config = profile as AutoCpConfig
            LOG.warn("AutoCp Debug: Config name: '${config.name}'")
            LOG.warn("AutoCp Debug: Config solutionFilePath: '${config.solutionFilePath}'")
        }

        // Support AutoCpConfig for all executors
        val result = isAutoConfig
        LOG.warn("AutoCp Debug: ProgramRunner.canRun returning: $result")
        LOG.warn("AutoCp Debug: =====================================================")
        return result
    }
    
    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        LOG.warn("AutoCp Debug: ========== doExecute() CALLED ==========")
        LOG.warn("AutoCp Debug: Executor = ${environment.executor.id}")
        LOG.warn("AutoCp Debug: ExecutionTarget = ${environment.executionTarget?.displayName}")
        
        // Execute the state and get the result
        val executionResult: ExecutionResult = state.execute(environment.executor, this)
            ?: throw RuntimeException("Failed to execute AutoCp state")
        
        LOG.warn("AutoCp Debug: ExecutionResult obtained successfully")
        
        // Create and return the RunContentDescriptor
        val descriptor = RunContentDescriptor(
            executionResult.executionConsole,
            executionResult.processHandler,
            executionResult.executionConsole?.component ?: throw RuntimeException("Console component is null"),
            environment.runProfile.name
        )
        
        LOG.warn("AutoCp Debug: RunContentDescriptor created successfully")
        LOG.warn("AutoCp Debug: ==========================================")
        
        return descriptor
    }
}
