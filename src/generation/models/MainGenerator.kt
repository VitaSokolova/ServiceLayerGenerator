package generation.models

import tree.DataTree
import tree.Model

object MainGenerator {
    val groupModels: MutableList<GroupGenModel> = mutableListOf()
    var parsingModels: MutableList<Model> = arrayListOf()

    fun createGenerationModel(dataTree: DataTree) {
        parsingModels = dataTree.models
        dataTree.groups.map { group -> GroupGenModel(group) }.toMutableList()
    }

    //TODO:сгенерировать enum-ы
}