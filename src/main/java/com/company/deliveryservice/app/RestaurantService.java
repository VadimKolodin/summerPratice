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

    public boolean isDeliveryAreaFree( Restaurant restaurant){
        List<Restaurant> restaurants = dataManager.load(Restaurant.class).query("e.delivery=?1",
                restaurant.getDelivery()).list();
        if(restaurants.isEmpty()){
            return true;
        }
        return restaurants.contains(restaurant);
    }

    public boolean isRestaurantWithinItsDelivery(Restaurant restaurant){
        return restaurant.getDelivery().getPolygon().contains(restaurant.getCoordinates());
    }

    public boolean isRestaurantWithinDelivery(Restaurant restaurant, DeliveryArea deliveryArea){
        return deliveryArea.getPolygon().contains(restaurant.getCoordinates());
    }

    public Restaurant getRestaurantByItsDeliveryArea(DeliveryArea deliveryArea){
        List<Restaurant> restaurants = dataManager.load(Restaurant.class).query("e.delivery=?1",
                deliveryArea).list();
        if (restaurants.isEmpty()){
            return null;
        } else {
            return restaurants.get(0);
        }
    }
}
