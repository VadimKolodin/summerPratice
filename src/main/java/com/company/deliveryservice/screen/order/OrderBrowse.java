package com.company.deliveryservice.screen.order;

import com.company.deliveryservice.app.DeliveryAreaService;
import com.company.deliveryservice.app.RestaurantService;
import com.company.deliveryservice.entity.DeliveryArea;
import com.company.deliveryservice.entity.Restaurant;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.mapsui.component.layer.style.GeometryStyle;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Order_.browse")
@UiDescriptor("order-browse.xml")
@LookupComponent("ordersTable")
public class OrderBrowse extends StandardLookup<Order> {
    @Autowired
    private GeoMap map;

    @Autowired
    private GeometryStyles geometryStyles;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private GroupTable<Order> ordersTable;

    private CanvasLayer.Point oldPoint;

    @Subscribe("map.orderLayer")
    public void onMapOrderLayerGeoObjectSelected(VectorLayer.GeoObjectSelectedEvent<Order> event) {
        ordersTable.setSelected(event.getItem());
        CanvasLayer canvas = map.getCanvas();

        CanvasLayer.Point point = canvas.addPoint(event.getItem().getCoordinates());
        point.setStyle(geometryStyles.point().withFontIcon(chooseIcon(event.getItem()))
                .setIconPathFillColor("#FFFFFF")
                .setIconTextFillColor("#000000")
                .setIconPathStrokeColor("#000000"));
        if (oldPoint!=null){
            canvas.removePoint(oldPoint);
        }
        oldPoint=point;
    }

    @Install(to = "map.orderLayer", subject = "styleProvider")
    private GeometryStyle mapOrderLayerStyleProvider(Order order) {
        return geometryStyles.point()
                .withFontIcon(chooseIcon(order))
                .setIconPathFillColor("#"+order.getRestaurant().getDelivery().getColor());
    }

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

    private JmixIcon chooseIcon(Order order){
        switch (order.getStatus().getId()){
            case -1:
                return JmixIcon.BAN;
            case 0:
                return JmixIcon.CIRCLE_O;
            case 1:
                return JmixIcon.CHECK;
            default:
                return JmixIcon.QUESTION;
        }
    }
}