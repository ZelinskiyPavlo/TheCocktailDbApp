package com.test.presentation.adapter.binding

// TODO: 11.09.2020 перенсти кожен в свій модуль
//@BindingAdapter("bind:rv_cocktails", "bind:rv_adapterTag", "bind:rv_sortType")
//fun RecyclerView.setItems(
//    oldItems: List<CocktailModel>?,
//    oldTag: AdapterType?,
//    oldCocktailSortType: CocktailSortType?,
//    newItems: List<CocktailModel>?,
//    tag: AdapterType?,
//    cocktailSortType: CocktailSortType?
//) {
//    when (tag) {
//        AdapterType.SEARCH_ADAPTER ->
//            (adapter as SearchCocktailAdapter).setData(newItems ?: emptyList())
//        AdapterType.COCKTAIL_ADAPTER ->
//            (adapter as CocktailAdapter).setData(
//                oldItems, newItems, cocktailSortType ?: CocktailSortType.RECENT
//            )
//    }
//}
//
//enum class AdapterType {
//    SEARCH_ADAPTER,
//    COCKTAIL_ADAPTER
//}
//
//@BindingAdapter("bind:rv_cocktailDetailFragment_ingredients")
//fun RecyclerView.setIngredients(items: List<Ingredient>?) {
//    with(adapter as IngredientAdapter) {
//        setData(items ?: emptyList())
//    }
//}