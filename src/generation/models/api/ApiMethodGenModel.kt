package generation.models.api

import common.MethodType
import common.RequestParamType
import generation.models.CodeGenerator
import generation.models.RequestParamGenModel

data class ApiMethodGenModel(val type: MethodType,
                             var name: String,
                             val url: String,
                             var comment: String? = null,
                             val params: MutableList<RequestParamGenModel> = arrayListOf(),
                             var requestObj: RequestGenObj?,
                             var responseObj: ResponseGenObj?,
                             val isCollectionInResponse: Boolean = false) : CodeGenerator {

    var rxObservableType: RxObservableType = RxObservableType.OBSERVABLE

    override fun generateCode(): String {
        return """
    ${generateCommentSection()}
    @${type.name}("$url")
    fun $name(${generateParamsSection()})${generateReturnParam()}
        """.trimIndent()
    }

    private fun generateParamsSection(): String {
        requestObj?.let {
            params.clear()
            params.add(RequestParamGenModel(it.name.decapitalize(), RequestParamType.BODY, it.name.capitalize(), it.defaultValue))
        }
        return params.joinToString(",\n", transform = { param -> param.generateCode() })
    }

    private fun generateReturnParam(): String {
        responseObj?.let {
            return if (isCollectionInResponse) {
                ": ${rxObservableType.observableName}<List<${it.name}>>"
            } else {
                ": ${rxObservableType.observableName}<${it.name}>"
            }
        }
        return ": Completable"
    }

    private fun generateCommentSection(): String {
        return if (comment.isNullOrEmpty()) {
            """
    /**
    * TODO: обавить комментарий
    */"""
        } else {
            """
    /**
    *$comment
    */"""
        }
    }

    override fun toString() = name
}