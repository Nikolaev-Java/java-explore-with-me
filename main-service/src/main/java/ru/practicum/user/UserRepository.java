package ru.practicum.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    @Query("""
                select user from User user
                where ?1 is null or user.id in ?1
            """)
    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}
