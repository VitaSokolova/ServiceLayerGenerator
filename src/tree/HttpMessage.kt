package tree

open class HttpMessage(val type: MediaType,
                       val attributes: List<AttrField>? = null,
                       val headers: List<Header>? = null,
                       val body: Model? = null,
                       val bodyAsModelRef: String? = null,
                       val isCollection: Boolean = false)