package de.preis.inventory.controller;

import de.preis.inventory.data.PersistableInventoryEntity;
import de.preis.inventory.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class CrudController<T extends PersistableInventoryEntity<ID>, ID> {

    @Autowired
    private CrudService<T, ID> crudService;

    @GetMapping
    public List<T> getResource() {
        return crudService.getResources();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createResource(@RequestBody T resource) {
        crudService.createResource(resource);
    }

    @PutMapping("/{id}")
    public void updateResource(@PathVariable ID id, @RequestBody T resource) {
        crudService.updateResource(id, resource);
    }

    @DeleteMapping("/{id}")
    public void deleteResource(@PathVariable ID id) {
        crudService.deleteResource(id);
    }

}
