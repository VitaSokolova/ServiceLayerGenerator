package generation.models.api

import common.MethodType
import common.RequestParamType
import generation.models.CodeGenerator
import generation.models.RequestParamGenModel

data class ApiMethodGenModel(val type: MethodType,
                             var name: String,
                             val url: String,
                             val params: MutableList<RequestParamGenModel> = arrayListOf(),
                             var requestObj: RequestGenObj?,
                             var responseObj: ResponseGenObj?,
                             val isCollectionInResponse: Boolean = false) : CodeGenerator {

    var rxObservableType: RxObservableType = RxObservableType.OBSERVABLE

    override fun generateCode(): String {
        return """
    @${type.name}($url)
    fun $name(${generateParamsSection()})${generateReturnParam()}
        """.trimIndent()
    }

    private fun generateParamsSection(): String {
        requestObj?.let {
            params.add(RequestParamGenModel(it.name.decapitalize(), RequestParamType.BODY, it.name.capitalize(), it.defaultValue))
        }
        return params.joinToString(",", transform = { param -> param.generateCode() })
    }

    private fun generateReturnParam(): String {
        responseObj?.let {
            return ": ${rxObservableType.name}<${it.name}>"
        }
        return ""
    }
}