package com.product;

import com.product.entity.BrandEntity;
import com.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
   private BrandService brandService;
    @Test
    void contextLoads() {
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        boolean result = brandService.save(brand);
        System.out.println("the result is " + result);
    }

}
