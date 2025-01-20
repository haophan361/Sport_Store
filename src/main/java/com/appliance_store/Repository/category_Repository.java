package com.appliance_store.Repository;

import com.appliance_store.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface category_Repository extends JpaRepository<Category,String>
{

}
