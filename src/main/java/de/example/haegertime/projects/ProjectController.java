package de.example.haegertime.projects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }
    @GetMapping
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }
}
