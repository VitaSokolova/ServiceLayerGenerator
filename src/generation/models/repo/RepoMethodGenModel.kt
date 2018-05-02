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
     * ${comment ?: "TODO: добавить комментарий"}
     ${generateParamsCommentsSection()}
     */
    fun $name(${generateParamsSection()})${generateReturnSection()} =
            $apiName.${apiMethod.name}(${generateParamsSentToFun()})
            ${generateTransformSection()}
       """
    }

    override fun toString() = name

    private fun generateParamsSection() = params.joinToString(",\n", transform = { param -> param.generateCode() })

    private fun generateParamsSentToFun() = params.joinToString(",", transform = { param -> param.name })

    private fun generateTransformSection() = if (apiMethod.isCollectionInResponse) ".transformCollection()" else ".map { it.transform() }"

    private fun generateParamsCommentsSection(): String {
        return if (params.isNotEmpty()) {
            params.joinToString("\n", transform = { param -> param.generateCommentSection() })
        } else ""
    }

    private fun generateReturnSection(): String {
        apiMethod.responseObj?.let {
            return if (apiMethod.isCollectionInResponse) {
                ": ${apiMethod.rxObservableType.observableName}<List<${it.name}>>"
            } else {
                ": ${apiMethod.rxObservableType.observableName}<${it.name}>"
            }
        }
        return ": Completable"
    }
}