package com.appviewx.repos.primarydb;

import com.appviewx.model.primarydb.FileLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileLineJPARepository extends JpaRepository<FileLine, String>, JpaSpecificationExecutor<FileLine> {
}
