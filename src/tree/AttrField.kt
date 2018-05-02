package tree

import common.MethodType

class AttrField(name: String,
                type: MethodType,
                val fieldType: String,
                defaultValue: String? = null,
                comment: String? = null,
                isRequired: Boolean) : Field(name, fieldType, defaultValue, comment, isRequired) {
}