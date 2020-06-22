package com.es.core.service.phone;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> getPhone(Long id);

    void save(Phone phone);

    List<Phone> getPhoneList(String query, int page, int limit, String sortBy, String orderBy);

    int getPhonesNumber(String query);
}
