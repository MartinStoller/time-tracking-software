package de.example.haegertime.projects;

import de.example.haegertime.advice.ItemAlreadyExistsException;
import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Project updateProject = projectRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Das Projekt mit der Id "+id+
                        " nicht in der DB")
        );
        Optional<Project> projectOptional = projectRepository.findProjectByTitle(project.getTitle());
        if(!projectOptional.isPresent()) {
            updateProject.setTitle(project.getTitle());
            updateProject.setStart(project.getStart());
            updateProject.setEnd(project.getEnd());
            projectRepository.save(updateProject);
            return updateProject;
        } else {
            throw new ItemAlreadyExistsException("Ein Projekt mit Title "+project.getTitle()
            +" ist vorhanden in der DB");
        }
    }


    public boolean existsProjectByIdAndCustomerId(Long customerId, Long projectId) {
        return projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent();
    }


    public void deleteById(long id) {
        if(projectRepository.findById(id).isPresent()) {
            projectRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Das Projekt mit Id "+id+" nicht in der DB");
        }
    }
}

