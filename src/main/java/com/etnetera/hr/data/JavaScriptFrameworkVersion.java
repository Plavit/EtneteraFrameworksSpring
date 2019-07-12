package com.etnetera.hr.data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class JavaScriptFrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    private String versionName;

    @ManyToOne(cascade = CascadeType.ALL)
    private JavaScriptFramework framework;

    //Represents date when particular version is deprecated
    private LocalDate deprecationDate;

    //Represents irrational hype level as per specification
    private long hypeLevel;

    public JavaScriptFrameworkVersion(String version) {
        this.versionName = version;
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
}