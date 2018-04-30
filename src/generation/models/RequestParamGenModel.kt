package generation.models

import common.RequestParamType

class RequestParamGenModel(var paramName: String,
                           val type: RequestParamType,
                           val fieldType: String,
                           var defaultValue: String? = null,
                           var comment: String? = null) : CodeGenerator {

    var name: String = ""

    override fun generateCode(): String {
        return when (type) {
            RequestParamType.QUERY -> """@Query("$paramName") $name:$fieldType ${codeForDefaultValue()})""" //  @Query("title") title: String? = null,
            RequestParamType.PATH -> """@Path("$paramName") $name:$fieldType ${codeForDefaultValue()})"""//  @Path("id") photoId: Long?,
            RequestParamType.PART -> """@Part("$paramName") $name:$fieldType ${codeForDefaultValue()})""" //  @Part("place_id") photoId: Long?,
            RequestParamType.BODY -> "@Body $name:$fieldType ${codeForDefaultValue()})"  //@Body photoRequest: PhotoRequest
        }
    }

    private fun codeForDefaultValue() = if (!defaultValue.isNullOrEmpty()) "= $defaultValue" else ""
}