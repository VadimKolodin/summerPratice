package com.company.deliveryservice.app;

import com.company.deliveryservice.entity.DeliveryArea;
import com.company.deliveryservice.entity.Restaurant;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private DataManager dataManager;

    public List<Restaurant> getAllRestaurant(){
        return dataManager.load(Restaurant.class).all().list();
    }

    public boolean isDeliveryAreaFree(DeliveryArea deliveryArea){
        return dataManager.load(Restaurant.class).query("e.delivery=?1",
                deliveryArea).list().isEmpty();
    }
}
