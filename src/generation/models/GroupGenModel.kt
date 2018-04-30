package generation.models

import generation.models.api.ApiGenModel
import generation.models.repo.RepoGenModel
import tree.Group
import tree.Model

class GroupGenModel(val group: Group) : CodeGenerator {

    val apiGenModel: ApiGenModel
    val repoGenModel: RepoGenModel

    init {
        this.apiGenModel = ApiGenModel("${group.name.capitalize()}Api",
                group.comment,
                group.methods.map { method -> method.convertToApiMethod() })
        this.repoGenModel = RepoGenModel(apiInterface = apiGenModel)
    }

    override fun generateCode(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}