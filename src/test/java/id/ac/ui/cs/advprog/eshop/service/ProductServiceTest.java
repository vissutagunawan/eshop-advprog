package id.ac.ui.cs.advprog.eshop.service;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct(){
        Product product = new Product();
        product.setProductName("product1");
        product.setProductQuantity(10);

        when(productRepository.create(any(Product.class))).thenReturn(product);
        Product result = productService.create(product);

        assertNotNull(result);
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getProductQuantity(), result.getProductQuantity());
        verify(productRepository).create(product);
    }

    @Test
    void testFindAllProducts(){
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductName("Product 1");
        Product product2 = new Product();
        product2.setProductName("Product 2");
        productList.add(product1);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList.iterator());

        List<Product> results = productService.findAll();

        assertNotNull(results);
        assertEquals(productList.size(), results.size());
        verify(productRepository).findAll();

    }

    @Test
    void testFindProductById(){
        // Prepare test data
        String productId = "test-id";
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Test Product");

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(product);

        // Execute service method
        Product result = productService.findById(productId);

        // Verify results
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(product.getProductName(), result.getProductName());
        verify(productRepository).findById(productId);
    }

    @Test
    void testFindByIdNotFound() {
        // Prepare test data
        String productId = "non-existent-id";

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(null);

        // Execute service method
        Product result = productService.findById(productId);

        // Verify results
        assertNull(result);
        verify(productRepository).findById(productId);
    }

    @Test
    void testEditProduct() {
        // Prepare test data
        Product editedProduct = new Product();
        editedProduct.setProductId("test-id");
        editedProduct.setProductName("Updated Product");
        editedProduct.setProductQuantity(20);

        // Mock repository behavior
        when(productRepository.edit(editedProduct)).thenReturn(editedProduct);

        // Execute service method
        Product result = productService.edit(editedProduct);

        // Verify results
        assertNotNull(result);
        assertEquals(editedProduct.getProductId(), result.getProductId());
        assertEquals(editedProduct.getProductName(), result.getProductName());
        assertEquals(editedProduct.getProductQuantity(), result.getProductQuantity());
        verify(productRepository).edit(editedProduct);
    }

    @Test
    void testEditProductNotFound() {
        // Prepare test data
        Product editedProduct = new Product();
        editedProduct.setProductId("non-existent-id");

        // Mock repository behavior
        when(productRepository.edit(editedProduct)).thenReturn(null);

        // Execute service method
        Product result = productService.edit(editedProduct);

        // Verify results
        assertNull(result);
        verify(productRepository).edit(editedProduct);
    }

    @Test
    void testDeleteProduct() {
        // Prepare test data
        String productId = "test-id";

        // Mock repository behavior
        when(productRepository.delete(productId)).thenReturn(true);

        // Execute service method
        boolean result = productService.delete(productId);

        // Verify results
        assertTrue(result);
        verify(productRepository).delete(productId);
    }

    @Test
    void testDeleteProductNotFound() {
        // Prepare test data
        String productId = "non-existent-id";

        // Mock repository behavior
        when(productRepository.delete(productId)).thenReturn(false);

        // Execute service method
        boolean result = productService.delete(productId);

        // Verify results
        assertFalse(result);
        verify(productRepository).delete(productId);
    }
}
