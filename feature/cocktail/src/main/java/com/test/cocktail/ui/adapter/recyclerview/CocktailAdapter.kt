package com.test.cocktail.ui.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.cocktail.R
import com.test.cocktail.model.sorttype.CocktailSortComparator
import com.test.cocktail.model.sorttype.CocktailSortType
import com.test.cocktail.ui.CocktailViewModel
import com.test.cocktail.ui.adapter.recyclerview.listener.impl.CocktailItemUserActionListenerImpl
import com.test.cocktail.ui.adapter.recyclerview.listener.impl.FavoriteCocktailUserActionListenerImpl
import com.test.cocktail.ui.adapter.recyclerview.listener.impl.HeaderItemUserActionListenerImpl
import com.test.presentation.adapter.recyclerview.CustomActionListener
import com.test.presentation.model.cocktail.CocktailModel
import java.util.*

class CocktailAdapter(
    private val viewModel: CocktailViewModel
) : RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {

    var cocktailWithHeader: TreeMap<String, MutableList<CocktailModel>>? = null
    private var headerPositionSet: TreeSet<Int> = TreeSet()

    private var oldFullCocktailList: List<Any> = emptyList()
    var newFullCocktailList: MutableList<Any> = emptyList<Any>().toMutableList()

    private val sortComparator = CocktailSortComparator()

    private var isFavoriteLayout: Boolean? = null
        set(value) {
            if (field == null) field = value
        }

    val asFavorite: CocktailAdapter
        get() {
            isFavoriteLayout = true
            return this
        }

    // TODO: 12.02.2021 It it looks like shitty spaghetti code
    fun setData(
//        oldCocktails: List<CocktailModel>?,
        newCocktails: List<CocktailModel>?,
        sortType: CocktailSortType? = CocktailSortType.RECENT
    ) {
        fun setAlcoholData() {
            cocktailWithHeader = newCocktails
                ?.groupByTo(
                    if (sortType == CocktailSortType.ALCOHOL_FIRST) TreeMap<String,
                            MutableList<CocktailModel>>(sortComparator.alcoholComparator)
                    else TreeMap<String, MutableList<CocktailModel>>(sortComparator.reverseAlcoholComparator)
                ) { it.alcoholType.key }
                .also { map ->
                    map?.forEach { (_, cocktails) ->
                        cocktails.sortBy { it.names.defaults }
                    }
                }
            val cocktailHeaderFlatList = cocktailWithHeader?.flatMap { entry ->
                mutableListOf<String>().apply {
                    add(entry.key)
                    addAll(entry.value.map { it.names.defaults!! })
                }
            } ?: emptyList()

            cocktailWithHeader?.keys?.forEach { key ->
                headerPositionSet.add(cocktailHeaderFlatList.indexOf(key))
            }
        }

        fun setDataByName() {
            cocktailWithHeader = newCocktails
                ?.groupByTo(
                    if (sortType == CocktailSortType.NAME_ASC) TreeMap<String, MutableList<CocktailModel>>(
                        compareBy { it })
                    else TreeMap<String, MutableList<CocktailModel>>(compareByDescending { it })
                ) { it.names.defaults?.get(0).toString() }
                .also { map ->
                    map?.forEach { (_, cocktails) ->
                        cocktails.sortBy { it.names.defaults }
                    }

                }
            val cocktailHeaderFlatList = cocktailWithHeader?.flatMap { entry ->
                mutableListOf<String>().apply {
                    add(entry.key)
                    addAll(entry.value.map { it.names.defaults!! })
                }
            } ?: emptyList()

            cocktailWithHeader?.keys?.forEach { key ->
                headerPositionSet.add(cocktailHeaderFlatList.indexOf(key))
            }
        }

        oldFullCocktailList = newFullCocktailList
        cocktailWithHeader = null
        headerPositionSet = TreeSet()

        when (sortType) {
            CocktailSortType.ALCOHOL_FIRST, CocktailSortType.NON_ALCOHOL_FIRST ->
                setAlcoholData()
            CocktailSortType.NAME_ASC, CocktailSortType.NAME_DESC ->
                setDataByName()
            CocktailSortType.INGREDIENT_ASC, CocktailSortType.INGREDIENT_DESC,
            CocktailSortType.RECENT, null -> Unit
        }

        newFullCocktailList =
            if (headerPositionSet.isEmpty() || isFavoriteLayout == true)
                newCocktails?.toMutableList() ?: emptyList<Any>().toMutableList()
            else {
                cocktailWithHeader?.flatMap { entry ->
                    mutableListOf<Any>().apply {
                        add(entry.key)
                        addAll(entry.value)
                    }
                }?.toMutableList() ?: emptyList<Any>().toMutableList()
            }

        val productDiffResult = DiffUtil.calculateDiff(
            CocktailDiffCallback(oldFullCocktailList, newFullCocktailList)
        )
        productDiffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val inflatedView: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return CocktailViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        fun bindHeaderData() {
            val header = newFullCocktailList[position] as String
            val listener = HeaderItemUserActionListenerImpl()
            holder.bind(header, listener)
        }

        fun bindCocktailData() {
            val cocktail = newFullCocktailList[position] as CocktailModel
            val listener = CocktailItemUserActionListenerImpl(viewModel)
            holder.bind(cocktail, listener)
        }

        fun bindFavoriteCocktailData() {
            val cocktail = newFullCocktailList[position] as CocktailModel
            val listener = FavoriteCocktailUserActionListenerImpl(viewModel)
            holder.bind(cocktail, listener)
        }

        when {
            isFavoriteLayout == true -> bindFavoriteCocktailData()
            newFullCocktailList[position] is String -> bindHeaderData()
            newFullCocktailList[position] is CocktailModel -> bindCocktailData()
            else -> throw IllegalStateException("Cannot find desired view type to bound of")
        }
    }

    override fun onBindViewHolder(
        holder: CocktailViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        } else {
            holder.bind(
                newFullCocktailList[position],
                CocktailItemUserActionListenerImpl(viewModel)
            )
        }
    }

    override fun getItemCount(): Int = newFullCocktailList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            isFavoriteLayout == true -> R.layout.item_cocktail_favorite
            newFullCocktailList[position] is String -> R.layout.item_cocktail_header
            newFullCocktailList[position] is CocktailModel -> R.layout.item_cocktail
            else -> throw IllegalStateException("Unknown view type specified")
        }
    }

    class CocktailViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun <T> bind(item: T, listener: CustomActionListener) {
            binding.setVariable(BR.obj, item)
            binding.setVariable(BR.listener, listener)

            binding.executePendingBindings()
        }
    }
}