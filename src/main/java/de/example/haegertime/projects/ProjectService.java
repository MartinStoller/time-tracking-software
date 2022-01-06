package de.example.haegertime.projects;

import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService {
   private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Auflisten aller Projekte in der DB
     * @return Liste von Projekten
     */
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getById(long id) {
        if(!projectRepository.existsById(id)) {
            throw new ItemNotFoundException("Das Projekt mit der Id "+id+
                    " existiert nicht.");
        }
        return projectRepository.getById(id);
    }

    /**
     * Aktualisieren das Projekt, es geht hier um ein PatchMapping,
     * also das Projekt wird teil aktualisiert.
     * @param id Project-ID Nummer
     * @param fields Die Teile, die man ändern will
     * @return das aktualisierte Projekt
     */
    public Project updateProject(long id, Map<Object, Object> fields) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(!projectOptional.isPresent()) {
            throw new ItemNotFoundException("Das Projekt mit der Id"+id+
                    " existiert nicht.");
        }
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Project.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, projectOptional.get(), value);
        });
        Project updatedProject = projectRepository.save(projectOptional.get());
        return updatedProject;
    }
}

