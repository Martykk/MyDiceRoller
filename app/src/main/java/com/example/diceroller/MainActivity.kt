/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background

                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp)
        .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf( 1) }
    var rollCount by remember { mutableStateOf(0) }

    // 建立一個狀態來追蹤是否展開
    var isExpanded by remember { mutableStateOf(false) }
    var rollHistory = remember { mutableStateListOf<Int>() }

    val imageResource = when(result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Total Roll: ${rollCount}",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 取代原本的 if (result == 6)
        Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
            if (result == 6) {
                Text(
                    text = "Lucky Six! ",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Surface(
            //大圓角
            shape = MaterialTheme.shapes.large,

            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        ){
            Image(modifier =
                Modifier.border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                painter = painterResource(imageResource),
                contentDescription = result.toString())
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                result = (1..6).random()
                rollHistory.add(0,result)
                rollCount++
                      },
        ) {
            Text(text = stringResource(R.string.roll), fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isExpanded) "▼ 隱藏歷史紀錄" else "▶ 顯示歷史紀錄",
            // 關鍵邏輯：如果展開就顯示無限多行，否則只准顯示 1 行
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            // 加上省略符號，讓使用者知道後面還有字
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded } // 點擊切換
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (isExpanded) {
            Text(text = "歷史紀錄", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier =Modifier
                .animateContentSize()
                .height(if (isExpanded) 200.dp else 0.dp)
            ) {
                items(rollHistory) { score -> // 這裡的 score 就是 Lambda 的參數
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 1.dp,
                        modifier = Modifier.animateContentSize().padding(1.dp),
                        color = animateColorAsState(
                            if (isExpanded) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        ).value
                    ) {
                        Text(
                            text = "第 ${rollHistory.size - rollHistory.indexOf(score)} 次擲出：$score 點",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                }
            }
        }
    }
}
