package generation.models.repo

import generation.models.CodeGenerator
import generation.models.api.ApiGenModel
import generation.models.api.ApiMethodGenModel

class RepoGenModel(var name: String = "",
                   var comment: String = "",
                   val apiInterface: ApiGenModel) : CodeGenerator {

    val repoMethods: List<RepoMethodGenModel>

    init {
        repoMethods = apiInterface.methods.map { apiMethod -> createRepoMethod(apiMethod) }
    }

    override fun generateCode(): String {
        return """
/**
 * $comment
 */
@PerApplication
class $name @Inject constructor(
        private val ${apiInterface.name.decapitalize()}: ${apiInterface.name.capitalize()}) {

        ${generateMethods()}

        }
       """.trimIndent()
    }

    private fun generateMethods() = repoMethods.joinToString("\n\n", transform = { method -> method.generateCode() })

    private fun createRepoMethod(apiMethod: ApiMethodGenModel): RepoMethodGenModel {
        return RepoMethodGenModel(apiMethod.name.decapitalize(),
                apiMethod.comment,
                apiMethod.params.map { apiParam -> RepoMethodParam(apiParam.name, apiParam.fieldType, apiParam.comment) }.toMutableList(),
                apiInterface.name.decapitalize(),
                apiMethod)
    }

    override fun toString() = name
}