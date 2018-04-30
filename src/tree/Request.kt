package tree

import generation.models.MainGenerator
import generation.models.api.RequestGenObj

class Request(val type: MediaType,
              val attributes: List<Field>? = null,
              val headers: List<Header>? = null,
              val body: Model? = null,
              val bodyAsModelRef:String? = null) {

    fun createRequestObj(): RequestGenObj? {
        return when {
            bodyAsModelRef != null -> RequestGenObj(bodyAsModelRef)
            body != null -> {
                val newResponseModelName = "${MainGenerator.parsingModels.size}Obj"
                MainGenerator.parsingModels.add(Model(newResponseModelName, body.fields))
                RequestGenObj(newResponseModelName)
            }
            else -> null
        }
    }
}