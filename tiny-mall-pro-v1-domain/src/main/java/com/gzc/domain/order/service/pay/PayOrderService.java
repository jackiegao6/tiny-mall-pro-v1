package com.gzc.domain.order.service.pay;


import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.entity.req.ShopCartEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService implements IPayOrderService{

    private final IPayOrderRepository payOrderRepository;

    @Override
    public void createPayOrder(ShopCartEntity shopCartEntity) {

        payOrderRepository.createPayOrder(shopCartEntity);

    }
}
