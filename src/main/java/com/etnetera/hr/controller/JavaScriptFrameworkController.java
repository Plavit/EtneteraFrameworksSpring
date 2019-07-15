package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

    private final JavaScriptFrameworkRepository repository;
    private final JavaScriptFrameworkVersionRepository versionRepository;


    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository, JavaScriptFrameworkVersionRepository versionRepository) {
        this.repository = repository;
        this.versionRepository = versionRepository;
    }

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> frameworks() {
        return repository.findAll();
    }


    @PostMapping({"/add", "/update"})
    public ResponseEntity<?> saveJavascriptFramework(@RequestBody @Valid JavaScriptFramework framework, BindingResult result) {
        if (!result.hasErrors())
            return ResponseEntity.ok(repository.save(framework));
        Errors errors = new Errors();
        List<ValidationError> errorList = result.getFieldErrors()
                .stream()
                .map(e -> new ValidationError(e.getField(), e.getCode()))
                .collect(Collectors.toList());

        errors.setErrors(errorList);
        return errorList.isEmpty() ? ResponseEntity.ok().body(errors) : ResponseEntity.badRequest().body(errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteFramework(@PathVariable Long id) {

        ValidationError error = new ValidationError();
        if (id != null)
            repository.deleteById(id);
        else {
            error.setField("id");
            error.setMessage("is null");
        }

        Errors errors = new Errors();
        errors.setErrors(Collections.singletonList(error));
        return error.getField() == null ? ResponseEntity.ok().body(errors) : ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/addVersion/{id}")
    public ResponseEntity addVersion(@RequestBody @Valid JavaScriptFrameworkVersion ver, @PathVariable Long id, BindingResult result) throws MethodArgumentNotValidException {
        Optional<JavaScriptFramework> javaScriptFramework = repository.findById(id);
        if (!result.hasErrors()) {
            ver.setFramework(javaScriptFramework.get());
            versionRepository.save(ver);
            javaScriptFramework.get().addVersion(ver);
            repository.save(javaScriptFramework.get());
        }else {
            Errors errors = new Errors();
            List<ValidationError> errorList = result.getFieldErrors()
                    .stream()
                    .map(e -> new ValidationError(e.getField(), e.getCode()))
                    .collect(Collectors.toList());

            errors.setErrors(errorList);
            return errorList.isEmpty() ? ResponseEntity.ok().body(errors) : ResponseEntity.badRequest().body(errors);
        }

        return null;
    }

    @GetMapping("/findByName/{name}")
    public JavaScriptFramework searchFramework(@PathVariable String name) {
        return repository.findJavaScriptFrameworkByName(name);
    }


}
