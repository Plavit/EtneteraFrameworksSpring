package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 */
@Entity
public class JavaScriptFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 30)
    @NotEmpty
    @Column(nullable = false, length = 30)
    private String name;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "framework", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JavaScriptFrameworkVersion> versions;

    public JavaScriptFramework() {
    }

    public JavaScriptFramework(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addVersion(JavaScriptFrameworkVersion ver){
        this.versions.add(ver);
    }
    public void addVersions(List<JavaScriptFrameworkVersion> vers){
        this.versions.addAll(vers);
    }

    public List<JavaScriptFrameworkVersion> getVersions(){
        return this.versions;
    }

    @Override
    public String toString() {


        return "JavaScriptFramework [id=" + this.id + ", name=" + this.name + "]";
    }

}
