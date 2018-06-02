package generation.models

import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import tree.DataTree
import tree.Model

public object MainGenerator : CodeGenerator {
    override fun generateCode(): String {
        return "Mew-Mew"
    }

    var projectName: String = ""
    var projectUrl: String = ""
    var targetDirectory: PsiDirectory? = null
    var groupModels: MutableList<GroupGenModel> = mutableListOf()
    var parsingModels: MutableList<Model> = arrayListOf()

    fun createGenerationModel(dataTree: DataTree) {
        parsingModels = dataTree.models
        groupModels = dataTree.groups.map { group -> GroupGenModel(group) }.toMutableList()
        projectName = dataTree.name
        projectUrl = dataTree.url
    }

    override fun toString() = projectName

    fun generateClasses(project: Project) {
        val factory = PsiFileFactory.getInstance(project)
        targetDirectory?.let {
           val modelsDir = it.createSubdirectory("models")
            parsingModels.forEach { genModel ->
                val file = factory.createFileFromText("${genModel.name}.kt", Language.findLanguageByID("kotlin")
                        ?: Language.ANY, genModel.generateCode())
                modelsDir.add(file)
            }
            val groupDir = it.createSubdirectory("groups")
            groupModels.forEach { genModel ->
                val groupNameDirectory: PsiDirectory = groupDir.createSubdirectory(genModel.group.name.toLowerCase())
                val repoFile = factory.createFileFromText("${genModel.repoGenModel.name}.kt", Language.findLanguageByID("kotlin")
                        ?: Language.ANY, genModel.repoGenModel.generateCode())
                val apiFile = factory.createFileFromText("${genModel.apiGenModel.name}.kt", Language.findLanguageByID("kotlin")
                        ?: Language.ANY, genModel.apiGenModel.generateCode())
                groupNameDirectory.add(repoFile)
                groupNameDirectory.add(apiFile)
            }
        }
    }

    //TODO:сгенерировать enum-ы
}