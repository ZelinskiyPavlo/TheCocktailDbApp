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

internal class CocktailAdapter(
    private val viewModel: CocktailViewModel
) : RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {

    var processedCocktailsList: List<Any> = emptyList()
        private set

    var headerWithCocktailsMap: TreeMap<String, MutableList<CocktailModel>>? = null
        private set

    private var previousProcessedCocktailsList: List<Any> = emptyList()

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
        newCocktails: List<CocktailModel>?,
        sortType: CocktailSortType? = CocktailSortType.RECENT
    ) {
        prepareVariables()
        processCocktailsList(newCocktails, sortType)

        DiffUtil.calculateDiff(
            CocktailDiffCallback(previousProcessedCocktailsList, processedCocktailsList)
        ).dispatchUpdatesTo(this)
    }

    private fun prepareVariables() {
        previousProcessedCocktailsList = processedCocktailsList
        headerWithCocktailsMap = null
    }

    private fun processCocktailsList(
        newCocktails: List<CocktailModel>?,
        sortType: CocktailSortType?
    ) {
        processedCocktailsList = when {
            (isFavoriteLayout == true)
                .or(sortType == null)
                .or(sortType == CocktailSortType.INGREDIENT_ASC)
                .or(sortType == CocktailSortType.INGREDIENT_DESC)
                .or(sortType == CocktailSortType.RECENT) -> {
                newCocktails
            }
            else -> {
                createHeadersMap(newCocktails, sortType)
                flattenHeadersMap()
            }
        } ?: emptyList()
    }

    private fun createHeadersMap(
        newCocktails: List<CocktailModel>?,
        sortType: CocktailSortType?
    ) {
        headerWithCocktailsMap = newCocktails
            ?.groupByTo(
                when (sortType) {
                    CocktailSortType.NAME_ASC -> {
                        TreeMap<String, MutableList<CocktailModel>>(compareBy { it })
                    }
                    CocktailSortType.NAME_DESC -> {
                        TreeMap<String, MutableList<CocktailModel>>(compareByDescending { it })
                    }
                    CocktailSortType.ALCOHOL_FIRST -> {
                        TreeMap<String, MutableList<CocktailModel>>(
                            CocktailSortComparator().alcoholComparator
                        )
                    }
                    CocktailSortType.NON_ALCOHOL_FIRST -> {
                        TreeMap<String, MutableList<CocktailModel>>(
                            CocktailSortComparator().reverseAlcoholComparator
                        )
                    }
                    else -> throw IllegalArgumentException("Unknown sort type specified")
                }
            ) { cocktail ->
                when (sortType) {
                    CocktailSortType.NAME_ASC, CocktailSortType.NAME_DESC -> {
                        cocktail.names.defaults?.get(0).toString()
                    }
                    CocktailSortType.ALCOHOL_FIRST, CocktailSortType.NON_ALCOHOL_FIRST -> {
                        cocktail.alcoholType.key
                    }
                    else -> throw IllegalArgumentException("Unknown sort type specified")
                }
            }
            .also { map ->
                map?.forEach { (_, cocktails) ->
                    cocktails.sortBy { it.names.defaults }
                }
            }
    }

    private fun flattenHeadersMap(): List<Any>? {
        return headerWithCocktailsMap?.flatMap { (key, values) ->
            listOf(key, *values.toTypedArray())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val inflatedView: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return CocktailViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        fun bindHeaderData() {
            val header = processedCocktailsList[position] as String
            val listener = HeaderItemUserActionListenerImpl()
            holder.bind(header, listener)
        }

        fun bindCocktailData() {
            val cocktail = processedCocktailsList[position] as CocktailModel
            val listener = CocktailItemUserActionListenerImpl(viewModel)
            holder.bind(cocktail, listener)
        }

        fun bindFavoriteCocktailData() {
            val cocktail = processedCocktailsList[position] as CocktailModel
            val listener = FavoriteCocktailUserActionListenerImpl(viewModel)
            holder.bind(cocktail, listener)
        }

        when {
            isFavoriteLayout == true -> bindFavoriteCocktailData()
            processedCocktailsList[position] is String -> bindHeaderData()
            processedCocktailsList[position] is CocktailModel -> bindCocktailData()
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
                processedCocktailsList[position],
                CocktailItemUserActionListenerImpl(viewModel)
            )
        }
    }

    override fun getItemCount(): Int = processedCocktailsList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            isFavoriteLayout == true -> R.layout.item_cocktail_favorite
            processedCocktailsList[position] is String -> R.layout.item_cocktail_header
            processedCocktailsList[position] is CocktailModel -> R.layout.item_cocktail
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