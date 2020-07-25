package com.test.thecocktaildb.dataNew.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.thecocktaildb.dataNew.db.Table
import com.test.thecocktaildb.dataNew.db.model.*

@Dao
interface CocktailDao {

    @get:Query("SELECT * FROM CocktailDbModel")
    val cocktailListLiveData: LiveData<List<CocktailDbModel>>

    @Query("SELECT * FROM CocktailDbModel LIMIT 1")
    fun getFirstCocktail(): CocktailDbModel?

    @Query("SELECT * FROM CocktailDbModel WHERE id = :id")
    fun getCocktailById(id: Long): CocktailDbModel?

    @Query("SELECT * FROM CocktailDbModel")
    fun getCocktails(): List<CocktailDbModel>?

    fun addOrReplaceCocktails(vararg cocktail: CocktailDbModel) {
        cocktail.forEach { addOrReplaceCocktail(it) }
    }

    @Transaction
    fun addOrReplaceCocktail(cocktail: CocktailDbModel) {
        with(cocktail) {
            insertCocktailEntity(
                CocktailEntity(
                    id,
                    category ?: "",
                    alcoholType ?: "",
                    glass ?: "",
                    image ?: "",
                    isFavorite ?: false,
                    Calendar.getInstance().time
                )
            )

            insertName(
                NameEntity(
                    id,
                    names?.defaults,
                    names?.defaultAlternate,
                    names?.es,
                    names?.de,
                    names?.fr,
                    names?.zhHans,
                    names?.zhHant
                )
            )

            insertInstruction(
                InstructionEntity(
                    id,
                    instructions?.defaults,
                    instructions?.defaultAlternate,
                    instructions?.es,
                    instructions?.de,
                    instructions?.fr,
                    instructions?.zhHans,
                    instructions?.zhHant
                )
            )

            measures.forEach { measure ->
                insertMeasure(MeasureEntity(measure))
                insertMeasureJunction(CocktailMeasureJunction(cocktail.id, measure))
            }

            ingredients.forEach { ingredient ->
                insertIngredient(IngredientEntity(ingredient))
                insertIngredientJunction(CocktailIngredientJunction(cocktail.id, ingredient))
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktailEntity(cocktailEntity: CocktailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertName(nameEntity: NameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInstruction(instructionEntity: InstructionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasure(measureEntity: MeasureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasureJunction(cocktailMeasureJunction: CocktailMeasureJunction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredientEntity: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredientJunction(ingredientJunction: CocktailIngredientJunction)

    @Transaction
    fun replaceAllCocktails(vararg cocktail: CocktailDbModel) {
        deleteAllCocktails()
        addOrReplaceCocktails(*cocktail)
    }

    //region Delete cocktail section

    //region Delete specified cocktails section
    @Transaction
    fun deleteCocktails(vararg cocktail: CocktailDbModel) {
        cocktail.forEach {
            deleteCocktailTable(it.id)
            deleteNameTable(it.id)
            deleteInstructionTable(it.id)
        }
    }

    @Query("DELETE FROM ${Table.COCKTAIL} WHERE id = :id")
    fun deleteCocktailTable(id: Long)

    @Query("DELETE FROM ${Table.NAME} WHERE cocktail_id = :id")
    fun deleteNameTable(id: Long)

    @Query("DELETE FROM ${Table.INSTRUCTION} WHERE cocktail_id = :id")
    fun deleteInstructionTable(id: Long)
    //endregion

    //region Delete all cocktails section
    @Transaction
    fun deleteAllCocktails() {
        deleteAllCocktailTable()
        deleteAllNameTable()
        deleteAllInstructionTable()
        deleteAllIngredientTable()
        deleteAllMeasureTable()
    }

    @Query("DELETE FROM ${Table.COCKTAIL}")
    fun deleteAllCocktailTable()

    @Query("DELETE FROM ${Table.NAME}")
    fun deleteAllNameTable()

    @Query("DELETE FROM ${Table.INSTRUCTION}")
    fun deleteAllInstructionTable()

    @Query("DELETE FROM ${Table.INGREDIENT}")
    fun deleteAllIngredientTable()

    @Query("DELETE FROM ${Table.MEASURE}")
    fun deleteAllMeasureTable()
    //endregion

    //endregion
}