package tree

import generation.models.MainGenerator
import generation.models.api.ResponseGenObj

class Response(val code: Int,
               val type: MediaType,
               val attributes: List<AttrField>? = null,
               val headers: List<Header>? = null,
               val body: Model? = null,
               val bodyAsModelRef: String? = null,
               val isCollection: Boolean = false) {

    fun createResponseObj(): ResponseGenObj? {
        return when {
            bodyAsModelRef != null -> ResponseGenObj(bodyAsModelRef)
            body != null -> {
                val newResponseModelName = "${MainGenerator.parsingModels.size}Obj"
                MainGenerator.parsingModels.add(Model(newResponseModelName, "",  body.fields))
                ResponseGenObj(newResponseModelName)
            }
            else -> null
        }
    }
}