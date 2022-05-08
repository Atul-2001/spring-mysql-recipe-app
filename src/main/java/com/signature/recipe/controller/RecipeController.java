package com.signature.recipe.controller;

import com.signature.recipe.data.RecipeDTO;
import com.signature.recipe.model.Recipe;
import com.signature.recipe.service.RecipeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping("/recipe")
public class RecipeController {

  private static final String RECIPE_FORM_URL = "recipe/form";

  private final RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping("/new")
  public String createRecipe(final Model model) {
    model.addAttribute("recipe", new RecipeDTO());
    return RECIPE_FORM_URL;
  }

  @GetMapping("/{id}/show")
  public String showById(@PathVariable String id, final Model model) {
    log.info("Get recipe for id " + id);
    model.addAttribute("recipe", recipeService.getById(Long.parseLong(id)));
    return "recipe/show";
  }

  @GetMapping("/{id}/update")
  public String updateRecipe(@PathVariable String id, final Model model) {
    model.addAttribute("recipe", recipeService.getById(Long.parseLong(id)).getDTO());
    return RECIPE_FORM_URL;
  }

  @PostMapping
  public String addOrUpdate(@Valid @ModelAttribute("recipe") final RecipeDTO recipeDTO, BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
      bindingResult.getAllErrors().forEach(objectError -> {
        log.debug(objectError.toString());
      });
      return RECIPE_FORM_URL;
    }

    final Recipe recipe = recipeService.save(recipeDTO);
    return "redirect:/recipe/".concat(String.valueOf(recipe.getId())).concat("/show");
  }

  @GetMapping("/{id}/delete")
  public String deleteRecipe(@PathVariable String id) {
    recipeService.deleteById(Long.parseLong(id));
    return "redirect:/";
  }
}