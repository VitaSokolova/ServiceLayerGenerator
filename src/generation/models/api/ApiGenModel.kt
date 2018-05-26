package generation.models.api

import generation.models.CodeGenerator

data class ApiGenModel(var name: String = "",
                       var comment: String = "",
                       val methods: List<ApiMethodGenModel> = mutableListOf()) : CodeGenerator {
    init {
        name.decapitalize()
    }

    override fun generateCode(): String {
        return """
/**
 *$comment
 */
interface $name {

${generateMethods()}
}""".trimIndent()
    }

    private fun generateMethods() = methods.joinToString("\n\n", transform = { method -> method.generateCode() })

    override fun toString() = name
}