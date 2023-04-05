package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.CategoryService;
import cz.example.kotoucovnaeshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/addProduct";
    }
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product")Product product,
                             @ModelAttribute("image") MultipartFile image,
                             Model model) throws IOException {
        productService.uploadImage(image);
        productService.save(product);

        return "redirect:/admin/products";
    }
    @GetMapping("/admin/products")
    public String manageProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        System.out.println(products.size());
        return "admin/productsList";
    }
    @GetMapping("/admin/product/edit")
    public String editProduct(@RequestParam("product") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/editProduct";
    }
    @PostMapping("/admin/product/edit")
    public String editProduct(@ModelAttribute Product product, @RequestParam("product") long productId) {
        product.setId(productId);

        productService.update(product);

        return "categoryList";
    }
    @PostMapping("/admin/product/delete")
    public String deleteProduct(@ModelAttribute("productid") long productid) {
        Product product = new Product();
        product.setId(productid);
        productService.delete(product);
        System.out.println(product.getId());
        return "redirect:/admin/products";
    }

}
