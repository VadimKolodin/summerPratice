package com.company.deliveryservice.screen.restaurant;

import com.company.deliveryservice.app.RestaurantService;
import io.jmix.ui.Notifications;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Restaurant.edit")
@UiDescriptor("restaurant-edit.xml")
@EditedEntityContainer("restaurantDc")
public class RestaurantEdit extends StandardEditor<Restaurant> {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    private Notifications notifications;

    @Install(to = "map.restaurantLayer", subject = "tooltipContentProvider")
    private String mapRestaurantLayerTooltipContentProvider(Restaurant restaurant) {
        return restaurant.getName();
    }
    @Subscribe
    public void onBeforeCommitChange(BeforeCommitChangesEvent event){
        if (restaurantService.isDeliveryAreaFree(getEditedEntity().getDelivery())){
            event.resume();
        } else {
            notifications.create().withCaption("This delivery area is already used by another restaurant").show();
            event.preventCommit();
        }
    }
}