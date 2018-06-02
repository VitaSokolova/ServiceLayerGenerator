package tree

import generation.models.MainGenerator
import generation.models.api.ResponseGenObj

class Response(val code: Int,
               type: MediaType,
               attributes: List<AttrField>? = null,
               headers: List<Header>? = null,
               body: Model? = null,
               bodyAsModelRef: String? = null,
               isCollection: Boolean = false) : HttpMessage(type, attributes, headers, body, bodyAsModelRef, isCollection) {

    fun createResponseObj(): ResponseGenObj? {
        return when {
            bodyAsModelRef != null -> ResponseGenObj(bodyAsModelRef)
            body != null -> {
                val newResponseModelName = "${MainGenerator.parsingModels.size}Obj"
                MainGenerator.parsingModels.add(Model(newResponseModelName, "", body.fields))
                ResponseGenObj(newResponseModelName)
            }
            else -> null
        }
    }
}