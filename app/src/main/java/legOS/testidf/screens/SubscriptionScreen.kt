package legOS.testidf.screens

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.viewmodel.SubscriptionViewModel
import legOS.testidf.R
import legOS.testidf.billing.BillingManager
import java.io.IOException

/**
 * ОБНОВЛЕНО: SubscriptionScreen с выбором между месячной и годовой подписками
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavController,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val uiState by viewModel.uiState.collectAsState()

    // Состояние выбранного плана
    var selectedPlan by remember { mutableStateOf("annual") } // "annual" или "monthly"

    // Фоновое изображение
    val backgroundImage = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("SubscriptionScreen", "Error loading background.png", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Фон
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Контент БЕЗ TopAppBar (убрана кнопка назад)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }
                uiState.isActive -> {
                    ActiveSubscriptionContent(
                        isTestMode = uiState.isTestMode,
                        subscriptionType = uiState.subscriptionType,
                        onContinue = {
                            navController.navigate("test_menu") {
                                popUpTo("subscription") { inclusive = true }
                            }
                        }
                    )
                }
                else -> {
                    InactiveSubscriptionContent(
                        selectedPlan = selectedPlan,
                        onPlanSelected = { selectedPlan = it },
                        errorMessage = uiState.errorMessage,
                        isPurchasing = uiState.isPurchasing,
                        onPurchase = {
                            activity?.let {
                                val productId = if (selectedPlan == "monthly") {
                                    BillingManager.SUBSCRIPTION_MONTHLY_PRODUCT_ID
                                } else {
                                    BillingManager.SUBSCRIPTION_ANNUAL_PRODUCT_ID
                                }
                                viewModel.purchaseSubscription(it, productId)
                            }
                        },
                        onRetry = { viewModel.checkSubscription() }
                    )
                }
            }

            // Диалог успешной покупки
            if (uiState.purchaseSuccess) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.clearPurchaseSuccess()
                    navController.navigate("test_menu") {
                        popUpTo("subscription") { inclusive = true }
                    }
                }

                AlertDialog(
                    onDismissRequest = {},
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(32.dp)
                            )
                            Text(stringResource(R.string.subscription_active))
                        }
                    },
                    text = {
                        Text(
                            stringResource(R.string.subscription_active_message),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    confirmButton = {}
                )
            }

            // Диалог ошибки
            uiState.errorMessage?.let { error ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text(stringResource(R.string.error_title)) },
                    text = { Text(error) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                )
            }
        }
    }
}
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                stringResource(R.string.checking_subscription),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ActiveSubscriptionContent(
    isTestMode: Boolean,
    subscriptionType: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = Color(0xFF4CAF50)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (isTestMode) {
                stringResource(R.string.test_mode_title)
            } else {
                stringResource(R.string.subscription_active)
            },
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(16.dp))

        if (isTestMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            stringResource(R.string.test_access),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.test_mode_message),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            // Показываем тип подписки
            val subscriptionText = when (subscriptionType) {
                "monthly" -> "Abonnement mensuel actif"
                "annual" -> "Abonnement annuel actif"
                else -> stringResource(R.string.subscription_active_message)
            }

            Text(
                subscriptionText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                stringResource(R.string.continue_button),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
private fun InactiveSubscriptionContent(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    errorMessage: String?,
    isPurchasing: Boolean,
    onPurchase: () -> Unit,
    onRetry: () -> Unit
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Альбомная ориентация (горизонтальная)
        InactiveSubscriptionLandscape(
            selectedPlan = selectedPlan,
            onPlanSelected = onPlanSelected,
            errorMessage = errorMessage,
            isPurchasing = isPurchasing,
            onPurchase = onPurchase,
            onRetry = onRetry
        )
    } else {
        // Портретная ориентация (вертикальная)
        InactiveSubscriptionPortrait(
            selectedPlan = selectedPlan,
            onPlanSelected = onPlanSelected,
            errorMessage = errorMessage,
            isPurchasing = isPurchasing,
            onPurchase = onPurchase,
            onRetry = onRetry
        )
    }
}

@Composable
private fun InactiveSubscriptionPortrait(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    errorMessage: String?,
    isPurchasing: Boolean,
    onPurchase: () -> Unit,
    onRetry: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            stringResource(R.string.subscription_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Преимущества подписки
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    stringResource(R.string.features_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                FeatureItem(stringResource(R.string.feature_categories))
                FeatureItem(stringResource(R.string.feature_advanced))
                FeatureItem(stringResource(R.string.feature_multiplayer))
                FeatureItem(stringResource(R.string.feature_creation))
                FeatureItem(stringResource(R.string.feature_catalog))
                FeatureItem(stringResource(R.string.feature_hall_of_fame))
                FeatureItem(stringResource(R.string.feature_ai_assistant))
                FeatureItem(stringResource(R.string.feature_updates))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Выбор плана подписки
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Годовая подписка (рекомендуется)
            SubscriptionPlanCard(
                title = stringResource(R.string.plan_yearly_title),
                price = stringResource(R.string.plan_yearly_price),
                pricePerMonth = stringResource(R.string.plan_yearly_per_month),
                savings = stringResource(R.string.plan_savings),
                isRecommended = true,
                isSelected = selectedPlan == "annual",
                onClick = { onPlanSelected("annual") }
            )

            // Месячная подписка
            SubscriptionPlanCard(
                title = stringResource(R.string.plan_monthly_title),
                price = stringResource(R.string.plan_monthly_price),
                pricePerMonth = "", // Пустая строка, чтобы не показывать повтор
                savings = null,
                isRecommended = false,
                isSelected = selectedPlan == "monthly",
                onClick = { onPlanSelected("monthly") }
            )
        }

        Spacer(Modifier.height(24.dp))

        // Кнопка покупки
        Button(
            onClick = onPurchase,
            enabled = !isPurchasing,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            if (isPurchasing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    stringResource(R.string.subscribe_button),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        if (errorMessage != null) {
            Spacer(Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    errorMessage,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_button))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Динамический текст о продлении из строковых ресурсов
        Text(
            text = stringResource(
                if (selectedPlan == "monthly") {
                    R.string.subscription_renew_monthly
                } else {
                    R.string.subscription_renew_annual
                }
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun InactiveSubscriptionLandscape(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    errorMessage: String?,
    isPurchasing: Boolean,
    onPurchase: () -> Unit,
    onRetry: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ЛЕВАЯ ЧАСТЬ: Преимущества (с прокруткой)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            val scrollState = rememberScrollState()

            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        stringResource(R.string.features_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )

                    FeatureItem(stringResource(R.string.feature_categories))
                    FeatureItem(stringResource(R.string.feature_advanced))
                    FeatureItem(stringResource(R.string.feature_multiplayer))
                    FeatureItem(stringResource(R.string.feature_creation))
                    FeatureItem(stringResource(R.string.feature_catalog))
                    FeatureItem(stringResource(R.string.feature_hall_of_fame))
                    FeatureItem(stringResource(R.string.feature_ai_assistant))
                    FeatureItem(stringResource(R.string.feature_updates))
                }
            }
        }

        // ПРАВАЯ ЧАСТЬ: Название, планы подписки, кнопка
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Название приложения
                Text(
                    stringResource(R.string.subscription_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                // Выбор плана подписки
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Годовая подписка (рекомендуется)
                    SubscriptionPlanCard(
                        title = stringResource(R.string.plan_yearly_title),
                        price = stringResource(R.string.plan_yearly_price),
                        pricePerMonth = stringResource(R.string.plan_yearly_per_month),
                        savings = stringResource(R.string.plan_savings),
                        isRecommended = true,
                        isSelected = selectedPlan == "annual",
                        onClick = { onPlanSelected("annual") }
                    )

                    // Месячная подписка
                    SubscriptionPlanCard(
                        title = stringResource(R.string.plan_monthly_title),
                        price = stringResource(R.string.plan_monthly_price),
                        pricePerMonth = "", // Пустая строка, чтобы не показывать повтор
                        savings = null,
                        isRecommended = false,
                        isSelected = selectedPlan == "monthly",
                        onClick = { onPlanSelected("monthly") }
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Кнопка покупки
                Button(
                    onClick = onPurchase,
                    enabled = !isPurchasing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    if (isPurchasing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            stringResource(R.string.subscribe_button),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Ошибка (если есть)
                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            errorMessage,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                    TextButton(onClick = onRetry) {
                        Text(stringResource(R.string.retry_button))
                    }
                }

                // Информация о продлении из строковых ресурсов
                Text(
                    text = stringResource(
                        if (selectedPlan == "monthly") {
                            R.string.subscription_renew_monthly
                        } else {
                            R.string.subscription_renew_annual
                        }
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SubscriptionPlanCard(
    title: String,
    price: String,
    pricePerMonth: String,
    savings: String?,
    isRecommended: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (isRecommended) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.plan_best_value),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = price,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // Показываем pricePerMonth только если это НЕ повторяется (для Monthly Plan)
            if (pricePerMonth != price && !pricePerMonth.contains("1,49$/month")) {
                Text(
                    text = pricePerMonth,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            if (savings != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "💰 $savings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}