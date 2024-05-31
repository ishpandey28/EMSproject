package com.ish.capstone.repository;


import com.ish.capstone.dto.ProjectDTO;
import com.ish.capstone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    @Query("SELECT new com.ish.capstone.dto.ProjectDTO(p.projectId, p.projectName) FROM Project p")
    List<ProjectDTO> findAllProjectDTOs();
}
