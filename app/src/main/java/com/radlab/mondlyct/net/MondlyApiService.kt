package com.radlab.mondlyct.net

import com.radlab.mondlyct.models.MondlyResponse
import retrofit2.Response
import retrofit2.http.GET

interface MondlyApiService {
    @GET("mondly_android_code_task_json")
    suspend fun getCodeTask(): Response<MondlyResponse>
}
