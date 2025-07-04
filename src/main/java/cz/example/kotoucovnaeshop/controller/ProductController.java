package cz.example.kotoucovnaeshop.controller;

import cz.example.kotoucovnaeshop.model.Category;
import cz.example.kotoucovnaeshop.model.Product;
import cz.example.kotoucovnaeshop.service.CategoryService;
import cz.example.kotoucovnaeshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/{category}/produkt/{productName}")
    public String productDetail(@PathVariable String productName, Model model) {
        productName = productName.replaceAll("-", " ");
        Product product = productService.getProduct(productName);
        model.addAttribute("product", product);

        return "productDetail";
    }


    @GetMapping("/admin/products")
    public String searchProducts(@RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "category", required = false) String categoryName,
                                 Model model) {
        List<Product> products;
        Category selectedCategory = null;

        if (categoryName != null) {
            selectedCategory = categoryService.getCategoryByUrl(categoryName);

            if (search != null) {
                products = productService.getByNameAndCategory(search, selectedCategory, productService.getRepository().NAME_ASC);
            } else {
                products = productService.getAllProductsInCategory(selectedCategory, productService.getRepository().NAME_ASC);
            }
        } else {
            if (search != null) {
                products = productService.getByName(search, productService.getRepository().NAME_ASC);
            } else {
                products = productService.findAll();
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", selectedCategory);

        return "admin/productsList";
    }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        Product product = new Product();

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "admin/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@Valid Product product,
                             BindingResult bindingResult,
                             @ModelAttribute("image") MultipartFile imageFile,
                             Model model) throws IOException {
        if (imageFile.isEmpty()) {
            bindingResult.rejectValue("image", "error.product.image.empty", "Zvolte obrázek");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/addProduct";
        }

        product.setId(productService.save(product, imageFile));

        return "redirect:/admin/product/edit?product=" + product.getId();
    }

    @GetMapping("/admin/product/edit")
    public String editProduct(@RequestParam("product") Long productId, Model model) {
        Product product = productService.getProduct(productId);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "admin/editProduct";
    }

    @PostMapping("/admin/product/edit")
    public String editProduct(@Valid Product product,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("product") long productId) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productService.getProduct(productId));
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/editProduct";
        }
        product.setId(productId);

        productService.update(product);

        return "redirect:/admin/product/edit?product=" + productId;
    }

    @PostMapping("/admin/product/delete")
    public String deleteProduct(@ModelAttribute("productid") long productid) throws IOException {
        Product product = new Product();
        product.setId(productid);
        productService.delete(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/product/change-image")
    public String changeImage(long productId,
                              MultipartFile imageFile,
                              Model model) throws IOException {
        if (imageFile.isEmpty()) {
            Product product = productService.getProduct(productId);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("imageFileEmpty", true);
            return "admin/editProduct";
        }

        Product product = productService.getProduct(productId);
        productService.changeImage(imageFile, product);

        return "redirect:/admin/product/edit?product=" + productId;
    }
}
