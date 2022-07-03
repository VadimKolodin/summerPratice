package com.company.deliveryservice.screen.deliveryarea;

import io.jmix.ui.screen.*;
import com.company.deliveryservice.entity.DeliveryArea;

@UiController("DeliveryArea.edit")
@UiDescriptor("delivery-area-edit.xml")
@EditedEntityContainer("deliveryAreaDc")
public class DeliveryAreaEdit extends StandardEditor<DeliveryArea> {
}