package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class JavaScriptFrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Represents the version of a framework
    @Size(max = 30)
    @NotEmpty
    @Column(nullable = false, length = 30)
    private String versionName;

    //Represents date when particular version is deprecated
    @Column
    private LocalDate deprecationDate;

    //Represents irrational hype level as per specification
    @Column
    @Min(0)
    @Max(1000)
    private long hypeLevel;

    @JoinColumn
    @ManyToOne
    @JsonBackReference
    private JavaScriptFramework framework;

    public JavaScriptFrameworkVersion() {
    }

    public JavaScriptFrameworkVersion(String version) {
        this.versionName = version;
        this.framework=framework;
    }

    public JavaScriptFramework getFramework() {
        return this.framework;
    }

    public void setFramework(JavaScriptFramework framework) {

        this.framework = framework;
    }

    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getVersion() {

        return this.versionName;
    }

    public void setVersion(String version) {

        this.versionName = version;
    }

    public long getHype() {

        return this.hypeLevel;
    }

    public void setHype(long hype) {

        this.hypeLevel = hype;
    }

    public LocalDate getDeprecationDate() {

        return this.deprecationDate;
    }

    public void setDeprecationDate(LocalDate date) {

        this.deprecationDate = date;
    }

    @Override
    public String toString() {
        StringBuilder fw=new StringBuilder();
        if(this.framework == null){
            fw.append("N/A");
        }else {
            fw.append(this.framework.getName());
        }

        return "JSF version [id=" + this.id + ", name=" + this.versionName + ", deprecation: " + this.deprecationDate + ", hype level: " + this.hypeLevel + ", Framework: " + fw.toString() + "]";
    }
}