package tree

import generation.models.CodeGenerator


class Model(val name: String,
            val comment: String = "",
            val fields: List<Field>) : CodeGenerator {


    override fun generateCode(): String {
        return """
/**
 * ${generateComment()}
 */
data class ${name.capitalize()}(
        ${generateParamSection()}) {
}
        """.trimIndent()
    }

    private fun generateComment() = if (comment.isNotEmpty()) comment else "TODO: Добавить комментарий"

    private fun generateParamSection() = fields.joinToString(",\n", transform = { param -> param.generateCode() })

    override fun toString() = "$name"

}