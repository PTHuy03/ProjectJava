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

    // Display a list of all products
    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "/products/products-list";
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
}