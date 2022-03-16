import org.gradle.api.Project
import java.util.*

inline fun <reified T> innerObjects(): Iterable<T> {
    return T::class.nestedClasses.map { it.objectInstance as T }
}

fun Project.properties(fileName: String): Properties? {
    val file = file(fileName)
    if (!file.exists())
        return null
    val props = Properties()
    props.load(file.inputStream())
    return props
}
