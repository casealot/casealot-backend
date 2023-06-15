package kr.casealot.shop.domain.ack.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.ack.dto.ACKResponse;
import kr.casealot.shop.domain.ack.service.AutoCompleteService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"AUTOCOMPLETE API"}, description = "상품명 자동완성")
@RequestMapping("/cal/v1/autocomplete")
public class AutoCompleteController {

    private final AutoCompleteService autoCompleteService;

    @GetMapping
    @ApiOperation(value = "상품명 자동완성", notes = "상품명으로 자동완성을 만들어준다.")
    public APIResponse<ACKResponse> getAutoCompleteKeyword(
            @ApiParam(value = "자동완성 query") @PathParam("query") String query){
        return autoCompleteService.getACK(query);
    }

}
