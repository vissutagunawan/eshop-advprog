package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Test Product");
        product.setProductQuantity(100);

        productList = new ArrayList<>();
        productList.add(product);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);

        verify(model).addAttribute(eq("product"), any(Product.class));
        assertEquals("createProduct", viewName);
    }

    @Test
    void testCreateProductPostSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = productController.createProductPost(product, bindingResult, model);

        verify(service).create(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void testCreateProductPostError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = productController.createProductPost(product, bindingResult, model);

        verify(service, never()).create(any(Product.class));
        assertEquals("createProduct", viewName);
    }

    @Test
    void testProductListPage() {
        when(service.findAll()).thenReturn(productList);

        String viewName = productController.productListPage(model);

        verify(model).addAttribute("products", productList);
        assertEquals("productList", viewName);
    }

    @Test
    void testEditProductPage() {
        when(service.findById(product.getProductId())).thenReturn(product);

        String viewName = productController.editProductPage(product.getProductId(), model);

        verify(model).addAttribute("product", product);
        assertEquals("editProduct", viewName);
    }

    @Test
    void testEditProductPostSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = productController.editProductPost(product, bindingResult, model);

        verify(service).edit(product);
        assertEquals("redirect:list", viewName);
    }

    @Test
    void testEditProductPostError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = productController.editProductPost(product, bindingResult, model);

        verify(service, never()).edit(any(Product.class));
        assertEquals("editProduct", viewName);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct(product.getProductId());

        verify(service).delete(product.getProductId());
        assertEquals("redirect:/product/list", viewName);
    }
}