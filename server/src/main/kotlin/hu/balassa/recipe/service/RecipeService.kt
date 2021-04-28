package hu.balassa.recipe.service

import hu.balassa.recipe.dto.NewStreetKitchenRecipe
import hu.balassa.recipe.exception.NotFoundException
import hu.balassa.recipe.model.Recipe
import hu.balassa.recipe.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(
        private val repository: RecipeRepository,
        private val imageUploadClient: ImageUploadClient,
        private val streetKitchenService: StreetKitchenService
) {

    fun getAllRecipes(): List<Recipe> = repository.findAll().toList()

    fun saveRecipe(recipe: Recipe): Recipe {
        if (recipe.imageUrl?.contains("amazonaws.com") != true) {
            recipe.imageUrl = imageUploadClient.uploadImageFromImageURL(recipe.imageUrl!!)
        }
        return repository.save(recipe)
    }

    fun addStreetKitchenRecipe(info: NewStreetKitchenRecipe): Recipe {
        val recipe = streetKitchenService.getRecipe(info.url)
        recipe.imageUrl = recipe.imageUrl?.let {
            imageUploadClient.uploadImageFromImageURL(it)
        }
        return repository.save(recipe)
    }

    fun deleteRecipe(id: String) = repository.deleteById(id)

    fun getRecipe(id: String): Recipe = repository.findById(id) ?: throw NotFoundException(id)

    fun filterRecipes(kw: List<String>): List<Recipe> = getAllRecipes()
//    = criteriaQuery(em) { root, cb ->
//        val keywords = kw.map { it.decapitalize() }
//        val ingredientGroups = root.join<Recipe, IngredientGroup>("ingredientGroups")
//        val ingredients = ingredientGroups.join<IngredientGroup, Ingredient>("ingredients")
//
//        val emptyPredicate = cb.isTrue(cb.literal(true))
//        val filter = keywords.fold(emptyPredicate) { predicate, keyword ->
//            cb.and(predicate, cb.or(
//                    cb.like(ingredients.get("name"), "%$keyword%"),
//                    cb.like(root.get("name"), "%${keyword}%")
//            ))
//        }
//        where(filter)
//    }
//
//    fun findRecipesByKeywords(kw: List<String>): List<Recipe> = criteriaQuery(em) { root, cb ->
//        val keywords = kw.map { it.decapitalize() }
//        val ingredientGroups = root.join<Recipe, IngredientGroup>("ingredientGroups")
//        val ingredients = ingredientGroups.join<IngredientGroup, Ingredient>("ingredients")
//
//        val emptyPredicate = cb.isTrue(cb.literal(false))
//        val filter = keywords.fold(emptyPredicate) { predicate, keyword ->
//            cb.or(predicate, cb.or(
//                    cb.like(ingredients.get("name"), "%$keyword%"),
//                    cb.like(root.get("name"), "%${keyword}%")
//            ))
//        }
//        where(filter)
//    }

//    fun findSimilarRecipes(id: Long): List<Recipe> {
//        val recipe = repository.findByIdOrNull(id)
//                ?: throw NotFoundException(id)
//        val keywords = recipe.name.split(" ").flatMap { it.split("-") }
//
//        val recipesByName = findRecipesByKeywords(keywords).filter {
//            it.id != recipe.id
//        }
//        if (recipesByName.size >= 3)
//            return recipesByName
//
//        return findSimilarRecipesByIngredients(id)
//
//    }
//
//    private fun findSimilarRecipesByIngredients(id: Long): List<Recipe> {
//        val ingredientGroups = repository.findById(id)?.ingredientGroups
//                ?: throw NotFoundException(id)
//        val ingredients = ingredientGroups.flatMap { it.ingredients }
//        val ingredientNames = ingredients.flatMap { ingredient ->
//            ingredient.name.split(" ").let {
//                if (ingredient.quantity == null || it.size == 1)
//                    it
//                else
//                    it.subList(1, it.size - 1)
//            }
//        }
//        return findRecipesByKeywords(ingredientNames)
//    }
//
//    private inline fun <reified ResultT> criteriaQuery(
//            em: EntityManager,
//            buildQuery: CriteriaQuery<ResultT>.(root: Root<ResultT>, cb: CriteriaBuilder) -> Unit
//    ): List<ResultT> {
//
//        val cb = em.criteriaBuilder
//        val cr = cb.createQuery(ResultT::class.java).distinct(true)
//        val root = cr.from(ResultT::class.java)
//
//        cr.buildQuery(root, cb)
//        val query = em.createQuery(cr)
//        return query.resultList
//    }
}