package generation.models

import generation.models.api.ApiGenModel
import generation.models.repo.RepoGenModel
import tree.Group

class GroupGenModel(val group: Group) : CodeGenerator {

    val apiGenModel: ApiGenModel
    val repoGenModel: RepoGenModel

    init {
        this.apiGenModel = ApiGenModel("${group.name.capitalize()}Api",
                group.comment,
                group.methods.map { method -> method.convertToApiMethod(group.params) })
        this.repoGenModel = RepoGenModel("${group.name.capitalize()}Repository",
                group.comment,
                apiGenModel)
    }

    override fun generateCode(): String {
 return "group"
    }

    override fun toString() = group.name
}