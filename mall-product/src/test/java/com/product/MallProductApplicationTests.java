package com.product;
import com.product.entity.BrandEntity;
import com.product.entity.CategoryBrandRelationEntity;
import com.product.entity.CategoryEntity;
import com.product.service.BrandService;
import com.product.service.CategoryBrandRelationService;
import com.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Test
    public void testFindPath(){
        Long[] result = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.toString(result));
    }
    @Test
    void contextLoads() {
        BrandEntity brand = new BrandEntity();
        brand.setName("huawei");
        boolean result = brandService.save(brand);
        System.out.println("the result is " + result);
    }

    @Test
    public void testGetCategoryTree(){
        List<CategoryEntity> result = categoryService.listByTree();
        System.out.println(result);
    }

    @Test
    public void testListRelatedCat(){
        List<CategoryBrandRelationEntity> result = categoryBrandRelationService.listRelatedCat(1L);
        System.out.println(result);
    }
}
