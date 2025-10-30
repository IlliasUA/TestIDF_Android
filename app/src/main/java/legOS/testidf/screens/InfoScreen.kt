package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import legOS.testidf.R
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun InfoScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Load background image with error handling
    val backgroundImage = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("InfoScreen", "Error loading background.png", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Display background image
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Контент с безопасными отступами поверх фона
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            when {
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact && !isLandscape -> {
                    InfoCompactLayout(navController)
                }
                isLandscape -> {
                    InfoLandscapeLayout(navController)
                }
                else -> {
                    InfoLargeLayout(navController)
                }
            }
        }
    }
}

@Composable
private fun InfoCompactLayout(navController: NavController) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовки
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.info_app_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.info_general_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.large)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LegalSectionsContent()
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(
                text = stringResource(R.string.back_button),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun InfoLandscapeLayout(navController: NavController) {
    val listState = rememberLazyListState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Левая часть - 60% экрана для контента со скроллингом
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .padding(end = 16.dp)
                .clip(MaterialTheme.shapes.large)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LegalSectionsContent()
                }
            }
        }

        // Правая часть - 40% экрана для заголовка и кнопки
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Заголовок вверху справа
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.info_app_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.info_general_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Кнопка возврата внизу справа
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    text = stringResource(R.string.back_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun InfoLargeLayout(navController: NavController) {
    val listState = rememberLazyListState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовки
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.info_app_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.info_general_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        LegalSectionsContent()
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    text = stringResource(R.string.back_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun LegalSectionsContent() {
    // Charger toutes les ressources string directement ici
    val privacyTitle = stringResource(R.string.info_privacy_title)
    val privacyIntroSubtitle = stringResource(R.string.info_privacy_intro_subtitle)
    val privacyIntroContent = stringResource(R.string.info_privacy_intro_content)
    val privacyDataSubtitle = stringResource(R.string.info_privacy_data_subtitle)
    val privacyDataContent = stringResource(R.string.info_privacy_data_content)
    val privacyUsageSubtitle = stringResource(R.string.info_privacy_usage_subtitle)
    val privacyUsageContent = stringResource(R.string.info_privacy_usage_content)
    val privacySecuritySubtitle = stringResource(R.string.info_privacy_security_subtitle)
    val privacySecurityContent = stringResource(R.string.info_privacy_security_content)
    val privacyChildrenSubtitle = stringResource(R.string.info_privacy_children_subtitle)
    val privacyChildrenContent = stringResource(R.string.info_privacy_children_content)
    val privacyChangesSubtitle = stringResource(R.string.info_privacy_changes_subtitle)
    val privacyChangesContent = stringResource(R.string.info_privacy_changes_content)
    val privacyContactSubtitle = stringResource(R.string.info_privacy_contact_subtitle)
    val privacyContactContent = stringResource(R.string.info_privacy_contact_content)

    val termsTitle = stringResource(R.string.info_terms_title)
    val termsAcceptanceSubtitle = stringResource(R.string.info_terms_acceptance_subtitle)
    val termsAcceptanceContent = stringResource(R.string.info_terms_acceptance_content)
    val termsDescriptionSubtitle = stringResource(R.string.info_terms_description_subtitle)
    val termsDescriptionContent = stringResource(R.string.info_terms_description_content)
    val termsUsageSubtitle = stringResource(R.string.info_terms_usage_subtitle)
    val termsUsageContent = stringResource(R.string.info_terms_usage_content)
    val termsIntellectualSubtitle = stringResource(R.string.info_terms_intellectual_subtitle)
    val termsIntellectualContent = stringResource(R.string.info_terms_intellectual_content)
    val termsPaymentSubtitle = stringResource(R.string.info_terms_payment_subtitle)
    val termsPaymentContent = stringResource(R.string.info_terms_payment_content)
    val termsLiabilitySubtitle = stringResource(R.string.info_terms_liability_subtitle)
    val termsLiabilityContent = stringResource(R.string.info_terms_liability_content)
    val termsModificationsSubtitle = stringResource(R.string.info_terms_modifications_subtitle)
    val termsModificationsContent = stringResource(R.string.info_terms_modifications_content)
    val termsContactSubtitle = stringResource(R.string.info_terms_contact_subtitle)
    val termsContactContent = stringResource(R.string.info_terms_contact_content)

    val licenseTitle = stringResource(R.string.info_license_title)
    val licenseUsageSubtitle = stringResource(R.string.info_license_usage_subtitle)
    val licenseUsageContent = stringResource(R.string.info_license_usage_content)
    val licenseScopeSubtitle = stringResource(R.string.info_license_scope_subtitle)
    val licenseScopeContent = stringResource(R.string.info_license_scope_content)
    val licenseIntellectualSubtitle = stringResource(R.string.info_license_intellectual_subtitle)
    val licenseIntellectualContent = stringResource(R.string.info_license_intellectual_content)
    val licenseRestrictionsSubtitle = stringResource(R.string.info_license_restrictions_subtitle)
    val licenseRestrictionsContent = stringResource(R.string.info_license_restrictions_content)
    val licenseTerminationSubtitle = stringResource(R.string.info_license_termination_subtitle)
    val licenseTerminationContent = stringResource(R.string.info_license_termination_content)

    val contactTitle = stringResource(R.string.info_contact_title)
    val contactContent = stringResource(R.string.info_contact_content)

    val disclaimerTitle = stringResource(R.string.info_disclaimer_title)
    val disclaimerContentSubtitle = stringResource(R.string.info_disclaimer_content_subtitle)
    val disclaimerContentContent = stringResource(R.string.info_disclaimer_content_content)
    val disclaimerSourcesSubtitle = stringResource(R.string.info_disclaimer_sources_subtitle)
    val disclaimerSourcesContent = stringResource(R.string.info_disclaimer_sources_content)
    val disclaimerLiabilitySubtitle = stringResource(R.string.info_disclaimer_liability_subtitle)
    val disclaimerLiabilityContent = stringResource(R.string.info_disclaimer_liability_content)
    val disclaimerRiskSubtitle = stringResource(R.string.info_disclaimer_risk_subtitle)
    val disclaimerRiskContent = stringResource(R.string.info_disclaimer_risk_content)
    val disclaimerContactSubtitle = stringResource(R.string.info_disclaimer_contact_subtitle)
    val disclaimerContactContent = stringResource(R.string.info_disclaimer_contact_content)

    // Construire la liste avec remember pour optimiser
    val legalSections = remember(
        privacyTitle, termsTitle, licenseTitle, contactTitle, disclaimerTitle
    ) {
        listOf(
            LegalSection(
                title = privacyTitle,
                subsections = listOf(
                    Subsection(subtitle = privacyIntroSubtitle, content = privacyIntroContent),
                    Subsection(subtitle = privacyDataSubtitle, content = privacyDataContent),
                    Subsection(subtitle = privacyUsageSubtitle, content = privacyUsageContent),
                    Subsection(subtitle = privacySecuritySubtitle, content = privacySecurityContent),
                    Subsection(subtitle = privacyChildrenSubtitle, content = privacyChildrenContent),
                    Subsection(subtitle = privacyChangesSubtitle, content = privacyChangesContent),
                    Subsection(subtitle = privacyContactSubtitle, content = privacyContactContent)
                )
            ),
            LegalSection(
                title = termsTitle,
                subsections = listOf(
                    Subsection(subtitle = termsAcceptanceSubtitle, content = termsAcceptanceContent),
                    Subsection(subtitle = termsDescriptionSubtitle, content = termsDescriptionContent),
                    Subsection(subtitle = termsUsageSubtitle, content = termsUsageContent),
                    Subsection(subtitle = termsIntellectualSubtitle, content = termsIntellectualContent),
                    Subsection(subtitle = termsPaymentSubtitle, content = termsPaymentContent),
                    Subsection(subtitle = termsLiabilitySubtitle, content = termsLiabilityContent),
                    Subsection(subtitle = termsModificationsSubtitle, content = termsModificationsContent),
                    Subsection(subtitle = termsContactSubtitle, content = termsContactContent)
                )
            ),
            LegalSection(
                title = licenseTitle,
                subsections = listOf(
                    Subsection(subtitle = licenseUsageSubtitle, content = licenseUsageContent),
                    Subsection(subtitle = licenseScopeSubtitle, content = licenseScopeContent),
                    Subsection(subtitle = licenseIntellectualSubtitle, content = licenseIntellectualContent),
                    Subsection(subtitle = licenseRestrictionsSubtitle, content = licenseRestrictionsContent),
                    Subsection(subtitle = licenseTerminationSubtitle, content = licenseTerminationContent)
                )
            ),
            LegalSection(
                title = contactTitle,
                subsections = listOf(
                    Subsection(subtitle = "", content = contactContent)
                )
            ),
            LegalSection(
                title = disclaimerTitle,
                subsections = listOf(
                    Subsection(subtitle = disclaimerContentSubtitle, content = disclaimerContentContent),
                    Subsection(subtitle = disclaimerSourcesSubtitle, content = disclaimerSourcesContent),
                    Subsection(subtitle = disclaimerLiabilitySubtitle, content = disclaimerLiabilityContent),
                    Subsection(subtitle = disclaimerRiskSubtitle, content = disclaimerRiskContent),
                    Subsection(subtitle = disclaimerContactSubtitle, content = disclaimerContactContent)
                )
            )
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        legalSections.forEach { section ->
            LegalSectionCard(section)
        }
    }
}

@Composable
private fun LegalSectionCard(section: LegalSection) {
    // Blanc doux / off-white (moins lumineux que le blanc pur)
    val softWhiteColor = Color(0xFFF8F8F8)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = softWhiteColor.copy(alpha = 0.95f)
        ),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0).copy(alpha = 0.7f)),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (section.title.isNotEmpty()) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            section.subsections.forEach { subsection ->
                SubsectionContent(subsection)
            }
        }
    }
}

@Composable
private fun SubsectionContent(subsection: Subsection) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (subsection.subtitle.isNotEmpty()) {
            Text(
                text = subsection.subtitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (subsection.content.isNotEmpty()) {
            Text(
                text = subsection.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f),
                lineHeight = 20.sp
            )
        }
    }
}

// Data classes
data class LegalSection(
    val title: String,
    val subsections: List<Subsection>
)

data class Subsection(
    val subtitle: String,
    val content: String
)