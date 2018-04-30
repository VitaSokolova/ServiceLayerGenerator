package generation.models.repo

import generation.models.CodeGenerator
import generation.models.api.ApiGenModel

class RepoGenModel(var name: String = "",
                   var comment: String = "",
                   val apiInterface: ApiGenModel,
                   val repoMethods: List<RepoMethodGenModel> = mutableListOf()) : CodeGenerator {

    override fun generateCode(): String {
        return """
/**
 * $comment
 */
@PerApplication
class $name @Inject constructor(
        private val ${apiInterface.name.toLowerCase()}: ${apiInterface.name.capitalize()}) {

        ${generateMethods()}
        }
       """.trimIndent()
    }

    private fun generateMethods() = repoMethods.joinToString("\n\n", transform = { method -> method.generateCode() })
}