package org.example.service;

import org.example.dao.DealDao;
import org.example.dao.DealDaoImpl;
import org.example.exception.CustomException;
import org.example.model.Deal;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DealService {
    final private DealDao dealDao = new DealDaoImpl();
    private final Map<Long,Object> dealLocks = new ConcurrentHashMap<>();

    public void updateDealAmount(Long dealId,BigDecimal newAmount){
        Object lock = dealLocks.computeIfAbsent(dealId,id -> new Object());
        synchronized(lock){
            Deal deal = dealDao.findById(dealId);
            if(deal == null){
                throw new CustomException("Deal not found." + dealId);
            }
            deal.setAmount(newAmount);
            dealDao.update(deal);
        }
    }
}
