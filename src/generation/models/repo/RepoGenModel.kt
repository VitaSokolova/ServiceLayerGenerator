package generation.models.repo

import generation.models.CodeGenerator
import generation.models.api.ApiGenModel
import generation.models.api.ApiMethodGenModel

class RepoGenModel(var name: String = "",
                   var comment: String = "",
                   val apiInterface: ApiGenModel) : CodeGenerator {

    val repoMethods: List<RepoMethodGenModel> = arrayListOf()

    init {
        apiInterface.methods.forEach { apiMethod -> createRepoMethod(apiMethod) }
    }

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

    private fun createRepoMethod(apiMethod: ApiMethodGenModel): RepoMethodGenModel {
        return RepoMethodGenModel(apiMethod.name,
                apiMethod.comment,
                apiMethod.params.map { apiParam -> RepoMethodParam(apiParam.name, apiParam.fieldType, apiParam.comment) }.toMutableList(),
                apiInterface.name,
                apiMethod)
    }

}