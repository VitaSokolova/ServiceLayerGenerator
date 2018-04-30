package tree

import common.RequestParamType

class UriParam(val name: String,
               val type: RequestParamType,
               val fieldType: String,
               val isRequired: Boolean,
               val defaultValue: String? = null,
               val comment: String? = null) {
}