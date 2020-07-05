package com.test.thecocktaildb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.thecocktaildb.data.Cocktail
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface CocktailsDao {

    // TODO: 01.07.2020 add DAO to change addedData field and rewrite all logic (currently I jut
    //  replace cocktail object with new Date, but now it can break logic of cocktail of the day
    //  feature if cocktail of the day already saved to DB and marked as favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCocktail(cocktail: Cocktail): Completable

    @Query("SELECT * FROM cocktails WHERE idDrink = :cocktailId")
    fun getCocktail(cocktailId: String): Maybe<Cocktail>

    @Query("SELECT * FROM cocktails ORDER BY dateAdded DESC")
    fun getCocktails(): Maybe<List<Cocktail>>

    @Query("SELECT COUNT(idDrink) FROM cocktails")
    fun getNumberOfItems(): Observable<Long>

    @Query("UPDATE cocktails SET isFavorite = :state WHERE idDrink = :cocktailId")
    fun updateFavoriteState(cocktailId: String, state: Boolean): Completable
}