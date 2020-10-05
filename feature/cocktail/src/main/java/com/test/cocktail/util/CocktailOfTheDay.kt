package com.test.cocktail.util

import com.test.thecocktaildb.data.local.CocktailIdHolder
import java.util.*
import kotlin.random.Random

class CocktailOfTheDay {
//    Logic explanation. I create list of mostly all cocktails id. Then i generate seed based on
//    current date. And get id based on random number using this seed.

    private val currentDate: Calendar = Calendar.getInstance()
    private val currentYear = currentDate.get(Calendar.YEAR)
    private val currentMonth = currentDate.get(Calendar.MONTH)
    private val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

    private val seed = currentYear + currentMonth + currentDay
    private val random = Random(seed)

    private val cocktailIdHolder = CocktailIdHolder()

    fun getCocktailId(): Int {
        val cocktailIndexOfTheDay = random.nextInt(0, cocktailIdHolder.dummyIdList.size)
        return cocktailIdHolder.dummyIdList[cocktailIndexOfTheDay]
    }
}