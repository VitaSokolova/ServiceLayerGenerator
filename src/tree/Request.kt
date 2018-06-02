package tree

import generation.models.MainGenerator
import generation.models.api.RequestGenObj

class Request(type: MediaType,
              attributes: List<AttrField>? = null,
              headers: List<Header>? = null,
              body: Model? = null,
              bodyAsModelRef: String? = null,
              isCollection: Boolean = false) : HttpMessage(type, attributes, headers, body, bodyAsModelRef) {

    fun createRequestObj(): RequestGenObj? {
        return when {
            bodyAsModelRef != null -> RequestGenObj(bodyAsModelRef)
            body != null -> {
                val newResponseModelName = "${MainGenerator.parsingModels.size}Obj"
                MainGenerator.parsingModels.add(Model(newResponseModelName, "", body.fields))
                RequestGenObj(newResponseModelName)
            }
            else -> null
        }
    }
}