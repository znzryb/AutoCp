package com.github.pushpavel.autocp.config

import com.github.pushpavel.autocp.common.helpers.pathString
import com.github.pushpavel.autocp.database.SolutionFiles
import com.intellij.execution.ExecutionTargetManager
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

/**
 * Implementation class for creating [AutoCpConfig] from context
 */
class AutoCpConfigProducer : LazyRunConfigurationProducer<AutoCpConfig>() {

    /**
     * Sets up [AutoCpConfig] if the file in this context is associated with a Problem
     */
    private val log = Logger.getInstance(AutoCpConfigProducer::class.java)

    override fun setupConfigurationFromContext(
        configuration: AutoCpConfig,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        log.info("=== AutoCp: setupConfigurationFromContext called ===")
        val file = context.location?.virtualFile
        val solutionPath = file?.pathString ?: return false
        val solutionFiles = SolutionFiles.getInstance(context.project)

        log.info("AutoCp: File path = $solutionPath")
        log.info("AutoCp: File extension = ${file.extension}")

        if (solutionPath !in solutionFiles) {
            log.info("AutoCp: Returning FALSE - not in database")
            return false
        }

        configuration.solutionFilePath = solutionPath

        val suggestedName = configuration.suggestedName()
        if (suggestedName != null)
            configuration.name = suggestedName

        // 新增：记录配置详情
        log.warn("AutoCp: Configuration created successfully")
        log.warn("AutoCp: Config name = ${configuration.name}")
        log.warn("AutoCp: Config type = ${configuration.type.displayName}")
        log.warn("AutoCp: Config factory = ${configuration.factory}")
        log.warn("AutoCp: Config ID = ${configuration.id}")
        log.warn("AutoCp: Config class = ${configuration.javaClass.name}")
        
        // 测试 canRunOn 是否在这里被调用
        try {
            val targetManager = ExecutionTargetManager.getInstance(context.project)
            val activeTarget = targetManager.activeTarget
            log.warn("AutoCp: Current active ExecutionTarget = ${activeTarget.displayName} (id=${activeTarget.id})")
            log.warn("AutoCp: Testing canRunOn with active target...")
            val canRun = configuration.canRunOn(activeTarget)
            log.warn("AutoCp: canRunOn(activeTarget) returned: $canRun")
            
            // 如果当前 target 不是默认的，尝试切换到默认 target
            if (activeTarget.id != "default" && activeTarget.javaClass.simpleName != "DefaultExecutionTarget") {
                log.warn("AutoCp: Current target is not default, checking for default target...")
                val defaultTarget = targetManager.targets.find { it.id == "default" || it.javaClass.simpleName == "DefaultExecutionTarget" }
                if (defaultTarget != null) {
                    log.warn("AutoCp: Found default target: ${defaultTarget.displayName}")
                } else {
                    log.warn("AutoCp: No default target found in available targets: ${targetManager.targets.map { "${it.displayName}(${it.id})" }}")
                }
            }
        } catch (e: Exception) {
            log.error("AutoCp: Error testing canRunOn", e)
        }

        log.info("AutoCp: Returning TRUE")
        return true
    }

    /**
     * Used to reuse existing AutoCpConfig created from this Context
     */
    override fun isConfigurationFromContext(configuration: AutoCpConfig, context: ConfigurationContext): Boolean {
        log.info("=== AutoCp: isConfigurationFromContext called ===")
        val path = context.location?.virtualFile?.pathString ?: return false
        val result = configuration.solutionFilePath == path
        log.info("AutoCp: Config path = ${configuration.solutionFilePath}")
        log.info("AutoCp: Context path = $path")
        log.info("AutoCp: isConfigurationFromContext returning = $result")

        return configuration.solutionFilePath == path
    }


    override fun getConfigurationFactory() = AutoCpConfigType.factory

}