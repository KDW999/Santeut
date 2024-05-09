package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.PartyListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate

interface PartyApiService {

    @GET("/api/party")
    suspend fun getPartyList(
        @Query("guild") guild: Int,
        @Query("name") name: String,
        @Query("start") start: LocalDate,
        @Query("end") end: LocalDate
    ): CustomResponse<PartyListResponse>

    @POST("/api/party/")
    suspend fun createParty(
        @Body createPartyRequest: CreatePartyRequest
    ): CustomResponse<Unit>
}