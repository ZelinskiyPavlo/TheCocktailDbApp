package com.test.databaseimpl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.test.database.Table
import com.test.database.model.cocktail.CocktailDbModel
import com.test.database.model.cocktail.CocktailEntity
import com.test.database.model.cocktail.CocktailIngredientJunction
import com.test.database.model.cocktail.CocktailMeasureJunction
import com.test.database.model.cocktail.IngredientEntity
import com.test.database.model.cocktail.InstructionEntity
import com.test.database.model.cocktail.MeasureEntity
import com.test.database.model.cocktail.NameEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

@Dao
interface CocktailDao {

    @get:Query("SELECT * FROM CocktailDbModel ORDER BY dateAdded DESC")
    val cocktailListFlow: Flow<List<CocktailDbModel>>

    @Query("SELECT * FROM CocktailDbModel LIMIT 1")
    fun getFirstCocktail(): CocktailDbModel?

    @Query("SELECT * FROM CocktailDbModel WHERE id = :id")
    fun getCocktailById(id: Long): CocktailDbModel?

    @Query("SELECT * FROM CocktailDbModel ORDER BY dateAdded DESC")
    fun getCocktails(): List<CocktailDbModel>?

    @Query("UPDATE ${Table.COCKTAIL} SET date_added = :dateAdded WHERE id = :cocktailId")
    fun updateCocktailDateAdded(dateAdded: Date, cocktailId: Long)

    @Query("UPDATE ${Table.COCKTAIL} SET is_favorite = :isFavorite WHERE id = :cocktailId")
    fun updateCocktailFavoriteState(cocktailId: Long, isFavorite: Boolean)

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

    fun addOrReplaceCocktails(vararg cocktail: CocktailDbModel) {
        cocktail.forEach { addOrReplaceCocktail(it) }
    }

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
            // Note: order of deletion matter
            // Also try to use ON DELETE CASCADE if this supported
            deleteFromInstructionTable(it.id)
            deleteFromNameTable(it.id)
            deleteFromIngredientJunctionTable(it.id)
            deleteFromMeasureJunctionTable(it.id)
            deleteFromCocktailTable(it.id)
        }
    }

    @Query("DELETE FROM ${Table.INSTRUCTION} WHERE cocktail_id = :id")
    fun deleteFromInstructionTable(id: Long)

    @Query("DELETE FROM ${Table.NAME} WHERE cocktail_id = :id")
    fun deleteFromNameTable(id: Long)

    @Query("DELETE FROM ${Table.COCKTAIL_INGREDIENT} WHERE cocktail_id = :id")
    fun deleteFromIngredientJunctionTable(id: Long)

    @Query("DELETE FROM ${Table.COCKTAIL_MEASURE} WHERE cocktail_id = :id")
    fun deleteFromMeasureJunctionTable(id: Long)

    @Query("DELETE FROM ${Table.COCKTAIL} WHERE id = :id")
    fun deleteFromCocktailTable(id: Long)
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