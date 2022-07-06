package com.company.deliveryservice.screen.order.order;

import com.company.deliveryservice.app.OrderService;
import com.company.deliveryservice.app.RestaurantService;
import com.company.deliveryservice.entity.DeliveryArea;
import com.company.deliveryservice.entity.Restaurant;
import com.company.deliveryservice.exceptions.CannotFingRestautantException;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.Notification;

@UiController("Order_.edit")
@UiDescriptor("order-edit.xml")
@EditedEntityContainer("orderDc")
public class OrderEdit extends StandardEditor<Order> {

    @Autowired
    private GeoMap map;

    @Autowired
    private GeometryStyles geometryStyles;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Notifications notifications;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        CanvasLayer canvas = map.getCanvas();
        for (Restaurant restaurant: restaurantService.getAllRestaurant()){
            CanvasLayer.Polygon polygon = canvas.addPolygon(restaurant.getDelivery().getPolygon());
            polygon.setStyle(geometryStyles.polygon()
                    .setFillColor("#"+restaurant.getDelivery().getColor())
                    .setStrokeColor("#"+restaurant.getDelivery().getColor())
                    .setFillOpacity(0.2)
                    .setStrokeWeight(1));

            CanvasLayer.Point point= canvas.addPoint(restaurant.getCoordinates());
            point.setStyle(geometryStyles.point()
                    .withFontIcon(JmixIcon.COFFEE)
                    .setIconPathFillColor("#"+restaurant.getDelivery().getColor()));
        }
    }

    @Subscribe
    public void onBeforeCommitChange(BeforeCommitChangesEvent event){
        Order order = getEditedEntity();
        try{
            order.setRestaurant(orderService.findRestaurantForOrder(order));
            event.resume();
        } catch (CannotFingRestautantException e){
            notifications.create().withCaption("Order must be within at least one delivery area of restaurant").show();
            event.preventCommit();
        }
    }
}