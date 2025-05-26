// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.ls.imports.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceData(
    val modules: List<ModuleData>,
    val libraries: List<LibraryData>,
    val sdks: List<SdkData>,
    val kotlinSettings: List<KotlinSettingsData>,
)

@Serializable
data class ModuleData(
    val name: String,
    val type: String? = "JAVA_MODULE",
    val dependencies: List<DependencyData>,
    val contentRoots: List<ContentRootData>,
    val facets: List<FacetData>,
)

@Serializable
sealed class DependencyData {

    @Serializable
    @SerialName("module")
    data class Module(
        val name: String,
        val scope: DependencyDataScope,
        val isExported: Boolean = false,
    ) : DependencyData()

    @Serializable
    @SerialName("library")
    data class Library(
        val name: String,
        val scope: DependencyDataScope,
        val isExported: Boolean = false,
    ) : DependencyData()

    @Serializable
    @SerialName("inheritedSdk")
    data object InheritedSdk : DependencyData()

    @Serializable
    @SerialName("moduleSource")
    data object ModuleSource : DependencyData()

    @Serializable
    @SerialName("sdk")
    data class Sdk(val name: String, val kind: String) : DependencyData()
}


@Serializable
data class ContentRootData(
    val path: String,
    val excludedPatterns: List<String>,
    val excludedUrls: List<String>,
    val sourceRoots: List<SourceRootData>
)

@Serializable
data class SourceRootData(
    val path: String,
    val type: String,
)

@Serializable
data class FacetData(
    val name: String,
    val type: String,
    val configuration: XmlElement? = null,
    val underlyingFacet: FacetData? = null
)

@Serializable
data class LibraryData(
    val name: String,
    val level: String = "project",
    val module: String? = null,
    val type: String?,
    val roots: List<LibraryRootData>,
    val excludedRoots: List<String> = emptyList(),
    val properties: XmlElement? = null,
)

enum class InclusionOptions {
    @SerialName("root_itself")
    ROOT_ITSELF,
    @SerialName("archives_under_root")
    ARCHIVES_UNDER_ROOT,
    @SerialName("archives_under_root_recursively")
    ARCHIVES_UNDER_ROOT_RECURSIVELY
}

@Serializable
data class LibraryRootData(
    val path: String,
    val type: String = "CLASSES",
    val inclusionOptions: InclusionOptions = InclusionOptions.ROOT_ITSELF
)


@Serializable
data class SdkData(
    val name: String,
    val type: String,
    val version: String?,
    val homePath: String?,
    val roots: List<SdkRootData>? = null, // Can be calculated from homePath when omitted
    val additionalData: String,
)

@Serializable
data class SdkRootData(
    val url: String,
    val type: String,
)


@Serializable
data class KotlinSettingsData(
    val name: String,
    val sourceRoots: List<String>,
    val configFileItems: List<ConfigFileItemData>,
    val module: String,

    // trivial parameters (String, Boolean)
    val useProjectSettings: Boolean,
    val implementedModuleNames: List<String>,
    val dependsOnModuleNames: List<String>,
    val additionalVisibleModuleNames: Set<String>,
    val productionOutputPath: String?,
    val testOutputPath: String?,
    val sourceSetNames: List<String>,
    val isTestModule: Boolean,
    val externalProjectId: String,
    val isHmppEnabled: Boolean,

    val pureKotlinSourceFolders: List<String>,

    //semi-trivial parameters (enums)
    val kind: KotlinModuleKind,

    //non-trivial parameters
    val compilerArguments: String?,

    val additionalArguments: String?,
    val scriptTemplates: String?,
    val scriptTemplatesClasspath: String?,
    val copyJsLibraryFiles: Boolean = false,
    val outputDirectoryForJsLibraryFiles: String?,

    val targetPlatform: String?,
    val externalSystemRunTasks: List<String>,
    val version: Int,
    val flushNeeded: Boolean
) {
    enum class KotlinModuleKind {
        @SerialName("default")
        DEFAULT,
        @SerialName("source_set_holder")
        SOURCE_SET_HOLDER,
        @SerialName("compilation_and_source_set_holder")
        COMPILATION_AND_SOURCE_SET_HOLDER
    }
}

@Serializable
data class ConfigFileItemData(val id: String, val url: String)

enum class DependencyDataScope {
    @SerialName("compile")
    COMPILE,

    @SerialName("test")
    TEST,

    @SerialName("runtime")
    RUNTIME,

    @SerialName("provided")
    PROVIDED
}

@Serializable
data class XmlElement(
    val tag: String = "",
    val attributes: Map<String, String> = emptyMap(),
    val children: List<XmlElement> = emptyList(),
    val text: String? = null
)