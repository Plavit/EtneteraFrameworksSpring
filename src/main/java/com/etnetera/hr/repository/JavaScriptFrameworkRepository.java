package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring data repository interface used for accessing the data in database.
 * 
 * @author Etnetera
 *
 */

public interface JavaScriptFrameworkRepository extends CrudRepository<JavaScriptFramework, Long> {
    JavaScriptFramework findJavaScriptFrameworkById(long Id);
    JavaScriptFramework findJavaScriptFrameworkByName(String name);
}
