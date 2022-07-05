package com.company.deliveryservice.screen.order.order;

import io.jmix.mapsui.component.GeoMap;
import io.jmix.mapsui.component.layer.VectorLayer;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Order_.edit")
@UiDescriptor("order-edit.xml")
@EditedEntityContainer("orderDc")
public class OrderEdit extends StandardEditor<Order> {
   /* @Autowired
    private GeoMap map;

    @Autowired
    private InstanceContainer<Order> orderDc;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        VectorLayer<Order> orderLayer = new VectorLayer<>("orderLayer", orderDc);
        orderLayer.setEditable(true);
        map.addLayer(orderLayer);
        map.selectLayer(orderLayer);
    }*/
}