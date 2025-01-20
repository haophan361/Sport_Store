package com.appliance_store.Repository;

import com.appliance_store.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface brand_Repository extends JpaRepository<Brand, String>
{

}
