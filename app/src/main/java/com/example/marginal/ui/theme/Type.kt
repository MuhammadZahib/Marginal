package com.example.marginal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.marginal.R

val FrauncesFamily = FontFamily(Font(R.font.fraunces_semibold, FontWeight.SemiBold))
val PlexMonoFamily = FontFamily(Font(R.font.ibm_plex_mono_medium, FontWeight.Medium))

val MarginalTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FrauncesFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = PlexMonoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.6.sp,
    ),
)