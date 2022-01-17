package de.example.haegertime.projects;

import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent();
    }


}

