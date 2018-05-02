package generation.models

import tree.DataTree
import tree.Model

public object MainGenerator:CodeGenerator {
    override fun generateCode(): String {
        return "Mew-Mew"
    }

    var projectName: String = ""
    var projectUrl: String = ""
    var groupModels: MutableList<GroupGenModel> = mutableListOf()
    var parsingModels: MutableList<Model> = arrayListOf()

    fun createGenerationModel(dataTree: DataTree) {
        parsingModels = dataTree.models
        groupModels = dataTree.groups.map { group -> GroupGenModel(group) }.toMutableList()
        projectName = dataTree.name
        projectUrl = dataTree.url
    }
    override fun toString() = projectName

    fun getCodeForLeaf(groupName:String?, methodName:String?, repoOrApiName:String? ): String {
      return "mew"
    }

    //TODO:сгенерировать enum-ы
}