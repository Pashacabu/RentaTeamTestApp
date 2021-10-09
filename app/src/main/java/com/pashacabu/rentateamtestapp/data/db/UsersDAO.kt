package com.pashacabu.rentateamtestapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET

@Dao
interface UsersDAO {

    @Insert(entity = Users::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<Users>)

    @Query("SELECT * FROM Users")
    suspend fun getUsers(): List<Users>

    @Insert(entity = Users::class, onConflict = OnConflictStrategy.REPLACE)
    fun rxInsertUsers(users: List<Users>?): Completable

    @Query("SELECT * FROM Users")
    fun rxGetUsers(): Maybe<List<Users>>

    @Query("DELETE FROM Users")
    fun rxDeleteAll(): Completable
}