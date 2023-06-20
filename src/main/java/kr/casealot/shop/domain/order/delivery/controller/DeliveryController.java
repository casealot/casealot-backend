package kr.casealot.shop.domain.order.delivery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Api(tags = {"DELIVERY COMMENT API"}, description = "DELIVERY 관련 API")
@RequestMapping(value = "/cal/v1/delivery", produces = "application/json; charset=utf8")
public class DeliveryController {
  private final String t_code = "08"; //롯데택배

  @Value("${delivery.key}")
  private String t_key; //발급받은 key

  private static final String BASE_URL = "https://info.sweettracker.co.kr/api/v1";
  private final RestTemplate restTemplate;

  @GetMapping
  @ApiOperation(value = "운송장 번호 조회", notes = "운송장 번호와 발급받은 키를 통해 운송장 번호를 조회한다.")
  public String getTrackingInfo(
      @ApiParam(value = "운송장 번호") @RequestParam String t_invoice) {

    // 필요한 파라미터 등을 설정하고 API 호출
    String url =
        BASE_URL + "/trackingInfo" +
            "?t_code=" + t_code +
            "&t_invoice=" + t_invoice +
            "&t_key=" + t_key;

    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url, HttpMethod.GET, null, String.class);

    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    } else if (responseEntity.getStatusCode().is4xxClientError()) {
      // 클라이언트 오류 응답 처리
      throw new RuntimeException();
    } else if (responseEntity.getStatusCode().is5xxServerError()) {
      // 서버 오류 응답 처리
      throw new RuntimeException();
    }
    return null;
  }
}
