package tree

import common.MethodType

class Field(val name: String,
            val type: MethodType,
            val defaultValue: String? = null,
            val comment: String? = null,
            val isRequired: Boolean) {
}