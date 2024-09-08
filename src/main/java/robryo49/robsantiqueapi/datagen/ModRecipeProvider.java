package robryo49.robsantiqueapi.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    public Item get_item(String mod, String id) {
        return Registries.ITEM.get(new Identifier(mod, id));
    }

    public void create_tool_crafting_recipe(Consumer<RecipeJsonProvider> exporter,
            Item tool, Item ingredient, Item stick_item, List<String> pattern) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, tool, 1)
                .pattern(pattern.get(0))
                .pattern(pattern.get(1))
                .pattern(pattern.get(2))
                .input('#', ingredient)
                .input('/', stick_item)
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .criterion(hasItem(stick_item), conditionsFromItem(stick_item))
                .offerTo(exporter, new Identifier(getRecipeName(tool)));
    }

    public void create_tool_smithing_recipe(Consumer<RecipeJsonProvider> exporter,
                                            Item tool, Item source_tool, Item ingredient,
                                            Item smithing_template) {
        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(smithing_template), Ingredient.ofItems(source_tool),
                        Ingredient.ofItems(ingredient), RecipeCategory.TOOLS, tool
                )
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .offerTo(exporter, getItemPath(tool) + "_smithing");
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

    }
}