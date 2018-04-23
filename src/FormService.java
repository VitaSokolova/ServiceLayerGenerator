import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class FormService {
    public FormService(Project project) {
    }

    public static FormService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, FormService.class);
    }
}
