package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Pair;
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

    Set<IngredientAmount> summarizeIngredients() {
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
            ingredientAmount.setAmount(ingredientAmount.getAmount() + times - 1);
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

    public boolean deleteRecipe(String recipeName) {
        return shoppingList.getRecipes().entrySet().removeIf(es -> es.getKey().getName().equalsIgnoreCase(recipeName));
    }

    public boolean recipeExists(String recipeName) {
        return shoppingList.getRecipes().entrySet().stream().anyMatch(es -> es.getKey().getName().equalsIgnoreCase(recipeName));
    }

    public boolean addRecipes(String recipeName, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("illegal amount");
        }
        final long count = getFilteredRecipeStream(recipeName)
                .filter(es -> {
                    es.setValue(es.getValue() + amount);
                    return true;
                }).count();
        return count > 0;
    }

    public boolean removeRecipes(String recipeName) {
        return getFilteredRecipeStream(recipeName).anyMatch(recipeIntegerEntry -> {
            recipeIntegerEntry.setValue(0);
            return true;
        });
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
        final ShoppingList newShoppingList = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        if (newShoppingList == null) {
            shoppingList = new ShoppingList();
        } else {
            shoppingList = newShoppingList;
        }
    }

    public void save(ModelService modelService) {
        modelService.save(shoppingList);
    }

    public void clearList() {
        shoppingList.getIngredients().clear();
        shoppingList.getRecipes().entrySet().forEach(es -> es.setValue(0));
    }

    private List<Pair<IngredientAmount, Boolean>> generateCheckingList() {
        final ArrayList<Pair<IngredientAmount, Boolean>> list = new ArrayList<>();
        final Set<IngredientAmount> ingredientAmounts = summarizeIngredients();
        for (IngredientAmount ia : ingredientAmounts) {
            list.add(new Pair<>(ia, false));
        }
        return list;
    }

    List<Pair<IngredientAmount, Boolean>> getOrGenerateCheckingList(HandlerInput input) {
        List<Pair<IngredientAmount, Boolean>> checkingList = SessionStorageService.getCheckingList(input);
        if (checkingList == null) {
            checkingList = generateCheckingList();
            SessionStorageService.storeCheckingList(input, checkingList);
        }
        return checkingList;
    }

    public boolean goToNextIngredient(HandlerInput input) {
        final List<Pair<IngredientAmount, Boolean>> checkingList = getOrGenerateCheckingList(input);

        if (checkingList.isEmpty()) {
            return false;
        }

        int index = SessionStorageService.getIngredientOutputIndex(input);
        if (index >= checkingList.size()) {
            index = 0;
        }

        final Pair<IngredientAmount, Boolean> current = checkingList.get(index);

        final List<Pair<IngredientAmount, Boolean>> filtered = checkingList.stream().filter(e -> !e.second).collect(Collectors.toList());
        final int filteredIndex = filtered.indexOf(current);

        if (filtered.isEmpty()) {
            return false;
        }

        final Pair<IngredientAmount, Boolean> next;
        if (filteredIndex + 1 < filtered.size()) {
            next = filtered.get(filteredIndex + 1);
        } else {
            next = filtered.get(0);
        }

        SessionStorageService.setIngredientOutputIndex(input, checkingList.indexOf(next));

        return true;
    }

    public String getCurrentIngredient(HandlerInput input) {
        final List<Pair<IngredientAmount, Boolean>> checkingList = getOrGenerateCheckingList(input);

        if (checkingList.isEmpty()) {
            return null;
        }

        final int index = SessionStorageService.getIngredientOutputIndex(input);
        try {
            final Pair<IngredientAmount, Boolean> pair = checkingList.get(index);
            return pair.first.toString();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean checkCurrentIngredient(HandlerInput input) {
        final List<Pair<IngredientAmount, Boolean>> checkingList = getOrGenerateCheckingList(input);

        if (checkingList.isEmpty()) {
            return false;
        }

        final int index = SessionStorageService.getIngredientOutputIndex(input);
        try {
            final Pair<IngredientAmount, Boolean> pair = checkingList.get(index);
            checkingList.set(index, new Pair<>(pair.first, true));
            SessionStorageService.storeCheckingList(input, checkingList);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }
}
