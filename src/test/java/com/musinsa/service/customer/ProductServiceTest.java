package com.musinsa.service.customer;

import com.musinsa.dto.*;
import com.musinsa.dto.request.ProductRequest;
import com.musinsa.dto.response.BrandLowestPriceResponse;
import com.musinsa.dto.response.BrandMinMaxResponse;
import com.musinsa.dto.response.CategoryLowestPriceResponse;
import com.musinsa.dto.response.ProductResponse;
import com.musinsa.entity.Brand;
import com.musinsa.entity.Category;
import com.musinsa.entity.Product;
import com.musinsa.exception.ErrorMessageType;
import com.musinsa.exception.ApplicationException;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.ProductCustomRepositoryImpl;
import com.musinsa.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductCustomRepositoryImpl productCustomRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private LowestBrandDto lowestBrand; // 인터페이스 모킹
    @Mock
    private CaffeineCacheManager caffeineCacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카테고리별 최저가 조회")
    void getLowestPriceByCategory() {
        // Given
        LowestCategoryDto lowestProduct1 = new LowestCategoryDto(1, 1000);
        LowestCategoryDto lowestProduct2 = new LowestCategoryDto(2, 2000);

        CategoryLowestPriceDto product1 = new CategoryLowestPriceDto("category-1", "brand-1", 1000);
        CategoryLowestPriceDto product2 = new CategoryLowestPriceDto("category-2", "brand-2", 2000);

        // Mock
        when(productCustomRepository.findLowestProductByCategory())
                .thenReturn(Arrays.asList(lowestProduct1, lowestProduct2));
        when(productCustomRepository.findProductByCategoryAndPrice(1, 1000))
                .thenReturn(product1);
        when(productCustomRepository.findProductByCategoryAndPrice(2, 2000))
                .thenReturn(product2);

        // When
        CategoryLowestPriceResponse response = productService.getLowestPriceByCategory();

        // Then
        assertNotNull(response);
        assertEquals(2, response.getProducts().size());
        assertEquals(3000, response.getTotalPrice());  // 총 가격 검증
    }

    @Test
    void getLowestPriceByBrand() {
        // Given
        BrandLowestPriceDto product1 = new BrandLowestPriceDto("category-1", 100);
        BrandLowestPriceDto product2 = new BrandLowestPriceDto("category-2", 150);
        BrandLowestPriceDto product3 = new BrandLowestPriceDto("category-2", 250);
        List<BrandLowestPriceDto> brandLowestPrices = Arrays.asList(product1, product2, product3);

        // Mock
        when(lowestBrand.getBrandId()).thenReturn(1L);
        when(lowestBrand.getBrandName()).thenReturn("brand-1");
        when(lowestBrand.getTotalPrice()).thenReturn(500L);
        when(productRepository.findLowestBrand()).thenReturn(Optional.of(lowestBrand));
        when(productCustomRepository.findProductByBrand(1L)).thenReturn(brandLowestPrices);

        // When
        BrandLowestPriceResponse response = productService.getLowestPriceByBrand();

        // Then
        assertNotNull(response);
        assertEquals("brand-1", response.getBrandLowestPrice().getBrandName());
        assertEquals(500, response.getBrandLowestPrice().getTotalPrice());
        assertEquals(3, response.getBrandLowestPrice().getProducts().size());
    }

    @Test
    void getCategoryMinMaxBrand() {
        // Given
        String categoryName = "스니커즈";
        BrandMinMaxPriceDto minBrand = BrandMinMaxPriceDto.builder()
                .brandName("브랜드-1")
                .price(100)
                .build();
        BrandMinMaxPriceDto maxBrand = BrandMinMaxPriceDto.builder()
                .brandName("브랜드-2")
                .price(500)
                .build();

        // mock
        when(productCustomRepository.findCategoryMinBrand(categoryName)).thenReturn(minBrand);
        when(productCustomRepository.findCategoryMaxBrand(categoryName)).thenReturn(maxBrand);

        // when
        BrandMinMaxResponse response = productService.getCategoryMinMaxBrand(categoryName);

        // then
        assertNotNull(response);
        assertEquals(categoryName, response.getCategoryName());

        // 최소 가격
        assertEquals(minBrand.getBrandName(), response.getBrandMinPrice().getBrandName());
        assertEquals(minBrand.getPrice(), response.getBrandMinPrice().getPrice());

        // 최대 가격
        assertEquals(maxBrand.getBrandName(), response.getBrandMaxPrice().getBrandName());
        assertEquals(maxBrand.getPrice(), response.getBrandMaxPrice().getPrice());
    }

    @Test
    void createProduct_정상등록() {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(1L)
                .brandId(1L)
                .name("상품-1")
                .useYn(true)
                .build();

        Brand brand = new Brand(1L, "브랜드-1", true);
        Category category = new Category(1L, "스니커즈", true);
        Product product = Product.builder()
                .name(productRequest.getName())
                .useYn(productRequest.isUseYn())
                .category(category)
                .brand(brand)
                .build();

        // Mock
        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        ProductResponse response = productService.createProduct(productRequest);

        // then
        assertNotNull(response);
        assertEquals("상품-1", response.getName());
    }

    @Test
    void testCreateProduct_브랜드_또는_카테고리_없음() {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(1L)
                .brandId(1L)
                .name("상품-1")
                .useYn(true)
                .build();

        // mock
        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.empty());

        // 예외 검증
        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            productService.createProduct(productRequest);
        });

        // then
        Set<ErrorMessageType> expectedValues = Set.of(ErrorMessageType.BRAND_NOT_FOUND, ErrorMessageType.CATEGORY_NOT_FOUND);
        assertTrue(expectedValues.contains(exception.getErrorMessageType()), "예상되는 값이 없음.");
    }

    @Test
    void updateProduct() {

        // given
        Long productId = 1L;
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(1L)
                .brandId(1L)
                .name("상품-1")
                .useYn(true)
                .build();

        Brand brand = new Brand(1L, "브랜드-1", true);
        Category category = new Category(1L, "스니커즈", true);
        Product existingProduct = Product.builder()
                .id(productId)
                .name("있던상품-1")
                .useYn(false)
                .brand(brand)
                .category(category)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name(productRequest.getName())
                .useYn(productRequest.isUseYn())
                .brand(brand)
                .category(category)
                .build();

        // mock
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // when
        ProductResponse response = productService.updateProduct(productId, productRequest);

        // then
        assertNotNull(response);
        assertEquals(productRequest.getName(), response.getName());
        assertEquals(productRequest.isUseYn(), response.isUseYn());
    }

    @Test
    void deleteProduct() {
        // given
        Long productId = 1L;
        Brand brand = new Brand(1L, "브랜드-1", true);
        Category category = new Category(1L, "스니커즈", true);
        Product existingProduct = Product.builder()
                .id(productId)
                .name("있던상품-1")
                .useYn(true)
                .brand(brand)
                .category(category)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("있던상품-1")
                .useYn(false)
                .brand(brand)
                .category(category)
                .build();

        // mock
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // when
        ProductResponse response = productService.deleteProduct(productId);

        // then
        assertNotNull(response);
        assertFalse(response.isUseYn(), "사용 상태가 변경되지 않았습니다.");
    }
}
