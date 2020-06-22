package com.es.core.dao.phone;

import com.es.core.model.color.Color;
import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit);

    List<Phone> findAll(String query, int offset, int limit, String sort, String order);

    void updatePhoneColors(Long phoneId, Set<Color> colors);

    int countPhones(String query);
}
