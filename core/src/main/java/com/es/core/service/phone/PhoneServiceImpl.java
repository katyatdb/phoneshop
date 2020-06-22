package com.es.core.service.phone;

import com.es.core.dao.color.ColorDao;
import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.phone.Phone;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private ColorDao colorDao;

    @Override
    public Optional<Phone> getPhone(Long id) {
        return phoneDao.get(id);
    }

    @Override
    public void save(Phone phone) {
        phoneDao.save(phone);
        colorDao.insertColors(phone.getColors());
        phoneDao.updatePhoneColors(phone.getId(), phone.getColors());
    }

    @Override
    public List<Phone> getPhoneList(String query, int page, int phonesPerPage, String sortBy, String orderBy) {
        int offset = (page - 1) * phonesPerPage;

        if (query != null) {
            query = query.trim().toLowerCase();
        }

        return phoneDao.findAll(query, offset, phonesPerPage, sortBy.toLowerCase(), orderBy.toLowerCase());
    }

    @Override
    public int getPhonesNumber(String query) {
        if (query != null) {
            query = query.trim().toLowerCase();
        }

        return phoneDao.countPhones(query);
    }
}
