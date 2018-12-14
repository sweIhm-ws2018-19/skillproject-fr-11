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

    public Set<IngredientAmount> summarizeIngredients() {
        Set<IngredientAmount> allIngredientAmounts = new HashSet<>(shoppingList.getIngredients());
        Set<IngredientAmount> allRecipeIngredients = new HashSet<>();
        shoppingList.getRecipes().forEach((k, v) -> addSeveralTimes(allRecipeIngredients, k.getIngredients(), v));

        for (IngredientAmount ia : allRecipeIngredients) {
            boolean matchingIngredient = (allIngredientAmounts.stream().filter(ingredientAmount ->
                    ingredientAmount.getName().equalsIgnoreCase(ia.getName())
                            && unitConversionService.canSummarize(ingredientAmount.getUnit(), ia.getUnit()))
                    .map(ingredientAmount ->
                            unitConversionService.summarizeIngredients(ingredientAmount, ia.getAmount(), ia.getUnit()))
                    .count()) > 0;

            if (!matchingIngredient) {
                allIngredientAmounts.add(ia);
            }
        }

        return allIngredientAmounts;
    }


    private void addSeveralTimes(final Set<IngredientAmount> allRecipeIngredients, Set<IngredientAmount> ingredientAmounts, int times) {
        for (IngredientAmount ingredientAmount : ingredientAmounts) {
            ingredientAmount.setAmount(ingredientAmount.getAmount() + times-1);
            allRecipeIngredients.add(ingredientAmount);
        }
    }

    public boolean removeIngredient(String ingredient) {
        return shoppingList.getIngredients().removeIf(i -> i.getName().equalsIgnoreCase(ingredient));
    }

    public boolean removeIngredientFromRecipe(String ingredient, String recipeName) {
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

    public boolean createRecipe(String recipeName) {
        if (shoppingList.getRecipes().keySet().stream().anyMatch(r -> r.getName().equalsIgnoreCase(recipeName))) {
            return false;
        }
        shoppingList.getRecipes().put(new Recipe(recipeName), 0);
        return true;
    }

    /**
     * Removes the recipe from the list of available recipes (drops the map key)
     *
     * @param recipeName the recipe name to remove from the list
     * @return true if a recipe was deleted
     */
    public boolean deleteRecipe(String recipeName) {
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
     */
    public boolean removeRecipes(String recipeName) {
        return getFilteredRecipeStream(recipeName).peek(recipeIntegerEntry -> recipeIntegerEntry.setValue(0)).count() > 0;
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

    public List<String> getAddedRecipeWithAmountStrings() {
        final List<String> list = new ArrayList<>();
        getAddedRecipesWithAmountStream().forEach(
                recipe -> list.add(recipe.getValue() + " Portionen " + recipe.getKey())
        );
        return list;
    }

    public List<Map.Entry<String, Integer>> getAddedRecipesWithAmount() {
        return getAddedRecipesWithAmountStream()
                .collect(Collectors.toList());
    }

    private Stream<Map.Entry<String, Integer>> getAddedRecipesWithAmountStream() {
        return shoppingList.getRecipes().entrySet().stream()
                .filter(es -> es.getValue() > 0)
                .map(es -> new AbstractMap.SimpleEntry<>(es.getKey().getName(), es.getValue()));
    }

    public void addIngredient(String name, int amount, String unitName) {
        boolean matchingIngredient = (shoppingList.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equalsIgnoreCase(name)
                && unitConversionService.canSummarize(ingredientAmount.getUnit(), unitName))
                .map(ingredientAmount ->
                        unitConversionService.summarizeIngredients(ingredientAmount, amount, unitName))
                .count()) > 0;

        if (!matchingIngredient) {
            IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
            shoppingList.addIngredient(ingredientAmount);
        }
    }

    public Recipe getRecipe(String nameSearchString) {
        List<Recipe> recipes = shoppingList.getRecipes().keySet().stream().filter(recipe -> recipe.getName().equalsIgnoreCase(nameSearchString))
                .collect(Collectors.toList());
        return recipes.size() == 1 ? recipes.get(0) : null;
    }


    public boolean addIngredientRecipe(String name, int amount, String unitName, String recipeName) {
        final Recipe recipe = getRecipe(recipeName);
        if (recipe == null) {
            return false;
        }
        boolean matchingIngredient = (recipe.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equalsIgnoreCase(name)
                && unitConversionService.canSummarize(ingredientAmount.getUnit(), unitName))
                .map(ingredientAmount ->
                        unitConversionService.summarizeIngredients(ingredientAmount, amount, unitName))
                .count()) > 0;
        if (!matchingIngredient) {
            IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
            recipe.addIngredient(ingredientAmount);
        }
        return true;
    }

    private IngredientAmount createIngredient(String name, int amount, String unitName) {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName(name);
        ingredientAmount.setAmount(amount);

        unitConversionService.getUnit(unitName);
        ingredientAmount.setUnit(unitName);
        return ingredientAmount;
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

    public void clearList() {
        shoppingList.getIngredients().clear();
        shoppingList.getRecipes().entrySet().forEach(es -> es.setValue(0));
    }
}
