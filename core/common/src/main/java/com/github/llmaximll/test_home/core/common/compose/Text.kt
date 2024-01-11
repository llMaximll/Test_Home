package com.github.llmaximll.test_home.core.common.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.github.llmaximll.test_home.core.common.R
import com.github.llmaximll.test_home.core.common.theme.AppColors

@Composable
fun CommonText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = AppColors.OnBackground
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontFamily = FontFamily(Font(R.font.circe)),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}