package com.es.phoneshop.web.converter;

import com.es.core.model.order.Order;
import com.es.phoneshop.web.model.UserDataForm;
import org.springframework.core.convert.converter.Converter;

public class UserDataFormToOrderConverter implements Converter<UserDataForm, Order> {
    private Order order;

    public UserDataFormToOrderConverter() {
        this.order = new Order();
    }

    public UserDataFormToOrderConverter(Order order) {
        this.order = order;
    }

    @Override
    public Order convert(UserDataForm userDataForm) {
        order.setFirstName(userDataForm.getFirstName());
        order.setLastName(userDataForm.getLastName());
        order.setDeliveryAddress(userDataForm.getAddress());
        order.setContactPhoneNo(userDataForm.getPhone());
        order.setAdditionalInfo(userDataForm.getAdditionalInfo());

        return order;
    }
}
