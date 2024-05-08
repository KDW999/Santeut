package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildPostRepository extends JpaRepository<GuildPostEntity, Integer> {
}