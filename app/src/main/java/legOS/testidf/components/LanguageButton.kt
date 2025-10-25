package legOS.testidf.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import legOS.testidf.LocaleManager

/**
 * Кнопка смены языка с выпадающим меню для глобального изменения языка приложения
 */
@Composable
fun LanguageButton(
    currentLanguage: LocaleManager.Language,
    onLanguageChange: (LocaleManager.Language) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        // Главная кнопка (активный язык)
        FloatingActionButton(
            onClick = {
                isExpanded = !isExpanded
            },
            modifier = Modifier.size(56.dp),
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
                    text = currentLanguage.displayName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Выпадающее меню с другими языками
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LocaleManager.Language.values().forEach { language ->
                    // Показываем только неактивные языки
                    if (language != currentLanguage) {
                        FloatingActionButton(
                            onClick = {
                                onLanguageChange(language)
                                isExpanded = false
                            },
                            modifier = Modifier.size(56.dp),
                            // Инвертированные цвета для неактивных языков
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    contentDescription = "Select ${language.displayName}",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = language.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}