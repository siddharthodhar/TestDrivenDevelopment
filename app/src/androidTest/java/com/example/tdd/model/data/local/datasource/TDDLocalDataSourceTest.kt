package com.example.tdd.model.data.local.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.tdd.model.TDDDatabase
import com.example.tdd.model.data.local.dao.TDDDao
import com.example.tdd.model.data.local.entity.DataEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class TDDLocalDataSourceTest {
    private lateinit var db: TDDDatabase
    private lateinit var dao: TDDDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TDDDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = db.tddDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun get_data_emits_after_insert() = runTest {
        val datasource = TDDLocalDataSource(dao)

        datasource.insertData(
            listOf(
                DataEntity(
                    1,
                    1,
                    "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
                )
            )
        )

        val result = datasource.getData().first()

        assertEquals(1, result.size)
        assertEquals(1, result.first().id)
    }

    @Test
    fun get_data_by_id_emits_after_insert() = runTest {
        val datasource = TDDLocalDataSource(dao)

        datasource.insertData(
            listOf(
                DataEntity(
                    2,
                    2,
                    "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
                )
            )
        )

        val result = datasource.getDataById(2).first()

        assertEquals(2, result?.id)
    }

    @Test
    fun insert_replaces_data_on_conflict() = runTest {
        val datasource = TDDLocalDataSource(dao)

        datasource.insertData(
            listOf(
                DataEntity(
                    2,
                    2,
                    "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
                )
            )
        )

        datasource.insertData(
            listOf(
                DataEntity(
                    3,
                    2,
                    "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
                )
            )
        )

        val result = datasource.getDataById(2).first()

        assertEquals(2, result?.id)
        assertEquals(3, result?.userId)
    }
}