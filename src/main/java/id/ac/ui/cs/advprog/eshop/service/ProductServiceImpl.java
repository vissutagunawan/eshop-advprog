package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.InterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private InterfaceRepository<Product> productRepository;

    @Override
    public Product create(Product product){
        product.setProductId(UUID.randomUUID().toString());
        productRepository.create(product);
        return product;
    }

    @Override
    public List<Product> findAll(){
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product findById(String productId){
        return productRepository.findById(productId);
    }

    @Override
    public Product edit(Product editedProduct){
        return productRepository.edit(editedProduct);
    }

    @Override
    public boolean delete(String productId){
        return productRepository.delete(productId);
    }

}
