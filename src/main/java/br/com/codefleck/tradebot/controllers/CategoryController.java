package br.com.codefleck.tradebot.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import br.com.codefleck.tradebot.daos.CategoryDao;
import br.com.codefleck.tradebot.models.Category;

@Controller
@RequestMapping("/category")
@Transactional
public class CategoryController
{

   @Autowired
   private CategoryDao categoryDao;

   @GetMapping("/form")
   public ModelAndView form(Category category)
   {
      ModelAndView modelAndView = new ModelAndView("category/form-add");
      return modelAndView;

   }

   @PostMapping
   public ModelAndView save(@Valid Category category, BindingResult bindingResult)
   {
      if (bindingResult.hasErrors())
      {
         return form(category);
      }
      categoryDao.save(category);
      return new ModelAndView("redirect:/category");
   }

   @GetMapping("/{id}")
   public ModelAndView load(@PathVariable("id") Integer id)
   {
      ModelAndView modelAndView = new ModelAndView("category/form-update");
      modelAndView.addObject("category", categoryDao.findById(id));
      return modelAndView;
   }

   @GetMapping
   public ModelAndView list(@RequestParam(defaultValue = "0", required = false) int page)
   {
      ModelAndView modelAndView = new ModelAndView("category/list");
      modelAndView.addObject("paginatedList", categoryDao.paginated(page, 10));
      return modelAndView;
   }

   //just because get is easier here. Be my guest if you want to change.
   @GetMapping("/remove/{id}")
   public String remove(@PathVariable("id") Integer id)
   {
      Category category = categoryDao.findById(id);
      categoryDao.remove(category);
      return "redirect:/category";
   }

   @PostMapping("/{id}")
   public ModelAndView update(@PathVariable("id") Integer id, @Valid Category category, BindingResult bindingResult)
   {
      category.setId(id);
      if (bindingResult.hasErrors())
      {
         return new ModelAndView("category/form-update");
      }
      categoryDao.update(category);
      return new ModelAndView("redirect:/category");
   }
}
