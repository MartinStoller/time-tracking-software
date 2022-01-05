package de.example.haegertime.projects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


    @PatchMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable long id, @RequestBody Map<Object, Object> fields) {
        Project project = projectService.updateProject(id, fields);
        return ResponseEntity.ok(project);
    }


}
