package tree

open class Field(val name: String,
                 val type: String,
                 val defaultValue: String? = null,
                 val comment: String? = null,
                 isRequired: Boolean = true,
                 isCollection:Boolean = false) {
}