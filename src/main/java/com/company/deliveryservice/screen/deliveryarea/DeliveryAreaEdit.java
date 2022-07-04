package com.company.deliveryservice.screen.deliveryarea;

import com.company.deliveryservice.app.DeliveryAreaService;
import com.company.deliveryservice.app.RestaurantService;
import com.company.deliveryservice.entity.Restaurant;
import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.Notifications;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.DeliveryArea;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("DeliveryArea.edit")
@UiDescriptor("delivery-area-edit.xml")
@EditedEntityContainer("deliveryAreaDc")
public class DeliveryAreaEdit extends StandardEditor<DeliveryArea> {
    @Autowired
    private DeliveryAreaService deliveryAreaService;

    @Autowired
    private Notifications notifications;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private GeoMap map;

    @Autowired
    private GeometryStyles geometryStyles;

    @Subscribe
    public void onBeforeCommitChange(BeforeCommitChangesEvent event){
        DeliveryArea deliveryArea = getEditedEntity();
        if (deliveryAreaService.isHex(deliveryArea.getColor())){
            event.resume();
        } else {
            notifications.create().withCaption("Colour must be hexadecimal string").show();
            event.preventCommit();
        }
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        CanvasLayer canvas = map.getCanvas();

        for (Restaurant restaurant: restaurantService.getAllRestaurant()){
            CanvasLayer.Point point = canvas.addPoint(restaurant.getCoordinates());
            point.setTooltipContent(restaurant.getName()).setStyle(
                    geometryStyles.point()
                            .withFontIcon(JmixIcon.COFFEE)
                            .setIconPathFillColor("#"+restaurant.getDelivery().getColor()));
        }

        for (DeliveryArea deliveryArea: deliveryAreaService.getAllDeliveryAreas()){
            CanvasLayer.Polygon polygon = canvas.addPolygon(deliveryArea.getPolygon());
            polygon.setStyle(geometryStyles.polygon()
                    .setFillColor("#"+deliveryArea.getColor())
                    .setStrokeColor("#"+deliveryArea.getColor())
                    .setFillOpacity(0.1)
                    .setStrokeWeight(1));
        }


    }

}