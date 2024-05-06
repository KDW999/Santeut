package com.santeut.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.santeut.data.model.response.PostResponse

@Composable
fun ReadPostScreen(
    postId: Int,
    postType: Char,
    postViewModel: PostViewModel
) {
    val focusManager = LocalFocusManager.current
    var comment by remember { mutableStateOf("") }

    val post by postViewModel.post.observeAsState()

    LaunchedEffect(key1 = postId) {
        postViewModel.readPost(postId, postType)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            HeaderSection(post)
            ContentSection(post)
            CommentSection(
                comment,
                onCommentChange = { comment = it },
                onSend = { focusManager.clearFocus() })
        }
    }
}

@Composable
fun HeaderSection(post: PostResponse?) {
    post?.let {
        val category = when (post.postType) {
            'T' -> "등산Tip"   // 'T'일 때 "등산Tip"
            'C' -> "등산 코스" // 'C'일 때 "등산 코스"
            else -> "기타"     // 그 외의 경우 "기타"
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = category, style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = post.postTitle, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = post.userNickname, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = formatTime(post.createdAt), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ContentSection(post: PostResponse?) {
    post?.let {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(text = post.postContent, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CommentSection(
    comment: String,
    onCommentChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 56.dp)
        ) {
            items(30) { PostCommentScreen() }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            TextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = { Text("내용") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSend() })
            )
            IconButton(onClick = onSend) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}