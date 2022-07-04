package com.company.deliveryservice.app;

import com.company.deliveryservice.entity.DeliveryArea;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryAreaService {

    @Autowired
    DataManager dataManager;

    public boolean isHex(String color){
        return color.matches("[A-Fa-f0-9]{6}");
    }

    public List<DeliveryArea> getAllDeliveryAreas(){
        return dataManager.load(DeliveryArea.class).all().list();
    }
}
