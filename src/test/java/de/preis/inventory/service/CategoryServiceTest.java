package de.preis.inventory.service;

import de.preis.inventory.data.Category;
import de.preis.inventory.data.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static de.preis.inventory.testutil.EntityFactory.product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    private Product p1;
    private Product p2;
    private Product p3;

    private Category category;
    private final String categoryTitle = "category";

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setTitle(categoryTitle);
        p1 = productService.createResource(product("p1"));
        p2 = productService.createResource(product("p2"));
        p3 = productService.createResource(product("p3"));
    }

    @Test
    public void shouldCreateCategory() {

        categoryService.createResource(category);

        List<Category> resources = categoryService.getResources();

        assertEquals(1, resources.size());
        assertEquals(categoryTitle, resources.get(0).getTitle());
    }

    @Test
    public void shouldCreateCategoryWithProducts() {

        category.getProducts().add(p1);
        category.getProducts().add(p2);

        categoryService.createResource(category);

        List<Category> resources = categoryService.getResources();

        assertEquals(1, resources.size());
        assertEquals(categoryTitle, resources.get(0).getTitle());
        assertEquals("p1", resources.get(0).getProducts().get(0).getTitle());
        assertEquals("p2", resources.get(0).getProducts().get(1).getTitle());
        assertEquals(category, resources.get(0).getProducts().get(0).getCategory());
        assertEquals(category, resources.get(0).getProducts().get(1).getCategory());

    }

    @Test
    public void shouldUpdateCategory() {

        categoryService.createResource(category);

        category.setTitle("new title");

        categoryService.updateResource(category.getId(), category);

        List<Category> updatedResources = categoryService.getResources();

        assertEquals(1, updatedResources.size());
        assertEquals("new title", updatedResources.get(0).getTitle());
    }

    @Test
    public void shouldUpdateCategoryWithProducts() {

        category.getProducts().add(p1);
        category.getProducts().add(p2);

        categoryService.createResource(category);

        category.getProducts().remove(p2);
        category.getProducts().add(p3);

        categoryService.updateResource(category.getId(), category);

        List<Category> updatedResources = categoryService.getResources();

        assertEquals(1, updatedResources.size());
        assertEquals(categoryTitle, updatedResources.get(0).getTitle());
        assertEquals("p1", updatedResources.get(0).getProducts().get(0).getTitle());
        assertEquals(category, updatedResources.get(0).getProducts().get(0).getCategory());
        assertEquals("p3", updatedResources.get(0).getProducts().get(1).getTitle());
        assertEquals(category, updatedResources.get(0).getProducts().get(1).getCategory());
    }

    @Test
    public void shouldDeleteCategoryWithoutDeletingProducts() {

        category.getProducts().add(p1);
        category.getProducts().add(p2);
        category.getProducts().add(p3);

        categoryService.createResource(category);

        categoryService.deleteResource(category.getId());

        List<Product> products = productService.getResources();
        assertEquals(3, products.size());
        assertNull(products.get(0).getCategory());
        assertNull(products.get(1).getCategory());
        assertNull(products.get(2).getCategory());

        List<Category> categories = categoryService.getResources();
        assertEquals(0, categories.size());
    }


}