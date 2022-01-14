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
    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project getById(long id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Das Projekt mit Id "+id
                + " nicht in der DB")
        );
    }


    public Project updateProject(long id, Project project) {
        /**
         * Aktualisieren das Projekt, es geht hier um ein PatchMapping,
         * also das Projekt wird teil aktualisiert.
         * @param id Project-ID Nummer
         * @param fields Die Teile, die man ändern will
         * @return das aktualisierte Projekt
         */
        Project updateProject = projectRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Das Projekt mit der Id "+id+
                        " nicht in der DB")
        );
        updateProject.setTitle(project.getTitle());
        updateProject.setStart(project.getStart());
        updateProject.setEnd(project.getEnd());
        projectRepository.save(updateProject);
        return updateProject;
    }

    public boolean existsProjectByIdAndCustomerId(Long customerId, Long projectId) {
        if(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


}

