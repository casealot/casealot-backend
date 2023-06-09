package kr.casealot.shop.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StringUtil {
    /**
     * 15자리 주문번호 생성
     * @return
     */
    public static String generateOrderNumber(){
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String orderNumber = currentDate + uuid.substring(0, 9);
        return orderNumber.toUpperCase();
    }

    public static String generateDeliveryNumber() {
        // 운송장 번호로
        Random random = new Random();
        Long deliveryNumber = random.nextLong(1000000000000L); //12자리

        return String.format("%012d", deliveryNumber);
    }

    public static void main(String[] args) {
        String orderNumber = generateOrderNumber();
        System.out.println("주문번호 ::: " + orderNumber);
    }
}
