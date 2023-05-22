package kr.casealot.shop.domain.product;

import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Component
@RequiredArgsConstructor
public class DummyDataInitializer {
    private final ProductRepository productRepository;

    @PostConstruct
    public void init(){
        List<String> season = Arrays.asList("봄","여름","가을","겨울");
        List<String> name = Arrays.asList("깔끔한","시원한","멋진","이쁜");
        for(int i = 0; i < 100 ; i++){
            Product product = Product.builder()
                    .name(season.get((int)(Math.random() * 3)) + " " + name.get((int)(Math.random() * 3))
                    + " 모자입니다.")
                    .content("테스트 데이터 입니다.")
                    .season("23/SS")
                    .sells(1)
                    .views(1)
                    .color("red").build();
            productRepository.save(product);
        }
    }
}
