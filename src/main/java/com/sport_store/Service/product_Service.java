package com.sport_store.Service;

import com.sport_store.Repository.customer_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class product_Service {
    private customer_Repository customer_Repository;

}
