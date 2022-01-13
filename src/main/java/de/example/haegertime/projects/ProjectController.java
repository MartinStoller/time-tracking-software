package de.example.haegertime.projects;

import org.springframework.http.HttpStatus;
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

    @PatchMapping("/update/{id}")
    public void updateProject(@PathVariable long id, @RequestBody Map<Object, Object> fields) {
        //The Map
        Project project = projectService.updateProject(id, fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getByIdProject(@PathVariable long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }
}
