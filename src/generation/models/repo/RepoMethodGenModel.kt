package generation.models.repo

import generation.models.CodeGenerator
import generation.models.api.ApiMethodGenModel

/**
 *
 */
class RepoMethodGenModel(var name: String,
                         var comment: String,
                         val params: List<RepoMethodParam> = mutableListOf(),
                         val apiName:String,
                         val apiMethodGenModel: ApiMethodGenModel) : CodeGenerator {

    override fun generateCode(): String {
        return """
     /**
     * $comment
     */
    fun $name(${generateParamsSection()}): ${apiMethodGenModel.rxObservableType.name}<Unit> = //TODO:заменить на свою модель
            $apiName.${apiMethodGenModel.name}(${generateParamsSentToFun()})
            ${generateTransformSection()}
       """.trimIndent()
    }

    private fun generateParamsSection() = params.joinToString(",\n", transform = { param -> param.generateCode() })

    private fun generateParamsSentToFun() = params.joinToString(",", transform = { param -> param.name })

    private fun generateTransformSection() = if (apiMethodGenModel.isCollectionInResponse) ".transformCollection()" else  ".map { it.transform() }"
}