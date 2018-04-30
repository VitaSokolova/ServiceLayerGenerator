package generation.models.repo

import generation.models.CodeGenerator
import generation.models.api.ApiMethodGenModel

/**
 *
 */
class RepoMethodGenModel(var name: String,
                         var comment: String? = null,
                         val params: List<RepoMethodParam> = mutableListOf(),
                         val apiName: String,
                         val apiMethod: ApiMethodGenModel) : CodeGenerator {

    override fun generateCode(): String {
        return """
     /**
     * $comment
     ${generateParamsCommentsSection()}
     */
    fun $name(${generateParamsSection()})${generateReturnSection()} = //TODO:заменить на свою модель
            $apiName.${apiMethod.name}(${generateParamsSentToFun()})
            ${generateTransformSection()}
       """.trimIndent()
    }

    private fun generateParamsSection() = params.joinToString(",\n", transform = { param -> param.generateCode() })

    private fun generateParamsSentToFun() = params.joinToString(",", transform = { param -> param.name })

    private fun generateTransformSection() = if (apiMethod.isCollectionInResponse) ".transformCollection()" else ".map { it.transform() }"

    private fun generateParamsCommentsSection() = params.joinToString("\n", transform = { param -> param.generateCommentSection() })

    private fun generateReturnSection(): String {
        apiMethod.responseObj?.let {
            return ": ${apiMethod.rxObservableType.name}<$it>"
        }
        return ": Completable"
    }
}