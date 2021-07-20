package com.gpch.hotel.controller;

import com.gpch.hotel.model.Product;
import com.gpch.hotel.service.DashboardService;
import com.gpch.hotel.service.ProductService;
import com.gpch.hotel.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ProductsController {
    private ProductService productService;
    private StoreService storeService;
    private DashboardService dashboardService;
    private MessageSource messageSource;

    @Autowired
    public ProductsController(ProductService productService, StoreService storeService
            , DashboardService dashboardService, MessageSource messageSource) {
        this.productService = productService;
        this.storeService = storeService;
        this.dashboardService = dashboardService;
        this.messageSource = messageSource;
    }


    @RequestMapping(value = "products/manage-products", method = RequestMethod.GET)
    public ModelAndView Products() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product", new Product());
        modelAndView.addObject("list", productService.findAll());
        modelAndView.addObject("liststores", storeService.findAll());
        if (dashboardService.countStores() == 0) {
            modelAndView.addObject("status", "off");
        }
        modelAndView.setViewName("products/manage-products");
        return modelAndView;
    }

    @RequestMapping(value = "products/delete_product{id}", method = RequestMethod.GET)
    public String Deleteproduct(@RequestParam(name = "id") String id, RedirectAttributes redirectAttributes) {
        productService.DeleteProductById(id);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("product.d.s.text"
                , null, LocaleContextHolder.getLocale()));
        return "redirect:/products/manage-products";
    }

    @RequestMapping(value = "products/edit_product{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product FindProduct(@RequestParam(name = "id") String id) {
        return productService.FindProductById(id);
    }

    @RequestMapping(value = "products/updateproduct{id}", method = RequestMethod.POST)
    public ModelAndView Updateproduct(@Valid Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam(name = "id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            product.setId(id);
            productService.Updateproduct(product);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("product.u.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/products/manage-products");
        return modelAndView;
    }

    @RequestMapping(value = "products/add_product", method = RequestMethod.POST)
    public ModelAndView Addproducte(@Valid Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.getAllErrors());
        } else {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("product.a.s.text"
                    , null, LocaleContextHolder.getLocale()));
        }
        modelAndView.setViewName("redirect:/products/manage-products");
        return modelAndView;
    }
}
