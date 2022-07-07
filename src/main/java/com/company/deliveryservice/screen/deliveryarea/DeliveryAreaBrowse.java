package com.company.deliveryservice.screen.deliveryarea;

import com.company.deliveryservice.app.DeliveryAreaService;
import com.company.deliveryservice.app.RestaurantService;
import com.company.deliveryservice.entity.Restaurant;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.mapsui.component.layer.style.GeometryStyle;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.RemoveOperation;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.DeliveryArea;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.HashMap;

@UiController("DeliveryArea.browse")
@UiDescriptor("delivery-area-browse.xml")
@LookupComponent("deliveryAreasTable")
public class DeliveryAreaBrowse extends StandardLookup<DeliveryArea> {
    @Autowired
    private GroupTable<DeliveryArea> deliveryAreasTable;

    @Autowired
    private GeoMap map;

    @Autowired
    private GeometryStyles geometryStyles;

    @Autowired
    private DeliveryAreaService deliveryAreaService;

    @Autowired
    private RestaurantService restaurantsService;

    private CanvasLayer.Polygon oldPolygon;
    private HashMap<DeliveryArea, CanvasLayer.Point> restaurantPointHashMap = new HashMap<>();

    @Subscribe("map.areaLayer")
    public void onMapAreaLayerGeoObjectSelected(VectorLayer.GeoObjectSelectedEvent<DeliveryArea> event) {
        if (event.getItem()==null)
            return;
        deliveryAreasTable.setSelected(event.getItem());
        CanvasLayer canvas = map.getCanvas();
        CanvasLayer.Polygon polygon = canvas.addPolygon(event.getItem().getPolygon());
        polygon.setStyle(geometryStyles.polygon()
                .setFillColor("#FFFFFF")
                .setStrokeWeight(3)
                .setStrokeColor("#FFFFFF")
                .setFillOpacity(0.5));
        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
        }
        oldPolygon = polygon;
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        CanvasLayer canvas = map.getCanvas();
        for (Restaurant restaurant: restaurantsService.getAllRestaurant()){
            CanvasLayer.Point point = canvas.addPoint(restaurant.getCoordinates());
            point.setTooltipContent(restaurant.getName()).setStyle(
                    geometryStyles.point()
                            .withFontIcon(JmixIcon.COFFEE)
                            .setIconPathFillColor("#"+restaurant.getDelivery().getColor()));
            restaurantPointHashMap.put(restaurant.getDelivery(), point);
        }

    }
    @Install(to = "map.areaLayer", subject = "styleProvider")
    private GeometryStyle mapAreaLayerStyleProvider(DeliveryArea deliveryArea) {
        return geometryStyles.polygon()
                .setFillColor("#"+deliveryArea.getColor())
                .setStrokeColor("#"+deliveryArea.getColor())
                .setFillOpacity(0.3)
                .setStrokeWeight(2);
    }

    @Install(to = "deliveryAreasTable.edit", subject = "afterCommitHandler")
    private void deliveryAreasTableEditAfterCommitHandler(DeliveryArea deliveryArea) {
        if (oldPolygon!=null) {
            CanvasLayer canvas = map.getCanvas();
            canvas.removePolygon(oldPolygon);
            oldPolygon = null;
        }
    }

    @Install(to = "deliveryAreasTable.remove", subject = "afterActionPerformedHandler")
    private void deliveryAreasTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent<DeliveryArea> afterActionPerformedEvent) {
        CanvasLayer canvas = map.getCanvas();
        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
            oldPolygon = null;
        }
        for (DeliveryArea deliveryArea: afterActionPerformedEvent.getItems()){
            canvas.removePoint(restaurantPointHashMap.get(deliveryArea));
            restaurantPointHashMap.remove(deliveryArea);
        }
    }


}