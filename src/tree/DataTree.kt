package tree

class DataTree(val name: String,
               val url: String,
               val groups: List<Group> = mutableListOf(),
               val models: List<Model> = mutableListOf(),
               val enums: List<EnumModel> = mutableListOf()) {
}