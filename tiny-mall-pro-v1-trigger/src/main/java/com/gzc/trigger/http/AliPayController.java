package com.gzc.trigger.http;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.gzc.api.dto.req.CreatePayRequestDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.service.IPayOrderService;
import com.gzc.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/alipay")
public class AliPayController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    @Resource
    private IPayOrderService payOrderService;


    /**
     * http://localhost:8090/api/v1/alipay/create_pay_order
     * <p>
     * {
     * "userId": "10001",
     * "productId": "100001"
     * }
     */
    @RequestMapping(value = "/create_pay_order", method = RequestMethod.POST)
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        try {
            String userId = createPayRequestDTO.getUserId();
            String productId = createPayRequestDTO.getProductId();
            // 下单
            PayOrderEntity payOrderEntity = payOrderService.createPayOrder(ShopCartEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .build());

            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("商品下单 userId:{}购买 商品ID为productId:{}时 创建支付单失败  ", createPayRequestDTO.getUserId(), createPayRequestDTO.getUserId(), e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     * 支付宝 回调调用
     * 在接收到支付回调消息后，更新订单状态。
     */
    @RequestMapping(value = "/alipay_notify_url", method = RequestMethod.POST)
    public String payNotify(HttpServletRequest request) throws AlipayApiException, ParseException {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));

            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }

                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");

                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("支付回调，交易名称: {}", params.get("subject"));
                    log.info("支付回调，交易状态: {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额: {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);

                    payOrderService.changeOrder2PaySuccess(tradeNo, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(gmtPayment));
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }
}
