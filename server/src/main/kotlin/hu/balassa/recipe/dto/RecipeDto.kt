package hu.balassa.recipe.dto

import hu.balassa.recipe.model.Category
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class RecipeDto (
    val id: String?,
    @field: NotEmpty
    val name: String,
    @field: URL @field: NotEmpty
    val imageUrl: String?,
    @field: NotNull
    val quantity: Int,
    val quantity2: Int?,
    @field: NotNull
    val isVegetarian: Boolean,
    @field: NotEmpty
    val category: Category,
    @field: NotEmpty
    val ingredientGroups: List<IngredientGroupDto>,
    @field: NotEmpty
    val instructions: List<String>
)