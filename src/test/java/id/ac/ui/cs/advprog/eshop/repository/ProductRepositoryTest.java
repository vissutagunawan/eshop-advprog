package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {
    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821d6e9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct() {
        // Create initial product
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Create updated product with same ID
        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang Updated");
        updatedProduct.setProductQuantity(200);

        // Perform update
        Product result = productRepository.edit(updatedProduct);

        // Verify the update
        assertNotNull(result);
        assertEquals(updatedProduct.getProductName(), result.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), result.getProductQuantity());

        // Verify through findAll
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(updatedProduct.getProductName(), savedProduct.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testEditProductNotFoundInLoop() {
        // Create initial product
        Product product = new Product();
        product.setProductId("id1");
        product.setProductName("Product 1");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Create a product with different ID
        Product editProduct = new Product();
        editProduct.setProductId("id2");
        editProduct.setProductName("Product 2");
        editProduct.setProductQuantity(200);

        // Try to edit - should fail because ID doesn't match
        Product result = productRepository.edit(editProduct);
        assertNull(result);

        // Verify original product is unchanged
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testEditNonExistentProduct() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Non Existent Product");
        nonExistentProduct.setProductQuantity(100);

        Product result = productRepository.edit(nonExistentProduct);
        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        // Create a product
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Verify product exists
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        // Delete the product
        productRepository.delete(product.getProductId());

        // Verify product no longer exists
        productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteNonExistentProduct() {
        // Delete non-existent product
        productRepository.delete("non-existent-id");

        // Verify no products exist
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }
}