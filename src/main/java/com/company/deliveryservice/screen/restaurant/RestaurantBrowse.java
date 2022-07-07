package com.company.deliveryservice.screen.restaurant;

import com.company.deliveryservice.app.DeliveryAreaService;
import com.company.deliveryservice.entity.DeliveryArea;
import com.company.deliveryservice.entity.Order;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.mapsui.component.layer.style.GeometryStyle;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.RemoveOperation;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@UiController("Restaurant.browse")
@UiDescriptor("restaurant-browse.xml")
@LookupComponent("restaurantsTable")
public class RestaurantBrowse extends StandardLookup<Restaurant> {

    @Autowired
    private GeoMap map;

    @Autowired
    private GeometryStyles geometryStyles;

    @Autowired
    private DeliveryAreaService deliveryAreaService;

    @Autowired
    private GroupTable<Restaurant> restaurantsTable;

    private CanvasLayer.Polygon oldPolygon;
    private CanvasLayer.Point oldPoint;
    private HashMap<DeliveryArea, CanvasLayer.Polygon> deliveryAreaPolygonHashMap = new HashMap<>();

    @Subscribe("map.restaurantLayer")
    public void onMapRestaurantLayerGeoObjectSelected(VectorLayer.GeoObjectSelectedEvent<Restaurant> event) {
        if (event.getItem()==null)
            return;
        restaurantsTable.setSelected(event.getItem());
        CanvasLayer canvas = map.getCanvas();

        CanvasLayer.Point point = canvas.addPoint(event.getItem().getCoordinates());
        point.setStyle(geometryStyles.point().withFontIcon(JmixIcon.CIRCLE)
                .setIconPathFillColor("#FFFFFF")
                .setIconTextFillColor("#000000")
                .setIconPathStrokeColor("#000000"));
        point.setTooltipContent(event.getItem().getName());

        CanvasLayer.Polygon polygon = canvas.addPolygon(event.getItem().getDelivery().getPolygon());
        polygon.setStyle(geometryStyles.polygon()
                .setFillColor("#FFFFFF")
                .setStrokeWeight(3)
                .setStrokeColor("#FFFFFF")
                .setFillOpacity(0.5));

        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
        }
        if (oldPoint!=null){
            canvas.removePoint(oldPoint);
        }

        oldPolygon = polygon;
        oldPoint = point;
    }

    @Install(to = "map.restaurantLayer", subject = "styleProvider")
    private GeometryStyle mapRestaurantLayerStyleProvider(Restaurant restaurant) {
        return geometryStyles.point()
                .withFontIcon(JmixIcon.COFFEE)
                .setIconPathFillColor("#"+restaurant.getDelivery().getColor());
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        CanvasLayer canvas = map.getCanvas();
        for (DeliveryArea deliveryArea: deliveryAreaService.getAllDeliveryAreas()){
            CanvasLayer.Polygon polygon = canvas.addPolygon(deliveryArea.getPolygon());
            polygon.setStyle(geometryStyles.polygon()
                    .setFillColor("#"+deliveryArea.getColor())
                    .setStrokeColor("#"+deliveryArea.getColor())
                    .setFillOpacity(0.2)
                    .setStrokeWeight(1));
            deliveryAreaPolygonHashMap.put(deliveryArea, polygon);
        }

    }

    @Install(to = "restaurantsTable.edit", subject = "afterCommitHandler")
    private void restaurantsTableEditAfterCommitHandler(Restaurant restaurant) {
        CanvasLayer canvas = map.getCanvas();
        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
            oldPolygon = null;
        }
        if (oldPoint!=null){
            canvas.removePoint(oldPoint);
            oldPoint = null;
        }

    }

    @Install(to = "restaurantsTable.remove", subject = "afterActionPerformedHandler")
    private void restaurantsTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<Restaurant> afterActionPerformedEvent) {
        CanvasLayer canvas = map.getCanvas();
        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
            oldPolygon = null;
        }
        if (oldPoint!=null){
            canvas.removePoint(oldPoint);
            oldPoint = null;
        }
        for (Restaurant restaurant: afterActionPerformedEvent.getItems()){
            canvas.removePolygon(deliveryAreaPolygonHashMap.get(restaurant.getDelivery()));
            deliveryAreaPolygonHashMap.remove(restaurant.getDelivery());
        }
    }


}