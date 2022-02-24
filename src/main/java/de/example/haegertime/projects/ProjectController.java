package de.example.haegertime.projects;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path="api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER') or hasRole('EMPLOYEE')")
    public List<Project> findAll(){
        return projectService.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    public Project updateProject(@PathVariable long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER') or hasRole('EMPLOYEE')")
    public Project getById(@PathVariable long id) {
        return projectService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    public void deleteById(@PathVariable long id) {
        projectService.deleteById(id);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
//    public Project createNewProject(@RequestBody Project project) {
//        return projectService.createNewProject(project);
//    }
}
