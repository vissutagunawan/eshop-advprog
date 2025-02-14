package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository{
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product){
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll(){
        return productData.iterator();
    }

    public Product findById(String productId){
        return productData
                .stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public boolean edit(Product editedProduct){
        for (int i=0; i<productData.size(); i++){
            if (productData.get(i).getProductId().equals(editedProduct.getProductId())){
                productData.set(i, editedProduct);
                return true;
            }
        }

        return false;
    }

    public boolean delete(String productId){
        Product targetProduct = findById(productId);
        if (targetProduct != null){
            productData.remove(targetProduct);
            return true;
        }

        return false;
    }
}