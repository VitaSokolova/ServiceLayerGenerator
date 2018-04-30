package tree

import common.MethodType
import generation.models.RequestParamGenModel
import generation.models.api.ApiMethodGenModel

class Method(val name: String,
             val type: MethodType,
             val uri: String,
             val params: List<UriParam>,
             val response: Response,
             val request: Request,
             val comment:String?= null) {

    fun convertToApiMethod(): ApiMethodGenModel {
        return ApiMethodGenModel(type,
                name,
                uri,
                comment,
                params.map { param ->
                    RequestParamGenModel(param.name,
                            param.type,
                            param.fieldType,
                            param.defaultValue,
                            param.comment)
                }.toMutableList(),
                request.createRequestObj(),
                response.createResponseObj(),
                response.isCollection)
    }
}