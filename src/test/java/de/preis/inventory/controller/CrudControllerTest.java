package de.preis.inventory.controller;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Offer;
import de.preis.inventory.data.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrudControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldCreateCategory() {

        Category category = new Category();
        String title = "category rest test";
        category.setTitle(title);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/categories", category, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ResponseEntity<List<Category>> exchange = testRestTemplate.exchange("http://localhost:" + port + "/categories", HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {
        });

        List<Category> body = exchange.getBody();
        assertEquals(1, body.size());
        assertEquals(title, body.get(0).getTitle());
    }

    @Test
    public void shouldCreateProductsAndAssignThemToCategories() {

        Category category = createNewCategoryWithApiCall();

        Product product1 = new Product();
        product1.setTitle("product 1");
        product1.setCategory(category);
        Product product2 = new Product();
        product2.setTitle("product 2");
        product2.setCategory(category);

        ResponseEntity<String> product1Response = testRestTemplate.postForEntity("http://localhost:" + port + "/products", product1, String.class);
        assertEquals(HttpStatus.CREATED, product1Response.getStatusCode());
        ResponseEntity<String> product2Response = testRestTemplate.postForEntity("http://localhost:" + port + "/products", product2, String.class);
        assertEquals(HttpStatus.CREATED, product2Response.getStatusCode());

        ResponseEntity<List<Product>> exchange = testRestTemplate.exchange("http://localhost:" + port + "/categories/" + category.getId() + "/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        List<Product> addedProducts = exchange.getBody();

        assertEquals(2, addedProducts.size());
        assertEquals("product 1", addedProducts.get(0).getTitle());
        assertEquals("/produkte/product-1/" + addedProducts.get(0).getId() + ".html", addedProducts.get(0).getProductDetailsPageUrl());
        assertEquals("product 2", addedProducts.get(1).getTitle());
        assertEquals("/produkte/product-2/" + addedProducts.get(1).getId() + ".html", addedProducts.get(1).getProductDetailsPageUrl());
    }

    @Test
    public void shouldCreateOffersAndAssignThemToProducts() {
        String p1Title = "product 1 shouldCreateOffersAndAssignThemToProducts";
        String p2Title = "product 2 shouldCreateOffersAndAssignThemToProducts";

        List<Product> products = createProductsWithApiCall(p1Title, p2Title);

        Offer offer1 = new Offer();
        offer1.setTitle("offer 1");
        offer1.setPrice(200.0);
        offer1.getProducts().add(products.get(0));
        offer1.getProducts().add(products.get(1));

        Offer offer2 = new Offer();
        offer2.setTitle("offer 2");
        offer2.setPrice(9.99);
        offer2.getProducts().add(products.get(0));

        ResponseEntity<String> offer1Response = testRestTemplate.postForEntity("http://localhost:" + port + "/offers", offer1, String.class);
        assertEquals(HttpStatus.CREATED, offer1Response.getStatusCode());
        ResponseEntity<String> offer2Response = testRestTemplate.postForEntity("http://localhost:" + port + "/offers", offer2, String.class);
        assertEquals(HttpStatus.CREATED, offer2Response.getStatusCode());


        ResponseEntity<List<Product>> exchange = testRestTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        List<Product> addedProducts = exchange.getBody();

        assertEquals(2, addedProducts.size());
        Product productResult1 = addedProducts.get(0);
        assertEquals(p1Title, productResult1.getTitle());
        assertEquals(9.99, productResult1.getBestPrice());
        Product productResult2 = addedProducts.get(1);
        assertEquals(p2Title, productResult2.getTitle());
        assertEquals(200.0, productResult2.getBestPrice());

    }

    private List<Product> createProductsWithApiCall(String p1Title, String p2Title) {
        Product product1 = new Product();
        product1.setTitle(p1Title);
        Product product2 = new Product();
        product2.setTitle(p2Title);

        testRestTemplate.postForEntity("http://localhost:" + port + "/products", product1, String.class);
        testRestTemplate.postForEntity("http://localhost:" + port + "/products", product2, String.class);

        ResponseEntity<List<Product>> exchange = testRestTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        return exchange.getBody().stream().filter(p -> p1Title.equals(p.getTitle()) || p2Title.equals(p.getTitle())).collect(Collectors.toList());
    }


    private Category createNewCategoryWithApiCall() {
        Category categoryRequest = new Category();
        String title = "shouldCreateProductsAndAssignThemToCategories";
        categoryRequest.setTitle(title);
        testRestTemplate.postForEntity("http://localhost:" + port + "/categories", categoryRequest, String.class);
        ResponseEntity<List<Category>> categoryResponse = testRestTemplate.exchange("http://localhost:" + port + "/categories", HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {
        });
        return categoryResponse.getBody().stream().filter(c -> "shouldCreateProductsAndAssignThemToCategories".equals(c.getTitle())).findFirst().get();
    }

}