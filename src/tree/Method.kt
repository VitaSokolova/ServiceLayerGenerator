package tree

import common.MethodType
import generation.models.RequestParamGenModel
import generation.models.api.ApiMethodGenModel

class Method(val name: String,
             val type: MethodType,
             val uri: String,
             val params: MutableList<UriParam> = arrayListOf(),
             val response: Response? = null,
             val request: Request? = null,
             val comment: String? = null) {

    fun convertToApiMethod(parentParams: List<UriParam> = arrayListOf()): ApiMethodGenModel {
        params.addAll(parentParams)
        return ApiMethodGenModel(type,
                name,
                uri,
                comment,
                params.map { param ->
                    RequestParamGenModel(param.name,
                            param.type,
                            param.fieldType,
                            param.defaultValue,
                            param.comment ?: "")
                }.toMutableList(),
                request?.createRequestObj(),
                response?.createResponseObj(),
                response?.isCollection ?: false)
    }
}