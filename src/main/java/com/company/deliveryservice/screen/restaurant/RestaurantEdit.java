package com.company.deliveryservice.screen.restaurant;

import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Restaurant;

@UiController("Restaurant.edit")
@UiDescriptor("restaurant-edit.xml")
@EditedEntityContainer("restaurantDc")
public class RestaurantEdit extends StandardEditor<Restaurant> {
    @Install(to = "map.restaurantLayer", subject = "tooltipContentProvider")
    private String mapRestaurantLayerTooltipContentProvider(Restaurant restaurant) {
        return restaurant.getName();
    }
}