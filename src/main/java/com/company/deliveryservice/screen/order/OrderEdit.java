package com.company.deliveryservice.screen.order;

import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.Order;

@UiController("Order_.edit")
@UiDescriptor("order-edit.xml")
@EditedEntityContainer("orderDc")
public class OrderEdit extends StandardEditor<Order> {
}