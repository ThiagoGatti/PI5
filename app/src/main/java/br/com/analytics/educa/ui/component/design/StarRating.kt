package br.com.analytics.educa.ui.component.design

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    selectedStars: Int,
    onStarsSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for (i in 1..10) {
            IconButton(
                onClick = { onStarsSelected(i) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (i <= selectedStars) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= selectedStars) Color(0xFFFFD700) else Color(0xFFB0BEC5),
                )
            }
        }
    }
}
