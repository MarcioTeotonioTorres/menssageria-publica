package com.menssageria.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.menssageria.api.domain.model.MessageTemplate;

public interface TemplateRepository extends JpaRepository<MessageTemplate, Long> {

}
