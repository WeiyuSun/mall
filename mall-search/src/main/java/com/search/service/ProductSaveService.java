package com.search.service;

import com.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductSaveService {

    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
