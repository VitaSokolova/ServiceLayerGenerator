package controllers;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import controllers.SelectSourceController;

public class GeneratorToolbarAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        new GenerationPreviewController();
    }
}
