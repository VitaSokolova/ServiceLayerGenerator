package controllers;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import generation.models.CodeGenerator;
import generation.models.MainGenerator;
import generation.models.api.ApiGenModel;
import generation.models.api.ApiMethodGenModel;
import generation.models.repo.RepoGenModel;
import generation.models.repo.RepoMethodGenModel;
import tree.MockDataTreeGenerator;
import tree.Model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Collection;
import java.util.List;

public class GenerationPreviewController {
    private JPanel rootPanel;
    private JButton minusBtn;
    private JButton plusBtn;
    private JTree tree;
    private JTextPane previewTextPane;
    private JButton nextButton;
    private JTextField nameTf;
    private JTextField commentTf;
    private JButton editParamsButton;

    private MainGenerator generator = MainGenerator.INSTANCE;

    private DocumentListener nameChangedListener;
    private DocumentListener commentChangedListener;

    public GenerationPreviewController() {
        JFrame frame = new JFrame("App");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        nextButton.addActionListener(e -> {
            showChooseDirectoryDialog();
            startGenerate();
        });
        generator.createGenerationModel(new MockDataTreeGenerator().generateMockTree());
        initTree(tree);
    }

    private void initTree(JTree tree) {
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(generator, true);
        //create the child nodes

        DefaultMutableTreeNode groupsNode = new DefaultMutableTreeNode("Groups", true);
        root.add(groupsNode);

        generator.getGroupModels().forEach(groupGenModel -> {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(groupGenModel, true);
            groupsNode.add(groupNode);

            DefaultMutableTreeNode apiNode = new DefaultMutableTreeNode(groupGenModel.getApiGenModel(), true);
            DefaultMutableTreeNode repositoryNode = new DefaultMutableTreeNode(groupGenModel.getRepoGenModel(), true);
            groupNode.add(apiNode);
            groupNode.add(repositoryNode);

            groupGenModel.getApiGenModel().getMethods().forEach(methodModel
                    -> apiNode.add(new DefaultMutableTreeNode(methodModel)));

            groupGenModel.getRepoGenModel().getRepoMethods().forEach(methodModel
                    -> repositoryNode.add(new DefaultMutableTreeNode(methodModel)));
        });

        DefaultMutableTreeNode modelsNode = new DefaultMutableTreeNode("Models", true);
        root.add(modelsNode);

        generator.getParsingModels().forEach(model -> {
            DefaultMutableTreeNode modelNode = new DefaultMutableTreeNode(model, true);
            modelsNode.add(modelNode);
        });

        tree.setModel(new DefaultTreeModel(root));
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                clearNameListener();
                clearCommentListener();

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (node == null) {
                    return;
                } else {
                    CodeGenerator leaf = (CodeGenerator) node.getUserObject();
                    previewTextPane.setText(leaf.generateCode());

                    if (leaf instanceof ApiMethodGenModel) {
                        ApiMethodGenModel apiMethod = (ApiMethodGenModel) leaf;
                        initEditPanelForApiMethod(apiMethod);
                    }
                    if (leaf instanceof RepoMethodGenModel) {
                        RepoMethodGenModel repoMethod = (RepoMethodGenModel) leaf;
                        initEditPanelForRepoMethod(repoMethod);
                    }
                    if (leaf instanceof ApiGenModel) {
                        ApiGenModel api = (ApiGenModel) leaf;
                        initEditPanelForApi(api);
                    }
                    if (leaf instanceof RepoGenModel) {
                        RepoGenModel repo = (RepoGenModel) leaf;
                        initEditPanelForRepo(repo);
                    }

                    if (leaf instanceof Model) {
                        Model model = (Model) leaf;
                        initEditPanelForModel(model);
                    }
                }
            }
        });
    }

    private void initEditPanelForModel(Model model) {
        nameTf.setText(model.getName());
        nameChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.setName(nameTf.getText());
                previewTextPane.setText(model.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        nameTf.getDocument().addDocumentListener(nameChangedListener);

        commentTf.setText(model.getComment());
        commentChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.setComment(nameTf.getText());
                previewTextPane.setText(model.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        commentTf.getDocument().addDocumentListener(nameChangedListener);
    }

    private void initEditPanelForApiMethod(ApiMethodGenModel apiMethod) {
        nameTf.setText(apiMethod.getName());
        nameChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apiMethod.setName(nameTf.getText());
                previewTextPane.setText(apiMethod.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        nameTf.getDocument().addDocumentListener(nameChangedListener);

        commentTf.setText(apiMethod.getComment());
        commentChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apiMethod.setComment(commentTf.getText());
                previewTextPane.setText(apiMethod.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        commentTf.getDocument().addDocumentListener(commentChangedListener);
    }

    private void initEditPanelForRepoMethod(RepoMethodGenModel repoMethodGenModel) {
        nameTf.setText(repoMethodGenModel.getName());
        nameChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repoMethodGenModel.setName(nameTf.getText());
                previewTextPane.setText(repoMethodGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        nameTf.getDocument().addDocumentListener(nameChangedListener);

        commentTf.setText(repoMethodGenModel.getComment());
        commentChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repoMethodGenModel.setComment(commentTf.getText());
                previewTextPane.setText(repoMethodGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        commentTf.getDocument().addDocumentListener(commentChangedListener);
    }

    private void initEditPanelForApi(ApiGenModel apiGenModel) {
        nameTf.setText(apiGenModel.getName());
        nameChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apiGenModel.setName(nameTf.getText());
                previewTextPane.setText(apiGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        nameTf.getDocument().addDocumentListener(nameChangedListener);

        commentTf.setText(apiGenModel.getComment());
        commentChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apiGenModel.setComment(commentTf.getText());
                previewTextPane.setText(apiGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        commentTf.getDocument().addDocumentListener(commentChangedListener);
    }

    private void initEditPanelForRepo(RepoGenModel repoGenModel) {
        nameTf.setText(repoGenModel.getName());
        nameChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repoGenModel.setName(nameTf.getText());
                previewTextPane.setText(repoGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        nameTf.getDocument().addDocumentListener(nameChangedListener);

        commentTf.setText(repoGenModel.getComment());
        commentChangedListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repoGenModel.setComment(commentTf.getText());
                previewTextPane.setText(repoGenModel.generateCode());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
        commentTf.getDocument().addDocumentListener(commentChangedListener);
    }

    private void clearNameListener() {
        nameTf.getDocument().removeDocumentListener(nameChangedListener);
        nameTf.setText("");
    }

    private void clearCommentListener() {
        commentTf.getDocument().removeDocumentListener(commentChangedListener);
        commentTf.setText("");
    }

    private void showChooseDirectoryDialog() {
        final Project defaultProject = ProjectManager.getInstance().getOpenProjects()[0]; //TODO: опасное место при нескольких проектах
        PackageChooserDialog dialog = new PackageChooserDialog("Choose generation directory", defaultProject);
        if (dialog.showAndGet()) {
            final PsiPackage aPackage = dialog.getSelectedPackage();
            if (aPackage != null) {
                generator.setTargetDirectory(aPackage.getDirectories()[0]); // еще подумать над этим
                startGenerate();
            }
        }
    }

    private void startGenerate() {
        final Project project = ProjectManager.getInstance().getOpenProjects()[0]; //TODO: опасное место при нескольких проектах
        generator.generateClasses(project);
}


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 3, new Insets(40, 40, 40, 40), -1, -1, true, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        minusBtn = new JButton();
        minusBtn.setText("-");
        panel1.add(minusBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusBtn = new JButton();
        plusBtn.setText("+");
        panel1.add(plusBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tree = new JTree();
        panel1.add(tree, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 161), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Name");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameTf = new JTextField();
        panel2.add(nameTf, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Comment");
        panel2.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentTf = new JTextField();
        panel2.add(commentTf, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Parameters");
        panel2.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editParamsButton = new JButton();
        editParamsButton.setText("Edit");
        panel2.add(editParamsButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        previewTextPane = new JTextPane();
        scrollPane1.setViewportView(previewTextPane);
        nextButton = new JButton();
        nextButton.setText("Далее");
        rootPanel.add(nextButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
