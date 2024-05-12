package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.CreateGuildPostScreen
import com.santeut.ui.guild.GuildCommunityScreen
import com.santeut.ui.guild.GuildMemberListScreen
import com.santeut.ui.guild.GuildScreen
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.guild.MyGuildScreen
import com.santeut.ui.party.CreatePartyScreen


fun NavGraphBuilder.GuildNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "guild",
        route = "guild_graph"
    ) {

        // 나의 모임 페이지
        composable("guild") {
            MyGuildScreen(navController)
        }

        // 동호회 상세 페이지
        composable(
            route = "getGuild/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildScreen(guildId, navController)
        }

        // 동호회 게시판
        composable(
            route = "guildCommunity/{guildId}",
            arguments = listOf(navArgument("guildId") { type = NavType.IntType })
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildCommunityScreen(guildId, navController)
        }

        // 동호회 게시판 글쓰기
        composable(
            route = "createGuildPost/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            CreateGuildPostScreen(guildId, navController)
        }

        // 동호회 회원 조회
        composable(
            route = "guildMemberList/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildMemberListScreen(guildId, navController)
        }

        composable("createParty") {
            CreatePartyScreen(navController)
        }
    }
}