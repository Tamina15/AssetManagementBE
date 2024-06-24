package com.nashtech.rookies.assetmanagement.repository;

import com.nashtech.rookies.assetmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByPrefix(String prefix);
    boolean existsByName(String name);
    Category findCategoryByName(String name);
}
