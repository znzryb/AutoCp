package com.github.pushpavel.autocp.config

import com.intellij.execution.ExecutionTarget
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.runners.GenericProgramRunner
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

//    override fun canRun(executorId: String, profile: RunProfile): Boolean {
//        LOG.warn("AutoCp Debug: ProgramRunner.canRun called with executorId: '$executorId', profile: ${profile.javaClass.simpleName}")
//        LOG.warn("AutoCp Debug: DefaultDebugExecutor.EXECUTOR_ID = '${DefaultDebugExecutor.EXECUTOR_ID}'")
//        LOG.warn("AutoCp Debug: DefaultRunExecutor.EXECUTOR_ID = '${DefaultRunExecutor.EXECUTOR_ID}'")
//        LOG.warn("AutoCp Debug: profile is AutoCpConfig = ${profile is AutoCpConfig}")
//
//        val isDebugExecutor = executorId == DefaultDebugExecutor.EXECUTOR_ID
//        val isRunExecutor = executorId == DefaultRunExecutor.EXECUTOR_ID
//        val isAutoConfig = profile is AutoCpConfig
//
//        LOG.warn("AutoCp Debug: isDebugExecutor = $isDebugExecutor")
//        LOG.warn("AutoCp Debug: isRunExecutor = $isRunExecutor")
//        LOG.warn("AutoCp Debug: isAutoConfig = $isAutoConfig")
//
//        val result = (isDebugExecutor || isRunExecutor) && isAutoConfig
//        LOG.warn("AutoCp Debug: ProgramRunner.canRun returning: $result")
//        return result
//    }
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
}