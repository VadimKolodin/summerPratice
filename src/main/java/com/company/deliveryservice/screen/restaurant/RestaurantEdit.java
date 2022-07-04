package com.company.deliveryservice.screen.restaurant;

import com.company.deliveryservice.app.DeliveryAreaService;
import com.company.deliveryservice.app.RestaurantService;
import com.company.deliveryservice.entity.DeliveryArea;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.Notifications;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Restaurant.edit")
@UiDescriptor("restaurant-edit.xml")
@EditedEntityContainer("restaurantDc")
public class RestaurantEdit extends StandardEditor<Restaurant> {

    @Autowired
    private GeoMap map;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DeliveryAreaService deliveryAreaService;

    @Autowired
    private GeometryStyles geometryStyles;

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

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        CanvasLayer canvas = map.getCanvas();
        for (DeliveryArea deliveryArea: deliveryAreaService.getAllDeliveryAreas()){
            CanvasLayer.Polygon polygon = canvas.addPolygon(deliveryArea.getPolygon());
            polygon.setStyle(geometryStyles.polygon()
                    .setFillColor("#"+deliveryArea.getColor())
                    .setStrokeColor("#"+deliveryArea.getColor())
                    .setFillOpacity(0.1)
                    .setStrokeWeight(1));
        }
        for (Restaurant restaurant: restaurantService.getAllRestaurant()){
            CanvasLayer.Point point = canvas.addPoint(restaurant.getCoordinates());
            point.setTooltipContent(restaurant.getName()).setStyle(
                    geometryStyles.point()
                            .withFontIcon(JmixIcon.COFFEE)
                            .setIconPathFillColor("#"+restaurant.getDelivery().getColor()));

        }
    }
}