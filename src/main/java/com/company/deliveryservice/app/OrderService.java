package com.company.deliveryservice.app;

import com.company.deliveryservice.entity.Order;
import com.company.deliveryservice.entity.Restaurant;
import com.company.deliveryservice.exceptions.CannotFingRestautantException;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private DataManager dataManager;

    @Autowired
    RestaurantService restaurantService;

    public Restaurant findRestaurantForOrder(Order order){
        List<Restaurant> nearestRestaurants = new ArrayList<>();
        for(Restaurant restaurant: restaurantService.getAllRestaurant()){
            if (order.getCoordinates().distance(restaurant.getDelivery().getPolygon()) == 0){
                nearestRestaurants.add(restaurant);
            }
        }
        if (nearestRestaurants.isEmpty()){
            throw new CannotFingRestautantException();
        }
        Restaurant nearestRestaurant = nearestRestaurants.get(0);
        double minDistance = order.getCoordinates().distance(nearestRestaurant.getCoordinates());
        for (Restaurant restaurant: nearestRestaurants){
            if (order.getCoordinates().distance(restaurant.getCoordinates())<minDistance){
                nearestRestaurant = restaurant;
                minDistance = order.getCoordinates().distance(restaurant.getCoordinates());
            }
        }
        return nearestRestaurant;
    }

    public List<Order> getAllOrders(){
       return dataManager.load(Order.class).all().list();
    }

}
