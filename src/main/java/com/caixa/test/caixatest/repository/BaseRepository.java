package com.caixa.test.caixatest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base repository package for JPA repositories
 * Extend JpaRepository in repository interfaces
 */
public interface BaseRepository extends JpaRepository<Object, Long> {
    // Repository interfaces will be placed here
}
