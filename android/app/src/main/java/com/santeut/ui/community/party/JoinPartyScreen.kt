package com.santeut.ui.community.party

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.PartyResponse
import com.santeut.designsystem.theme.DarkGreen
import com.santeut.designsystem.theme.Green
import com.santeut.ui.party.PartyViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinPartyScreen(
    guildId: Int?,
    partyViewModel: PartyViewModel = hiltViewModel(),
) {

    val partyList by partyViewModel.partyList.observeAsState(emptyList())

    var searchFilterStartDate by remember { mutableStateOf("") }
    var searchFilterEndDate by remember { mutableStateOf("") }

    var showBottomFilterSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    // 달력
    val startCalendar = Calendar.getInstance()
    val startDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            startCalendar.set(year, month, day)
            val selectedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startCalendar.time)
            startCalendar.set(year, month, day)
            searchFilterStartDate = selectedDate
        },
        startCalendar.get(Calendar.YEAR),
        startCalendar.get(Calendar.MONTH),
        startCalendar.get(Calendar.DAY_OF_MONTH)
    )
    val endCalendar = Calendar.getInstance()
    val endDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            endCalendar.set(year, month, day)

            if (endCalendar.time.before(startCalendar.time)) {
                Toast.makeText(context, "종료 날짜는 시작 날짜보다 이후여야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endCalendar.time)
                endCalendar.set(year, month, day)
                searchFilterEndDate = selectedDate
            }
        },
        endCalendar.get(Calendar.YEAR),
        endCalendar.get(Calendar.MONTH),
        endCalendar.get(Calendar.DAY_OF_MONTH)
    )

    // 검색어
    var searchWord by remember {mutableStateOf("")}

    LaunchedEffect(key1 = null) {
        partyViewModel.getPartyList(guildId = null, name = null, start = null, end = null)
    }

    Scaffold(
        topBar = {
            PartySearchBar(
                partyViewModel,
                searchWord,
                onSearchTextChanged = { searchWord = it },
                onClickFilter = {
                    showBottomFilterSheet = true
                }
            )
        }, content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (partyList.isEmpty()) {
                    Text(
                        text = "소모임이 없습니다",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        items(partyList) { party ->
                            PartyCard(party, partyViewModel)
                        }
                    }
                }
            }

            val context = LocalContext.current
            if (showBottomFilterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomFilterSheet = false },
                    sheetState = filterSheetState
                ) {
                    Column (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("조회 시작 날짜")
                        TextField(
                            value = searchFilterStartDate,
                            onValueChange = {},
                            label = { },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { startDatePickerDialog.show() }) {
                                    androidx.compose.material.Icon(Icons.Filled.DateRange, contentDescription = "조회 시작 날짜")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("조회 마지막 날짜")
                        Text(
                            text = "조회 마지막 날짜",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        TextField(
                            value = searchFilterEndDate,
                            onValueChange = {},
                            label = { },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { endDatePickerDialog.show() }) {
                                    androidx.compose.material.Icon(Icons.Filled.DateRange, contentDescription = "조회 마지막 날짜")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 적용 버튼
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 16.dp, 0.dp, 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green,
                                contentColor = Color.White
                            ),
                            onClick = {
                                if (searchFilterStartDate == "") {
                                    Toast.makeText(context, "시작 날짜를 선택해주세요", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                if (searchFilterEndDate == "") {
                                    Toast.makeText(context, "종료 날짜를 선택해주세요", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                // TODO: 소모임 필터 검색
                                Log.d("조회 시작", searchFilterStartDate)
                                Log.d("조회 종료", searchFilterEndDate)
                            }
                        ) {
                            Text(
                                text = "적용하기",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyCard(party: PartyResponse, partyViewModel: PartyViewModel) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Card (
        modifier = Modifier
            .clickable(onClick = { showBottomSheet = true })
            .fillMaxWidth()
            .height(160.dp)  // 카드 높이 조정
            .padding(8.dp), // 카드 주변 여백
            colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column (
          modifier = Modifier
              .padding(16.dp)
        ) {
            Text(
                text = party.partyName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
            // 일정
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "일정",
                    tint = Green,
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = party.schedule)
            }
            // 모임장소
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "위치",
                    tint = Green,
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = party.place)
            }
            // 산, 인원수
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(0.dp, 4.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardDoubleArrowUp,
                        contentDescription = "산",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = party.mountainName)
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "인원 수",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = "${party.curPeople} / ${party.maxPeople} 명")
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Surface(modifier = Modifier.padding(16.dp)) {
                Column {
                    // 동호회 상세 정보
                    Button(
                        onClick = { partyViewModel.joinParty(party.partyId) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !(party.isMember),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Black,
                        )
                    ) {
                        if(party.isMember) {
                            Text(
                                "이미 가입한 소모임입니다",
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "참가하기",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PartySearchBar(
    partyViewModel: PartyViewModel,
    enteredText: String,
    onSearchTextChanged: (String) -> Unit,
    onClickFilter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp, bottom = 8.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xff666E7A)),
            value = enteredText,
            onValueChange = { text ->
                onSearchTextChanged(text)
            },
            placeholder = {
                androidx.compose.material.Text(
                    text = "어느 소모임을 찾으시나요?",
                    color = Color(0xff666E7A)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done // 완료 액션 지정
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFD6D8DB),
                unfocusedContainerColor = Color(0xFFEFEFF0),
                focusedBorderColor = Color(0xFFD6D8DB),
                focusedContainerColor = Color(0xFFEFEFF0),
            ),
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color(0xff33363F),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            Log.d("소모임 검색 버튼 클릭", enteredText)
                            partyViewModel.getPartyList(null, enteredText, null, null)
                        }
                )
            }
        )
        Spacer(modifier = Modifier.width(15.dp))
        IconButton(
            onClick = {
                onClickFilter()
            }
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "필터",
                tint = Color(0xff335C49),
                modifier = Modifier.size(30.dp),
            )
        }
    }
}

