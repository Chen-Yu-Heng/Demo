package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TB_COINDESK;

@Repository
public interface TB_COINDESKRepository extends JpaRepository<TB_COINDESK, String> {

}
