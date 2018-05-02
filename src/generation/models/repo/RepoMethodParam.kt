package generation.models.repo

import generation.models.CodeGenerator


class RepoMethodParam(var name: String,
                      var type: String,
                      var comment: String = "") : CodeGenerator {

    override fun generateCode() = "$name: $type"

    fun generateCommentSection(): String {
        return """* @param: $name $comment"""
    }
}