package generation.models

import tree.DataTree
import tree.Model

object MainGenerator {
    val groupModels: List<GroupGenModel> = mutableListOf()
    val parsingModels: ArrayList<Model> = arrayListOf()

    fun createGenerationModel(dataTree: DataTree) = dataTree.groups.map { group -> GroupGenModel(group) }.toMutableList()
}