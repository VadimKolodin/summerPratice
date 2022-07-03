package com.company.deliveryservice.screen.restaurant;

import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Restaurant;

@UiController("Restaurant.browse")
@UiDescriptor("restaurant-browse.xml")
@LookupComponent("restaurantsTable")
public class RestaurantBrowse extends StandardLookup<Restaurant> {
}