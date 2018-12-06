package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ShoppingListService {

    private final UnitConversionService unitConversionService = new UnitConversionService();
    private ShoppingList shoppingList;

    public ShoppingListService(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ShoppingListService(ModelService modelService) {
        load(modelService);
    }

    public void addIngredient(IngredientAmount ingredientAmount) {
        throw new UnsupportedOperationException("not implemented");
    }

    private Set<IngredientAmount> summarizeIngredients() {
        return new HashSet<>();
    }

    public boolean removeIngredient(String ingredient) {
        return shoppingList.getIngredients().removeIf(i -> i.getName().equalsIgnoreCase(ingredient));
    }

    public boolean removeIngredientRecipe(String ingredient, String recipeName) {
        final Recipe recipe = getRecipe(recipeName);
        if (recipe == null) {
            return false;
        }
        return recipe.getIngredients().removeIf(i -> i.getName().equalsIgnoreCase(ingredient));
    }

    private Stream<Map.Entry<Recipe, Integer>> getFilteredRecipeStream(String recipeName) {
        return shoppingList.getRecipes().entrySet().stream().filter(es -> es.getKey().getName().equalsIgnoreCase(recipeName));
    }

    public boolean containsRecipe(String recipeName) {
        return getFilteredRecipeStream(recipeName).findAny().isPresent();
    }

    /**
     * Creates a recipe and overrides it if it exists (create a map key with value 0)
     *
     * @param recipeName the name of the recipe to create
     */
    public void createRecipe(String recipeName) {
        shoppingList.getRecipes().put(new Recipe(recipeName), 0);
    }

    /**
     * Removes the recipe from the list of available recipes (drops the map key)
     *
     * @param recipeName the recipe name to remove from the list
     * @return true if a recipe was deleted
     * @throws IllegalArgumentException when an illegal amount is given
     */
    public boolean deleteRecipe(String recipeName) throws IllegalArgumentException {
        return shoppingList.getRecipes().entrySet().removeIf(es -> es.getKey().getName().equalsIgnoreCase(recipeName));
    }

    /**
     * Adds a existing recipe to the shoppingList amount times
     *
     * @param recipeName an existing recipe name
     * @param amount     how many times to add the recipe to the shoppingList
     * @return whether any recipes were added to the shoppingList
     * @throws IllegalArgumentException when an illegal amount is given
     */
    public boolean addRecipes(String recipeName, int amount) throws IllegalArgumentException {
        if (amount < 1) {
            throw new IllegalArgumentException("illegal amount");
        }
        final long count = getFilteredRecipeStream(recipeName)
                .peek(es -> es.setValue(es.getValue() + amount)).count();
        return count > 0;
    }

    /**
     * Removes an existing recipe from the shoppingList, but not from the list of available recipes
     *
     * @param recipeName the name of the recipe to remove amount times
     * @param amount     how many times to remove the recipe
     * @return whether any recipes were removed
     * @throws IllegalArgumentException when an illegal amount is given
     */
    public boolean removeRecipes(String recipeName, int amount) throws IllegalArgumentException {
        if (amount < 1) {
            throw new IllegalArgumentException("illegal amount");
        }
        final long count = getFilteredRecipeStream(recipeName)
                .peek(es -> es.setValue(es.getValue() - amount > 0 ? es.getValue() - amount : 0)).count();
        return count > 0;
    }

    public List<String> getRecipeStrings() {
        return shoppingList.getRecipes().entrySet().stream().map(es -> es.getKey().getName()).collect(Collectors.toList());
    }

    public List<String> getAddedRecipeStrings() {
        return shoppingList.getRecipes().entrySet().stream()
                .filter(es -> es.getValue() > 0)
                .map(es -> es.getKey().getName())
                .collect(Collectors.toList());
    }

    public List<Map.Entry<String, Integer>> getAddedRecipesWithAmount() {
        return shoppingList.getRecipes().entrySet().stream()
                .filter(es -> es.getValue() > 0)
                .map(es -> new AbstractMap.SimpleEntry<>(es.getKey().getName(), es.getValue()))
                .collect(Collectors.toList());
    }

    public void addIngredient(String name, int amount, String unitName) {
        boolean matchingIngredient = (shoppingList.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equals(name))
                .map(ingredientAmount ->
                        unitConversionService.summmarizeIngredients(ingredientAmount, amount, unitName))
                .count()) > 0;

        if (!matchingIngredient) {
            IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
            shoppingList.addIngredient(ingredientAmount);
        }
    }

    public Recipe getRecipe(String nameSearchString) {
        List<Recipe> recipes = shoppingList.getRecipes().keySet().stream().filter(recipe -> recipe.getName().equals(nameSearchString))
                .collect(Collectors.toList());
        return recipes.size() == 1 ? recipes.get(0) : null;
    }


    public boolean addIngredientRecipe(String name, int amount, String unitName, String recipeName) {
        final Recipe recipe = getRecipe(recipeName);
        if (recipe == null) {
            return false;
        }
        boolean matchingIngredient = (recipe.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equals(name))
                .map(ingredientAmount ->
                        unitConversionService.summmarizeIngredients(ingredientAmount, amount, unitName))
                .count()) > 0;
        if (!matchingIngredient) {
            IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
            recipe.addIngredient(ingredientAmount);
        }
        return true;
    }

    public IngredientAmount createIngredient(String name, int amount, String unitName) {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName(name);
        ingredientAmount.setAmount(amount);

        unitConversionService.getUnit(unitName);
        ingredientAmount.setUnit(unitName);
        return ingredientAmount;
    }
    
    public List<String> getIngredientStrings() {
        return new ArrayList<>();
    }

    public Set<IngredientAmount> getIngredients() {
        return shoppingList.getIngredients();
    }

    public void load(ModelService modelService) {
        final ShoppingList shoppingList = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        if (shoppingList == null) {
            this.shoppingList = new ShoppingList();
        } else {
            this.shoppingList = shoppingList;
        }
    }

    public void save(ModelService modelService) {
        modelService.save(shoppingList);
    }

}
