package com.product.feign;

import com.common.to.SkuHasStockVo;
import com.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name="mall-ware", url="localhost:11000")
public interface WareFeignService {

    @PostMapping(value = "/mall/ware/waresku/hasstock")
    List<SkuHasStockVo> getSkuHasStock(@RequestBody List<Long> skuIds);
}
