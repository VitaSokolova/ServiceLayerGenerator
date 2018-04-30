package tree

class DataTree(val name: String,
               val url: String,
               val groups: MutableList<Group> = mutableListOf(),
               val models: MutableList<Model> = mutableListOf(),
               val enums: MutableList<EnumModel> = mutableListOf()) {
}