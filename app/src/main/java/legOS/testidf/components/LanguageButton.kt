package legOS.testidf.components

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import legOS.testidf.LocaleManager

/**
 * Кнопка смены языка для использования на всех экранах
 */
@Composable
fun LanguageButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val currentLanguage = remember { mutableStateOf(LocaleManager.getCurrentLanguage(context)) }

    FloatingActionButton(
        onClick = {
            LocaleManager.toggleLanguage(context)
            currentLanguage.value = LocaleManager.getCurrentLanguage(context)
            activity?.recreate() // Перезапускаем активность для применения языка
        },
        modifier = modifier.size(56.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Language,
                contentDescription = "Change Language",
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = currentLanguage.value.displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}