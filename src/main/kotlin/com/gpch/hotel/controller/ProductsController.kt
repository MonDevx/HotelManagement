package com.gpch.hotel.controller

import com.gpch.hotel.model.Product
import com.gpch.hotel.service.DashboardService
import com.gpch.hotel.service.ProductService
import com.gpch.hotel.service.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class ProductsController @Autowired constructor(
    private val productService: ProductService,
    private val storeService: StoreService,
    private val dashboardService: DashboardService,
    private val messageSource: MessageSource
) {

    @RequestMapping(value = ["products/manage-products"], method = [RequestMethod.GET])
    fun products(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("product", Product())
        modelAndView.addObject("list", productService.findAll())
        modelAndView.addObject("liststores", storeService.findAll())
        if (dashboardService.countStores() == 0L) {
            modelAndView.addObject("status", "off")
        }
        modelAndView.viewName = "products/manage-products"
        return modelAndView
    }

    @RequestMapping(value = ["products/delete_product{id}"], method = [RequestMethod.GET])
    fun deleteProduct(
        @RequestParam(name = "id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        productService.deleteProductById(id)
        redirectAttributes.addFlashAttribute(
            "message",
            messageSource.getMessage("product.d.s.text", null, LocaleContextHolder.getLocale())
        )
        return "redirect:/products/manage-products"
    }

    @RequestMapping(value = ["products/edit_product{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun findProduct(@RequestParam(name = "id") id: Long): Product? = productService.findProductById(id)

    @RequestMapping(value = ["products/updateproduct{id}"], method = [RequestMethod.POST])
    fun updateProduct(
        @Valid product: Product,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        @RequestParam(name = "id") id: Long
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            product.id = id
            productService.updateProduct(product)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("product.u.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/products/manage-products"
        return modelAndView
    }

    @RequestMapping(value = ["products/add_product"], method = [RequestMethod.POST])
    fun addProduct(
        @Valid product: Product,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): ModelAndView {
        val modelAndView = ModelAndView()
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message_error", bindingResult.allErrors)
        } else {
            productService.saveProduct(product)
            redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("product.a.s.text", null, LocaleContextHolder.getLocale())
            )
        }
        modelAndView.viewName = "redirect:/products/manage-products"
        return modelAndView
    }
}
