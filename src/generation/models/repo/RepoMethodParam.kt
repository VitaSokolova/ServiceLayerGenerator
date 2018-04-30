package generation.models.repo

import generation.models.CodeGenerator


class RepoMethodParam(var name: String,
                      var type: String) : CodeGenerator {
    override fun generateCode(): String {
        return "$name: $type"
    }

}