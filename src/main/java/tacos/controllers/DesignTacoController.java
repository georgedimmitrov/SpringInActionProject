package tacos.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import tacos.models.Ingredient;
import tacos.models.Taco;
import tacos.models.Ingredient.Type;
import tacos.models.Order;
import tacos.repositories.IngredientRepository;
import tacos.repositories.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {
	
	private IngredientRepository ingredientRepo;
	private TacoRepository designRepo;
	
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo,
			TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),
					filterByType(ingredients, type));
		}
		model.addAttribute("design", new Taco());
		
		return "design";
	}
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@PostMapping
	public String processDesign(
			@Valid Taco design,
			Errors errors,
			@ModelAttribute Order order) {
		if (errors.hasErrors()) {
			return "design";
		}
		
		Taco saved = designRepo.save(design);

		// save the taco design
		log.info("Processing design: " + design);
		
		return "redirect:/orders/current";
	}
	
	private List<Ingredient> filterByType(List<Ingredient> ingredients,
			Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
