import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.project

fun DependencyHandler.kapt(vararg list: Dependencies.Libs) {
    list.forEach { dependency ->
        add("kapt", dependency.path)
    }
}

private fun DependencyHandler.addDependencies(configuration: String, vararg list: Dependencies) {
    list.forEach { dependency ->
        addDependency(configuration, dependency)
    }
}

private fun DependencyHandler.addDependency(configuration: String, dependency: Dependencies) {
    fun Dependencies.notation(): Any? {
        return when (this) {
            is Dependencies.Libs.LibSet -> null
            is Dependencies.Libs -> path
            else -> project(path)
        }
    }
    dependency.notation()?.let { notation ->
        when (dependency.configuration) {
            null -> add(configuration, notation)
            else -> addDependencyTo(
                dependencies = this,
                configuration = configuration,
                dependencyNotation = notation,
                configurationAction = dependency.configuration
            )
        }
    }
    val subDependencies = when (dependency) {
        is Dependencies.Libs.LibSet -> dependency.libs
        else -> listOf<Dependencies>()
    }
    subDependencies.forEach { subDependency ->
        addDependency(configuration, subDependency)
    }
}


fun DependencyHandler.androidTestImplementation(vararg list: Dependencies) {
    addDependencies("androidTestImplementation", *list)
}

fun DependencyHandler.testImplementation(vararg list: Dependencies) {
    addDependencies("testImplementation", *list)
}

fun DependencyHandler.implementation(vararg list: Dependencies) {
    addDependencies("implementation", *list)
}

fun DependencyHandler.implementation(list: Iterable<Dependencies>) {
    list.forEach { dependency ->
        addDependency("implementation", dependency)
    }
}

fun DependencyHandler.debugImplementation(vararg list: Dependencies) {
    addDependencies("debugImplementation", *list)
}

fun DependencyHandler.devImplementation(vararg list: Dependencies) {
    addDependencies("devImplementation", *list)
}

fun DependencyHandler.api(vararg list: Dependencies) {
    addDependencies("api", *list)
}

