package com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.adapter.recyclerview.CustomActionListener
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.impl.CocktailItemUserActionListenerImpl
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.impl.FavoriteCocktailUserActionListenerImpl
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.impl.HeaderItemUserActionListenerImpl
import com.test.thecocktaildb.presentation.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.presentation.ui.cocktail.sorttype.CocktailSortComparator
import com.test.thecocktaildb.presentation.ui.cocktail.sorttype.CocktailSortType
import java.util.*

class CocktailAdapter(
    private val viewModel: SharedHostViewModel
) : RecyclerView.Adapter<CocktailAdapter.TestViewHolder>() {

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

    fun setData(
        oldCocktails: List<CocktailModel>?,
        newCocktails: List<CocktailModel>?,
        sortType: CocktailSortType = CocktailSortType.RECENT
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
            CocktailSortType.INGREDIENT_ASC, CocktailSortType.INGREDIENT_DESC, CocktailSortType.RECENT ->
                Unit
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val inflatedView: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return TestViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
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
        holder: TestViewHolder,
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

    class TestViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun <T> bind(item: T, listener: CustomActionListener) {
            binding.setVariable(BR.obj, item)
            binding.setVariable(BR.listener, listener)

            binding.executePendingBindings()
        }
    }

    class CocktailDiffCallback(
        private val oldCocktails: List<Any>,
        private val newCocktails: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldCocktails.size
        }

        override fun getNewListSize(): Int {
            return newCocktails.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldCocktail = oldCocktails[oldItemPosition]
            val newCocktail = newCocktails[newItemPosition]
            if (oldCocktail is String && newCocktail is String) {
                return oldCocktail == newCocktail
            }
            if (oldCocktail is CocktailModel && newCocktail is CocktailModel) {
                return oldCocktail.id == newCocktail.id
            }
            return false
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldCocktails[oldItemPosition]
            val new = newCocktails[newItemPosition]

            if (old is String && new is String) {
                return old == new
            }
            if (old is CocktailModel && new is CocktailModel) {
                return old.image == new.image
                        && old.names.defaults == new.names.defaults
                        && old.isFavorite == new.isFavorite
            }
            return false
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldCocktails[oldItemPosition]
            val newItem = newCocktails[newItemPosition]

            val diff = Bundle()
            if (oldItem is CocktailModel && newItem is CocktailModel) {
                if (newItem.isFavorite != oldItem.isFavorite) {
                    diff.putBoolean("isFavorite", newItem.isFavorite)
                }

                return if (diff.size() == 0) null
                else diff
            }
            return null
        }
    }
}