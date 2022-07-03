package com.company.deliveryservice.screen.deliveryarea;

import io.jmix.mapsui.component.CanvasLayer;
import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.mapsui.component.layer.style.GeometryStyle;
import io.jmix.mapsui.component.layer.style.GeometryStyles;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.DeliveryArea;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

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

    private CanvasLayer.Polygon oldPolygon ;

    @Subscribe("map.areaLayer")
    public void onMapOrderLayerGeoObjectSelected(VectorLayer.GeoObjectSelectedEvent<DeliveryArea> event) {
        deliveryAreasTable.setSelected(event.getItem());
        CanvasLayer canvas = map.getCanvas();
        CanvasLayer.Polygon polygon = canvas.addPolygon(event.getItem().getPolygon());
        polygon.setStyle(geometryStyles.polygon().setFillColor("#FFFFFF").setStrokeWeight(5).setStrokeColor("#FFFFFF"));
        if (oldPolygon!=null) {
            canvas.removePolygon(oldPolygon);
        }
        oldPolygon = polygon;
    }



}