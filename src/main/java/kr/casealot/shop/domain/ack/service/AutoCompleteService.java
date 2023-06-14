package kr.casealot.shop.domain.ack.service;

import kr.casealot.shop.domain.ack.dto.ACKResponse;
import kr.casealot.shop.domain.ack.dto.Keyword;
import kr.casealot.shop.domain.ack.support.TrieNode;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutoCompleteService {

    private static TrieNode trieNode;
    private final ProductRepository productRepository;

    @PostConstruct
    void loadKeyword(){
        trieNode = new TrieNode();
        Set<Product> productSet = new HashSet<>(productRepository.findAll());
        productSet.stream().map(Product::getName).forEach(trieNode::insert);
    }

    public APIResponse<ACKResponse> getACK(String query) {
        if(query.trim().isEmpty()){
            return APIResponse.success(ACKResponse.builder()
                            .item(new ArrayList<>())
                            .totalCount(0)
                    .build());
        }

        List<String> matchKeywords = trieNode.autoComplete(query);
        List<Keyword> keywords = matchKeywords.stream()
                .map(matchKeyword -> Keyword.builder()
                        .keyword(matchKeyword)
                        .hlKeyword(highlightResult(matchKeyword, query))
                        .build())
                .collect(Collectors.toList());

        ACKResponse ackResponse = ACKResponse.builder()
                .item(keywords)
                .totalCount(keywords.size())
                .build();

        return APIResponse.success(ackResponse);
    }

    private String highlightResult(String result, String query) {
        int startIndex = result.toLowerCase().indexOf(query.toLowerCase());
        if (startIndex >= 0) {
            result = "<HS>" + result.substring(result.indexOf(query), query.length()) + "<HE>" + result.substring(query.length());
        }
        return result;
    }
}
