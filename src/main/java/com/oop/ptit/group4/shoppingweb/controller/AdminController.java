package com.oop.ptit.group4.shoppingweb.controller;

import com.oop.ptit.group4.shoppingweb.constants.Pages;
import com.oop.ptit.group4.shoppingweb.constants.PathConstants;
import com.oop.ptit.group4.shoppingweb.domain.Order;
import com.oop.ptit.group4.shoppingweb.domain.OrderItem;
import com.oop.ptit.group4.shoppingweb.domain.Product;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.dto.request.ProductRequest;
import com.oop.ptit.group4.shoppingweb.dto.request.SearchRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;
import com.oop.ptit.group4.shoppingweb.dto.response.UserInfoResponse;
import com.oop.ptit.group4.shoppingweb.service.AdminService;
import com.oop.ptit.group4.shoppingweb.service.OrderItemService;
import com.oop.ptit.group4.shoppingweb.service.ProductService;
import com.oop.ptit.group4.shoppingweb.service.UserService;
import com.oop.ptit.group4.shoppingweb.service.impl.OrderService;
import com.oop.ptit.group4.shoppingweb.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(PathConstants.ADMIN)
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Lấy số lượng người dùng
            List<User> users;
            try {
                users = userService.findAll();
            } catch (Exception e) {
                users = Collections.emptyList();
                model.addAttribute("userError", "Lỗi khi lấy danh sách người dùng: " + e.getMessage());
            }
            model.addAttribute("totalUsers", users.size());
            model.addAttribute("users", users);

            // Lấy số lượng xe
            List<Product> products;
            try {
                products = productService.findAll();
            } catch (Exception e) {
                products = Collections.emptyList();
                model.addAttribute("carError", "Lỗi khi lấy danh sách xe: " + e.getMessage());
            }
            model.addAttribute("totalProducts", products.size());
            model.addAttribute("products", products);

            // Thêm các thuộc tính cho layout
            model.addAttribute("pageTitle", "Dashboard");
            model.addAttribute("active", "dashboard");

            return "admin/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi máy chủ: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());
            return "error";
        }
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        try {
            List<User> users = userService.findAll();
            model.addAttribute("users", users);

            // Thêm các thuộc tính cho layout
            model.addAttribute("pageTitle", "Quản lý người dùng");
            model.addAttribute("active", "users");

            return "admin/users";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi máy chủ: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());
            return "error";
        }
    }

    @GetMapping("/products")
    public String listCars(Model model) {
        try {
            // Lấy danh sách xe từ service
            List<Product> products = productService.findAll();
            // Nếu danh sách null thì gán thành danh sách rỗng để tránh lỗi NullPointerException
            if (products == null) {
                products = Collections.emptyList();
            }

            // Thêm danh sách xe vào model
            model.addAttribute("products", products);
            // Thêm các thuộc tính cho layout
            model.addAttribute("pageTitle", "Quản lý sản phẩm");
            model.addAttribute("active", "products");

            return "admin/laptops";
        } catch (Exception e) {
            // Ghi log lỗi
            e.printStackTrace();

            // Thêm thông tin lỗi vào model
            model.addAttribute("error", "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());

            // Thêm các thuộc tính cần thiết cho layout error
            model.addAttribute("pageTitle", "Lỗi - Quản lý sản phẩm");

            // Trả về trang lỗi
            return "error";
        }
    }
    @GetMapping("/order-items")
    public String listOrderItems(Model model) {
        try {
            // Lấy danh sách đơn hàng từ service
            List<OrderItem> orderItems = orderItemService.findAll();
            // Nếu danh sách null thì gán thành danh sách rỗng để tránh lỗi NullPointerException
            if (orderItems == null) {
                orderItems = Collections.emptyList();
            }

            // Thêm danh sách đơn hàng vào model
            model.addAttribute("orderItems", orderItems);
            // Thêm các thuộc tính cho layout
            model.addAttribute("pageTitle", "Quản lý đơn hàng");
            model.addAttribute("active", "order-items");

            return "admin/orders";
        } catch (Exception e) {
            // Ghi log lỗi
            e.printStackTrace();

            // Thêm thông tin lỗi vào model
            model.addAttribute("error", "Lỗi khi tải danh sách đơn hàng: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());

            // Thêm các thuộc tính cần thiết cho layout error
            model.addAttribute("pageTitle", "Lỗi - Quản lý đơn hàng");

            // Trả về trang lỗi
            return "error";
        }
    }

    @GetMapping("/orders")
    public String listOrders(Model model){
        try {
            // Lấy danh sách đơn hàng từ service
            List<Order> orders = orderService.findAllOrders();
            // Nếu danh sách null thì gán thành danh sách rỗng để tránh lỗi NullPointerException
            if (orders == null) {
                orders = Collections.emptyList();
            }

            // Thêm danh sách đơn hàng vào model
            model.addAttribute("orders", orders);
            // Thêm các thuộc tính cho layout
            model.addAttribute("pageTitle", "Quản lý đơn hàng");
            model.addAttribute("active", "orders");

            return "admin/order-items";
        } catch (Exception e) {
            // Ghi log lỗi
            e.printStackTrace();

            // Thêm thông tin lỗi vào model
            model.addAttribute("error", "Lỗi khi tải danh sách đơn hàng: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());

            // Thêm các thuộc tính cần thiết cho layout error
            model.addAttribute("pageTitle", "Lỗi - Quản lý đơn hàng");

            // Trả về trang lỗi
            return "error";
        }
    }
//    @GetMapping("/products")
//    public String getProducts(Pageable pageable, Model model) {
//        controllerUtils.addPagination(model, adminService.getProducts(pageable));
//        return Pages.ADMIN_LAPTOPS;
//    }

    @GetMapping("/products/search")
    public String searchProducts(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchProducts(request, pageable));
        return Pages.ADMIN_LAPTOPS;
    }

//    @GetMapping("/users")
//    public String getUsers(Pageable pageable, Model model) {
//        controllerUtils.addPagination(model, adminService.getUsers(pageable));
//        return Pages.ADMIN_ALL_USERS;
//    }

    @GetMapping("/users/search")
    public String searchUsers(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchUsers(request, pageable));
        return Pages.ADMIN_ALL_USERS;
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", adminService.getOrder(orderId));
        return Pages.ORDER;
    }

//    @GetMapping("/orders")
//    public String getOrders(Pageable pageable, Model model) {
//        controllerUtils.addPagination(model, adminService.getOrders(pageable));
//        return Pages.ORDERS;
//    }

    @GetMapping("/orders/search")
    public String searchOrders(SearchRequest request, Pageable pageable, Model model) {
        controllerUtils.addPagination(request, model, adminService.searchOrders(request, pageable));
        return Pages.ORDERS;
    }

    @GetMapping("/product/{productId}") // doi ten sau
    public String getProduct(@PathVariable Long productId, Model model) {
        model.addAttribute("product", adminService.getProductById(productId));
        return Pages.ADMIN_EDIT_LAPTOP;
    }

    @GetMapping("/edit/products/{productId}")
    public String editProduct(@PathVariable Long productId, Model model){
        model.addAttribute("product", adminService.getProductById(productId));
        return "admin-edit-product";
    }
    @PostMapping("/edit/products")
    public String editProduct(@Valid ProductRequest product, BindingResult bindingResult, Model model,
                          @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (controllerUtils.validateInputFields(bindingResult, model, "product", product)) {
            return "admin-edit-product";
        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/products", adminService.editProduct(product, file));
    }

    @GetMapping("/add/product")
    public String addProduct() {
        return Pages.ADMIN_ADD_LAPTOP;
    }

    @PostMapping("/add/product")
    public String addProduct(@Valid ProductRequest product, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
//        if (controllerUtils.validateInputFields(bindingResult, model, "product", product)) {
//            return Pages.ADMIN_ADD_LAPTOP;
//        }
        return controllerUtils.setAlertFlashMessage(attributes, "/admin/products", adminService.addProduct(product, file));
    }

    @GetMapping("/user/{userId}")
    public String getUserById(@PathVariable Long userId, Model model, Pageable pageable) {
        UserInfoResponse userResponse = adminService.getUserById(userId, pageable);
        model.addAttribute("user", userResponse.getUser());
        controllerUtils.addPagination(model, userResponse.getOrders());
        return Pages.ADMIN_USER_DETAIL;
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Thêm người dùng mới");
        return "admin/add-user";
    }

    @PostMapping("/users/add")
    public String addUser(User user, RedirectAttributes redirectAttributes) {
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/admin/users", adminService.addUser(user));
    }

    @GetMapping("/lock/user/{id}")
    public String lockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/admin/users", adminService.lockUser(id));
    }

    @GetMapping("/unlock/user/{id}")
    public String unlockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/admin/users", adminService.unlockUser(id));
    }

    @GetMapping("/order/confirm/{id}")
    public String confirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/admin/orders", adminService.confirmOrder(id));
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return controllerUtils.setAlertFlashMessage(redirectAttributes, "/admin/orders", adminService.deleteOrder(id));
    }
}
