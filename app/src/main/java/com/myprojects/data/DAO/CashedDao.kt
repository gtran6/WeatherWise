package com.myprojects.data.DAO

import androidx.room.*
import com.myprojects.data.entity.CashedEntity

@Dao
interface CashedDao {

    @Update
    fun updateCashed(cashedEntity: CashedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCashed(cashedEntity: CashedEntity)

    @Delete
    fun deleteCashed(cashedEntity: CashedEntity)

    @Query("SELECT * FROM cashed_table")
    fun getAllCashed(): List<CashedEntity>

}