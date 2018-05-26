package tree

import generation.models.CodeGenerator

open class Field(val name: String,
                 val type: String,
                 val defaultValue: String? = null,
                 val comment: String? = null,
                 val isRequired: Boolean = true,
                 val isCollection: Boolean = false) : CodeGenerator {

    override fun generateCode(): String {
        return """@SerializedName("$name") val $name: ${generateType()}${generateDefaultSection()} """
    }

    private fun generateDefaultSection():String {
        return if (!defaultValue.isNullOrEmpty()) {
            if (type == "String") {
                """ = "$defaultValue" """
            } else {
                " = $defaultValue"
            }
        } else {
            return if (!isRequired) {
                "? = null"
            } else {
                ""
            }
        }
    }

    private fun generateType(): String = if (isCollection) "List<$type>" else "$type"
}