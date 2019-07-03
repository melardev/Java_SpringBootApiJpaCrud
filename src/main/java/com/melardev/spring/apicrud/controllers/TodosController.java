package com.melardev.spring.apicrud.controllers;

import com.melardev.spring.apicrud.entities.Todo;
import com.melardev.spring.apicrud.repositories.TodosRepository;
import com.melardev.spring.apicrud.responses.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/todos")
public class TodosController {

    @Autowired
    private TodosRepository todosRepository;

    @GetMapping
    public Iterable<Todo> index() {
        return this.todosRepository.fetchAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Todo> todo = this.todosRepository.fetchById(id);

        /*
                if (todo.isPresent())
            return new ResponseEntity<>(todo.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Todo(), HttpStatus.NOT_FOUND);
         */

        return todo.map(todo1 -> new ResponseEntity(todo1, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(new ErrorResponse("Not found"), HttpStatus.NOT_FOUND));

    }

    @GetMapping("/pending")
    public List<Todo> getNotCompletedTodos() {
        return this.todosRepository.findByHqlCompletedIs(false);
    }

    @GetMapping("/completed")
    public List<Todo> getCompletedTodos() {
        return todosRepository.findByHqlCompletedIs(true);
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
        return new ResponseEntity<>(todosRepository.save(todo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestBody Todo todoInput) {
        Optional<Todo> optionalTodo = todosRepository.fetchById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setTitle(todoInput.getTitle());

            String description = todoInput.getDescription();
            if (description != null)
                todo.setDescription(description);

            todo.setCompleted(todoInput.isCompleted());
            return ResponseEntity.ok(todosRepository.save(optionalTodo.get()));
        } else {
            return new ResponseEntity<>(new ErrorResponse("This todo does not exist"), HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Todo> todo = todosRepository.fetchAsProxy(id);
        if (todo.isPresent()) {
            todosRepository.delete(todo.get());
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(new ErrorResponse("This todo does not exist"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        todosRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
