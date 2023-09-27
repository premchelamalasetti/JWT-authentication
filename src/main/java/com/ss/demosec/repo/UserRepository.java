package com.ss.demosec.repo;

import java.util.Optional;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.demosec.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	Optional<User> findByEmail(String email);
}
