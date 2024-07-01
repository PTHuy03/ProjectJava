package com.example.DAJava.Controller;

import com.example.DAJava.Model.Product;
import com.example.DAJava.Service.CategoryService;
import com.example.DAJava.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "/products/products-list";
    }

    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
        }
        return "products/page-details";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "query", required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            List<Product> searchResults = productService.searchProducts(query);
            model.addAttribute("products", searchResults);
        } else {
            List<Product> allProducts = productService.getAllProducts();
            model.addAttribute("products", allProducts);
        }
        return "/products/products-list"; // Trả về view products.html
    }


    // For adding a new product
    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        //Load categories
        return "/products/add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }

        // Lưu tệp hình ảnh nếu có
        if (!imageFile.isEmpty()) {
            try {
                // Đảm bảo rằng đường dẫn lưu trữ hình ảnh là chính xác
                String uploadDir = "src/main/resources/static/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String originalFileName = imageFile.getOriginalFilename();
                String fileName = "product_" + System.currentTimeMillis() + "_" + originalFileName;
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);

                // Cập nhật đường dẫn hình ảnh của sản phẩm
                product.setImage( fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi khi không thể lưu file
            }
        }

        // Lưu sản phẩm vào cơ sở dữ liệu
        productService.addProduct(product);
        return "redirect:/products";
    }

    // For editing a product
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        //Load categories
        return "/products/update-product";
    }

    // Process the form for updating a product
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, BindingResult result, @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";
        }
        // Lưu tệp hình ảnh nếu có thay đổi
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);

                // Lưu tên tệp hoặc đường dẫn vào trường image
                product.setImage("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi tại đây
            }
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // Handle request to delete a product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/mu-em-be")
    public String muEmBe(Model model) {
        // Lấy dữ liệu sản phẩm và lọc theo tên "Mũ em bé"
        List<Product> products = productService.findByNameContaining("Mũ em bé");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/kyt-tiger-yet")
    public String kytTigerYet(Model model) {
        // Lấy dữ liệu sản phẩm và lọc theo tên "KYT Tiger Yet"
        List<Product> products = productService.findByNameContaining("KYT Tiger Yet");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/poc-p05")
    public String pocP05(Model model) {
        // Lấy dữ liệu sản phẩm và lọc theo tên "POC P05"
        List<Product> products = productService.findByNameContaining("POC P05");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/poc-p07")
    public String pocP07(Model model) {
        // Lấy dữ liệu sản phẩm và lọc theo tên "POC P07"
        List<Product> products = productService.findByNameContaining("POC P07");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/poc-p20")
    public String pocP20(Model model) {
        // Lấy dữ liệu sản phẩm và lọc theo tên "POC P20"
        List<Product> products = productService.findByNameContaining("POC P20");
        model.addAttribute("products", products);
        return "/products/products-list";
    }
    // Mũ 3/4 routes
    @GetMapping("/mu3-4-ls2")
    public String ls23_4(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ 3/4 LS2");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu3-4-kyt")
    public String kyt3_4(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ 3/4 KYT");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu3-4-pull-dog")
    public String pullDog3_4(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ 3/4 Pull Dog");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu3-4-royal")
    public String royal3_4(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ 3/4 Royal");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu3-4-roc")
    public String roc3_4(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ 3/4 ROC");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    // Mũ Fullface routes
    @GetMapping("/fullface-ls2")
    public String fullfaceLS2(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ Fullface LS2");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/fullface-yohe")
    public String fullfaceYohe(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ Fullface Yohe");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/fullface-kyt")
    public String fullfaceKyt(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ Fullface KYT");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/fullface-agv")
    public String fullfaceAgv(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ Fullface AGV");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/fullface-roc")
    public String fullfaceRoc(Model model) {
        List<Product> products = productService.findByNameContaining("Mũ Fullface ROC");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    // Phụ kiện routes
    @GetMapping("/ao-giap")
    public String aoGiap(Model model) {
        List<Product> products = productService.findByNameContaining("Áo giáp");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/kinh-mu-bao-hiem")
    public String kinhMuBaoHiem(Model model) {
        String categoryName = "PhuKien"; // category name in Vietnamese
        String productName = "Kinh"; // product name contains this string

        List<Product> products = productService.findByCategoryNameAndProductNameContaining(categoryName, productName);
        model.addAttribute("products", products);
        return "products/products-list";
    }

    @GetMapping("/tem-decal")
    public String temDecal(Model model) {
        String categoryName = "PhuKien"; // category name in Vietnamese
        String productName = "Tem Decal"; // product name contains this string

        List<Product> products = productService.findByCategoryNameAndProductNameContaining(categoryName, productName);
        model.addAttribute("products", products);
        return "products/products-list";
    }

    @GetMapping("/gang-tay")
    public String gangTay(Model model) {
        List<Product> products = productService.findByNameContaining("Găng tay");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/fullface")
    public String fullface(Model model) {
        List<Product> products = productService.findByCategoryName("MuFullface");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu1-2")
    public String mu1_2(Model model) {
        List<Product> products = productService.findByCategoryName("Mu1-2");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/mu3-4")
    public String mu3_4(Model model) {
        List<Product> products = productService.findByCategoryName("Mu3-4");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/givi")
    public String GiVi(Model model) {
        List<Product> products = productService.findByCategoryName("GiVi");
        model.addAttribute("products", products);
        return "/products/products-list";
    }

    @GetMapping("/phu-kien")
    public String phukien(Model model) {
        List<Product> products = productService.findByCategoryName("PhuKien");
        model.addAttribute("products", products);
        return "/products/products-list";
    }
}