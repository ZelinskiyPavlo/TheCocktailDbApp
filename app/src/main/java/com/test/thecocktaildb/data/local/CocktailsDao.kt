package com.test.thecocktaildb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.thecocktaildb.data.Cocktail
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface CocktailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCocktail(cocktail: Cocktail): Completable

    @Query("SELECT * FROM cocktails WHERE idDrink = :cocktailId")
    fun getCocktail(cocktailId: String): Maybe<Cocktail>
}