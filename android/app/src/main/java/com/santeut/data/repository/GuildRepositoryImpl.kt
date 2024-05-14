package com.santeut.data.repository

import android.util.Log
import com.google.gson.Gson
import com.santeut.data.apiservice.GuildApiService
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GuildRepositoryImpl @Inject constructor(
    private val guildApiService: GuildApiService
) : GuildRepository {
    override suspend fun getGuilds(): List<GuildResponse> {
        return try {
            val response = guildApiService.getGuilds()
            if (response.status == "200") {
                response.data.guildList
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun createGuild(
        guildProfile: MultipartBody.Part?,
        createGuildRequest: CreateGuildRequest
    ): Flow<Unit> = flow {
        Log.d("", "레포짇토리 ")
        val response =
            guildApiService.createGuild(guildProfile, createGuildPart(createGuildRequest))
        Log.d("response status", response.status)
        if (response.status == "200") {
            Log.d("Guild Repository", "동호회 생성 성공")
            emit(response.data)
        } else {
            throw Exception("동호회 생성 실패: ${response.status} ${response.data}")
        }
    }

    private fun createGuildPart(createGuildPostPart: CreateGuildRequest): MultipartBody.Part {
        val json = Gson().toJson(createGuildPostPart)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("request", null, requestBody)
    }


    override suspend fun myGuilds(): List<GuildResponse> {
        return try {
            val response = guildApiService.myGuilds()
            if (response.status == "200") {
                response.data.guildList ?: emptyList()
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getGuild(guildId: Int): GuildResponse {
        return try {
            val response = guildApiService.getGuild(guildId)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun applyGuild(guildId: Int): Flow<Unit> = flow {
        val response = guildApiService.applyGuild(guildId)
        if (response.status == "200") {
            Log.d("Guild Repository", "가입 요청 성공")
            emit(response.data)
        } else {
            throw Exception("가입 요청 실패: ${response.status} ${response.data}")
        }
    }

    override suspend fun getGuildPostList(guildId: Int, categoryId: Int): List<GuildPostResponse> {
        return try {
            val response = guildApiService.getGuildPostList(guildId, categoryId)
            if (response.status == "200") {
                Log.d("GuildRepository", "동호회 게시글 목록 조회 성공")
                response.data.postList ?: emptyList()
            } else {
                throw Exception("동호회 게시글 목록 조회 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun createGuildPost(
        images: List<MultipartBody.Part>?,
        createGuildPostRequest: CreateGuildPostRequest
    ): Flow<Unit> = flow {
        val response =
            guildApiService.createGuildPost(images, createGuildPostPart(createGuildPostRequest))
        if (response.status == "200") {
            Log.d("Guild Repository", "동호회 게시글 작성 성공")
            emit(response.data)
        } else {
            throw Exception("동호회 게시글 작성 실패: ${response.status} ${response.data}")
        }
    }

    private fun createGuildPostPart(createGuildPostPart: CreateGuildPostRequest): MultipartBody.Part {
        val json = Gson().toJson(createGuildPostPart)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("postCreateRequestDto", null, requestBody)
    }

    override suspend fun getGuildPost(guildPostId: Int): GuildPostDetailResponse {
        return try {
            val response = guildApiService.getGuildPost(guildPostId)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getGuildMemberList(guildId: Int): List<GuildMemberResponse> {
        return try {
            val response = guildApiService.getGuildMemberList(guildId)
            if (response.status == "200") {
                Log.d("GuildRepository", "동호회 회원 목록 조회 성공")
                response.data.memberList ?: emptyList()
            } else {
                throw Exception("동호회 회원 목록 조회 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun exileMember(guildId: Int, userId: Int): Flow<Unit> = flow {
        val response = guildApiService.exileMember(guildId, userId)
        if (response.status == "200") {
            Log.d("Guild Repository", "회원 추방 성공")
            emit(response.data)
        } else {
            throw Exception("회원 추방 실패: ${response.status} ${response.data}")
        }
    }

    override suspend fun changeLeader(guildId: Int, newLeaderId: Int): Flow<Unit> = flow {
        val response = guildApiService.changeLeader(guildId, newLeaderId)
        if (response.status == "200") {
            Log.d("Guild Repository", "회장 위임 성공")
            emit(response.data)
        } else {
            throw Exception("회장 위임 실패: ${response.status} ${response.data}")
        }
    }

    override suspend fun quitGuild(guildId: Int): Flow<Unit> = flow {
        val response = guildApiService.quitGuild(guildId)
        if (response.status == "200") {
            Log.d("Guild Repository", "동호회 탈퇴 성공")
            emit(response.data)
        } else {
            throw Exception("동호회 탈퇴 실패: ${response.status} ${response.data}")
        }
    }

    override suspend fun getRanking(type: Char): List<RankingResponse> {
        return try {
            val response = guildApiService.getRanking(type)
            if (response.status == "200") {
                Log.d("GuildRepository", "랭킹 조회 성공")
                response.data.partyMembers ?: emptyList()
            } else {
                throw Exception("랭킹 조회 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }
}