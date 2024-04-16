package com.project.eat.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoPayServiceImpl {


    // 첫번째 요청주소(결제 준비)

    public ReadyResponseVO payReady(Long orderId, String itemName, int quantity, String memberId, int totalAmount) {
        // String order_id = "100";


        //log.info("주문번호: " + odr_code);
        //log.info("주문상품명:" + itemName);
        //log.info("수량:" + quantity);
        //log.info("아이디: " + mem_id);s
        //log.info("총금액: " + totalAmount);


        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("cid", "TC0ONETIME");
        parameters.put("partner_order_id", String.valueOf(orderId));
        parameters.put("partner_user_id", memberId);
        parameters.put("item_name", itemName);
        parameters.put("quantity", String.valueOf(quantity));
        parameters.put("total_amount", String.valueOf(totalAmount));
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "http://localhost:8088/order/kakao/success");
        parameters.put("cancel_url", "http://localhost:8088/order/kakao/cancel");
        parameters.put("fail_url", "http://localhost:8088/order/kakao/fail");


        // 헤더와 파라미터 정보 구성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부 API 통신
        RestTemplate template = new RestTemplate();

        // 1차 요청주소
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        ReadyResponseVO readyResponseVO = template.postForObject(url, requestEntity, ReadyResponseVO.class);

        return readyResponseVO;
    }

    // 요청 헤더정보
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY DEVA3CC956AFA327ABB7CDA1885DEA974E70DB23");
        headers.set("Content-type", "application/json");

        return headers;
    }

    // 두번째 요청(결제승인 요청)
    public ApproveResponseVO payApprove(Long orderId, String tid, String pgToken, String memberId) {

        //String order_id = "100";

        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("cid", "TC0ONETIME");  // 가맹점 코드, 10자. 제휴를 맺게되면, 회사담당자에게 문의를 하여 가맹점코드를 변경한다.
        parameters.put("tid", tid);
        parameters.put("partner_order_id", String.valueOf(orderId)); // 가맹점 주문번호, 최대 100자
        parameters.put("partner_user_id", memberId); // 가맹점 회원 id, 최대 100자
        // 결제승인 요청을 인증하는 토큰
        parameters.put("pg_token", pgToken);


        // 헤더와 파라미터정보를 구성.
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        //외부API통신
        RestTemplate template = new RestTemplate();
        //1차 요청주소
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

        ApproveResponseVO approveResponse = template.postForObject(url, requestEntity, ApproveResponseVO.class);

        return new ApproveResponseVO();
    }

    public ApproveResponseVO payCancel(Order order) {

        //String order_id = "100";
        Long orderId = order.getId();
        String tid = order.getTid();
        String cancel_amount = String.valueOf(order.getOrderPrice() + order.getTotalPrice());
        String cancel_tax_free_amount = "0";
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("cid", "TC0ONETIME");  // 가맹점 코드, 10자. 제휴를 맺게되면, 회사담당자에게 문의를 하여 가맹점코드를 변경한다.
        parameters.put("tid", tid);
        parameters.put("cancel_amount", cancel_amount); // 취소 주문 금액
        parameters.put("cancel_tax_free_amount", cancel_tax_free_amount); // 취소 비과세 금액


        // 헤더와 파라미터정보를 구성.
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        //외부API통신
        RestTemplate template = new RestTemplate();
        //1차 요청주소
        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";

        ApproveResponseVO approveResponse = template.postForObject(url, requestEntity, ApproveResponseVO.class);

        return new ApproveResponseVO();
    }
}
