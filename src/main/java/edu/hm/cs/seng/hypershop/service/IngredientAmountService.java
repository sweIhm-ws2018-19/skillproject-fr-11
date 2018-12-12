package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;
import java.util.Set;

public class IngredientAmountService {

    public IngredientAmount mergeIngredients(final IngredientAmount firstIngredient,
                                             final IngredientAmount secondIngredient) {
        throw new NotImplementedException();
    }

    private StringBuilder getIngredientsString(Set<IngredientAmount> ingredientAmountSet, StringBuilder sb) {
        for (IngredientAmount ie : ingredientAmountSet) {
            Optional<IngredientAmount> firstIngredient = ingredientAmountSet.stream().findFirst();
            if (firstIngredient.isPresent() && ie != firstIngredient.get()) {
                sb.append(", ");
            }
            addIngredientToStringBuilder(sb, ie);
        }
        return sb;
    }

    private void addIngredientToStringBuilder(final StringBuilder sb, IngredientAmount ia) {
        sb.append("<say-as interpret-as=\"number\">");
        sb.append(fmt(ia.getAmount()));
        sb.append("</say-as>");
        sb.append(" ");
        if (!ia.getUnit().equalsIgnoreCase(HypershopCustomUnits.PIECE.getSymbol())) {
            sb.append("<say-as interpret-as=\"unit\">");
            sb.append(ia.getUnit());
            sb.append("</say-as>");
        }
        sb.append(" ");
        sb.append(ia.getName());
    }

    private static String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }


}
