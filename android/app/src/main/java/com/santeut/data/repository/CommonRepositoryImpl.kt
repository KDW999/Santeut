package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.CommonApiService
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.NotificationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonApiService: CommonApiService
) : CommonRepository {

    override suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit> = flow {
        val response = commonApiService.createComment(postId, postType.toString(), commentRequest)
        if (response.status == "201") {
            emit(response.data)
        }
    }

    override suspend fun getNotificationList(): List<NotificationResponse> {
        return try {
            val response = commonApiService.getNotificationList()
            if (response.status == "200") {
                Log.d("CommonRepository", "알림 목록 조회 성공")
                response.data.notiList ?: emptyList()
            } else {
                throw Exception("알림 목록 조회 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("CommonRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

}