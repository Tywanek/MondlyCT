package com.radlab.mondlyct.repo

import com.radlab.mondlyct.models.Item
import com.radlab.mondlyct.net.MondlyApiService
import com.radlab.mondlyct.room.AppDatabase
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CodeTaskRepository(private val database: AppDatabase) {

    private val mondlyApiService: MondlyApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://europe-west1-mondly-workflows.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MondlyApiService::class.java)
    }

    suspend fun getCodeTaskList(): Response<List<Item>> {
        return database.codeTaskDao().getAllItems()
            .takeIf { it.isNotEmpty() }
            ?.let { Response.success(it) }
            ?: fetchAndStoreCodeTasks()
    }

    private suspend fun fetchAndStoreCodeTasks(): Response<List<Item>> {
        return try {
            val response = mondlyApiService.getCodeTask()
            return if (response.isSuccessful) {
                response.body()?.dataCollection
                    ?.map { it.item }
                    ?.also { database.codeTaskDao().insertAllItems(it) }
                    .let { Response.success(it) }
            } else {
                Response.error(
                    response.code(),
                    response.errorBody() ?: "An error occurred".toResponseBody(null)
                )
            }
        } catch (e: Exception) {
            Response.error(500, "An error occurred".toResponseBody(null))
        }
    }
}
