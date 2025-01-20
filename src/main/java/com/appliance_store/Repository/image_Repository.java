package com.appliance_store.Repository;

import com.appliance_store.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface image_Repository extends JpaRepository<Image, Integer>
{

}
