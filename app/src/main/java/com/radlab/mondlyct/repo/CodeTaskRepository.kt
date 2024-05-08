package com.radlab.mondlyct.repo

import com.radlab.mondlyct.models.Item
import com.radlab.mondlyct.models.MondlyResponse
import com.radlab.mondlyct.net.MondlyApiService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CodeTaskRepository {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://europe-west1-mondly-workflows.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val mondlyApi: MondlyApiService by lazy {
        retrofit.create(MondlyApiService::class.java)
    }

    suspend fun getCodeTaskList() = mapToItemList(mondlyApi.getCodeTask())

    private fun mapToItemList(response: Response<MondlyResponse>): Response<List<Item>> {
        return if (response.isSuccessful && response.body() != null) {
            val dataCollection = response.body()!!.dataCollection
            val itemList = dataCollection.map { it.item }
            Response.success(itemList)
        } else {
            Response.error(
                response.code(),
                response.errorBody() ?: "An error occurred".toResponseBody(null)
            )
        }
    }

}
