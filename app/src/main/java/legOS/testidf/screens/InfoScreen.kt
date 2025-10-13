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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    InfoCompactLayout(navController, isLandscape)
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    InfoLargeLayout(navController, isLandscape)
                }
            }
        }
    }
}

@Composable
private fun InfoCompactLayout(navController: NavController, isLandscape: Boolean) {
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
                text = "Tanks Hunter: Quiz (v 1.0)",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Information générales",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .run {
                        if (!isLandscape) {
                            // Применяем градиент только в вертикальной ориентации
                            this.drawWithContent {
                                drawContent()

                                val fadeHeight = 100.dp.toPx()

                                // Верхний градиент
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        startY = 0f,
                                        endY = fadeHeight
                                    ),
                                    blendMode = BlendMode.DstIn
                                )

                                // Нижний градиент
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Black, Color.Transparent),
                                        startY = size.height - fadeHeight,
                                        endY = size.height
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                            }
                        } else {
                            this // В горизонтальной ориентации градиент не применяется
                        }
                    },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(legalSections.size) { index ->
                    LegalSectionItem(legalSections[index], isLargeScreen = false)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        ReturnButton(navController, Modifier.fillMaxWidth(0.8f))
    }
}

@Composable
private fun InfoLargeLayout(navController: NavController, isLandscape: Boolean) {
    val listState = rememberLazyListState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Левая часть - текст с прокруткой (60%)
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()

                        val fadeHeight = 120.dp.toPx()

                        // Верхний градиент
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 0f,
                                endY = fadeHeight
                            ),
                            blendMode = BlendMode.DstIn
                        )

                        // Нижний градиент
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startY = size.height - fadeHeight,
                                endY = size.height
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(legalSections.size) { index ->
                    LegalSectionItem(legalSections[index], isLargeScreen = true)
                }
            }
        }

        // Правая часть - заголовки и кнопка (40%)
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя часть - заголовки
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "Tanks Hunter: Quiz (v 1.0)",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Information générales",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Нижняя часть - кнопка возврата
            ReturnButton(navController, Modifier.fillMaxWidth(0.7f))
        }
    }
}

@Composable
private fun LegalSectionItem(section: LegalSection, isLargeScreen: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Заголовок раздела (1., 2., 3., и т.д.)
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (isLargeScreen) 22.sp else 20.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Подразделы с параграфами
        section.subsections.forEach { subsection ->
            // Название параграфа (если есть)
            if (subsection.subtitle.isNotEmpty()) {
                Text(
                    text = subsection.subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isLargeScreen) 16.sp else 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Содержимое параграфа
            Text(
                text = subsection.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = if (isLargeScreen) 16.sp else 14.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun ReturnButton(navController: NavController, modifier: Modifier) {
    OutlinedButton(
        onClick = { navController.navigate("main_menu") },
        modifier = modifier
            .height(48.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    ) {
        Text("Retour", style = MaterialTheme.typography.bodyLarge)
    }
}

// Модель данных для юридических разделов
data class LegalSection(
    val title: String,
    val subsections: List<Subsection>
)

data class Subsection(
    val subtitle: String,
    val content: String
)

// Полный текст юридических разделов
private val legalSections = listOf(
    // Пустая первая строка
    LegalSection(
        title = "",
        subsections = listOf(
            Subsection(
                subtitle = "",
                content = ""
            )
        )
    ),
    LegalSection(
        title = "1. Politique de confidentialité",
        subsections = listOf(
            Subsection(
                subtitle = "Introduction",
                content = "L'application \"Tanks Hunter: Quiz\" est une application éducative conçue pour fournir des tests informatifs et éducatifs sur les équipements militaires. Cette Politique de confidentialité explique comment nous gérons les données dans le cadre de l'utilisation de l'Application."
            ),
            Subsection(
                subtitle = "Données collectées",
                content = "L'Application fonctionne principalement hors ligne et ne collecte généralement aucune donnée personnelle identifiable. Cependant, dans le mode \"Test collectif\", les données suivantes sont stockées sur nos serveurs :\n\n- Le nom d'utilisateur saisi par le participant\n- L'heure de création de la session de test\n- Les résultats des tests effectués dans ce mode\n\nCes données sont nécessaires pour permettre le fonctionnement du mode multijoueur et la comparaison des résultats entre les participants. Aucune autre information personnelle telle que l'adresse e-mail, l'emplacement ou d'autres données sensibles n'est collectée."
            ),
            Subsection(
                subtitle = "Utilisation des données",
                content = "Les données collectées dans le mode \"Test collectif\" sont utilisées uniquement pour :\n\n- Afficher les résultats des participants dans les sessions de groupe\n- Permettre la comparaison des scores entre les utilisateurs\n- Gérer les sessions de test en temps réel\n\nCes informations ne sont pas partagées avec des tiers à des fins commerciales et sont conservées uniquement le temps nécessaire au fonctionnement du mode collectif."
            ),
            Subsection(
                subtitle = "Sécurité",
                content = "Nous prenons des mesures raisonnables pour protéger les données stockées dans le cadre du mode \"Test collectif\". Les données sont transmises et stockées de manière sécurisée. Cependant, aucune méthode de transmission sur Internet ou de stockage électronique n'est totalement sécurisée, et nous ne pouvons garantir une sécurité absolue."
            ),
            Subsection(
                subtitle = "Confidentialité des enfants",
                content = "L'Application n'est pas recommandée aux enfants de moins de 14 ans. Nous ne collectons pas sciemment de données personnelles auprès d'enfants de moins de 14 ans. Si vous êtes parent ou tuteur et que vous pensez que votre enfant nous a fourni des informations personnelles, veuillez nous contacter à tanks.hunterquiz@gmail.com afin que nous puissions prendre les mesures appropriées."
            ),
            Subsection(
                subtitle = "Modifications de la politique de confidentialité",
                content = "Nous pouvons mettre à jour cette Politique de confidentialité de temps à autre. Toute modification sera publiée dans l'Application ou sur cette page avec une date de mise à jour révisée. Nous vous encourageons à consulter régulièrement cette politique."
            ),
            Subsection(
                subtitle = "Contactez-nous",
                content = "Si vous avez des questions concernant cette Politique de confidentialité, veuillez nous contacter à l'adresse suivante : tanks.hunterquiz@gmail.com"
            )
        )
    ),
    LegalSection(
        title = "2. Conditions générales d'utilisation",
        subsections = listOf(
            Subsection(
                subtitle = "Acceptation des conditions",
                content = "En téléchargeant, installant et utilisant l'application Tanks Hunter: Quiz (\"l'Application\"), vous acceptez d'être lié par les présentes Conditions générales d'utilisation (\"Conditions\"). Si vous n'acceptez pas ces Conditions, veuillez ne pas utiliser l'Application."
            ),
            Subsection(
                subtitle = "Description de l'Application",
                content = "L'Application propose des tests éducatifs et informatifs sur les équipements militaires, organisés en plusieurs catégories, avec un test final. L'Application fonctionne principalement hors ligne et ne nécessite pas de connexion Internet pour la plupart de ses fonctionnalités. Cependant, le mode \"Test collectif\" nécessite une connexion Internet stable pour permettre le jeu multijoueur en temps réel, la synchronisation des résultats et la gestion des sessions de groupe."
            ),
            Subsection(
                subtitle = "Utilisation autorisée",
                content = "Vous êtes autorisé à utiliser l'Application uniquement à des fins personnelles et non commerciales. Vous ne devez pas :\n\n- Modifier, désosser, décompiler ou tenter d'extraire le code source de l'Application.\n- Utiliser l'Application à des fins illégales ou non autorisées.\n- Copier, distribuer ou reproduire le contenu de l'Application sans autorisation préalable."
            ),
            Subsection(
                subtitle = "Propriété intellectuelle",
                content = "Tout le contenu de l'Application, y compris les textes, images et autres éléments, est protégé par les lois sur la propriété intellectuelle. Vous ne pouvez pas reproduire, distribuer ou utiliser ce contenu sans autorisation expresse, sauf dans le cadre de l'utilisation normale de l'Application."
            ),
            Subsection(
                subtitle = "Paiement",
                content = "L'Application est disponible à l'achat via le Google Play Store. Le prix est indiqué dans le magasin d'applications. Aucun abonnement ou achat intégré n'est requis."
            ),
            Subsection(
                subtitle = "Limitation de responsabilité",
                content = "L'Application est fournie \"telle quelle\". Nous ne garantissons pas que l'Application sera exempte d'erreurs ou fonctionnera sans interruption. Dans la mesure permise par la loi, nous ne serons pas responsables des dommages directs, indirects, accessoires ou consécutifs découlant de l'utilisation de l'Application."
            ),
            Subsection(
                subtitle = "Modifications des Conditions",
                content = "Nous nous réservons le droit de modifier ces Conditions à tout moment. Les modifications seront publiées dans l'Application ou sur cette page avec une date de mise à jour révisée."
            ),
            Subsection(
                subtitle = "Contactez-nous",
                content = "Pour toute question concernant ces Conditions, veuillez nous contacter à : tanks.hunterquiz@gmail.com"
            )
        )
    ),
    LegalSection(
        title = "3. Licence",
        subsections = listOf(
            Subsection(
                subtitle = "Licence d'utilisation",
                content = "L'application Tanks Hunter: Quiz (\"l'Application\") est concédée sous licence, et non vendue, à l'utilisateur pour une utilisation personnelle et non commerciale conformément aux présentes conditions."
            ),
            Subsection(
                subtitle = "Étendue de la licence",
                content = "- Vous êtes autorisé à installer et utiliser l'Application sur un appareil compatible que vous possédez ou contrôlez.\n- La licence est non transférable et ne peut être partagée avec d'autres utilisateurs ou appareils.\n- Vous ne pouvez pas copier, modifier, distribuer, vendre ou louer l'Application ou une partie de celle-ci sans autorisation écrite préalable."
            ),
            Subsection(
                subtitle = "Propriété intellectuelle",
                content = "L'Application, y compris son code, ses textes, ses images et tout autre contenu, est la propriété de l'équipe de développement d'Arsenal Quiz ou de ses concédants de licence. Tous les droits non expressément accordés dans cette licence sont réservés."
            ),
            Subsection(
                subtitle = "Restrictions",
                content = "Vous ne devez pas :\n- Tenter de désosser, décompiler ou extraire le code source de l'Application.\n- Utiliser l'Application à des fins contraires aux lois applicables.\n- Supprimer ou modifier les avis de droits d'auteur ou autres mentions de propriété dans l'Application."
            ),
            Subsection(
                subtitle = "Résiliation",
                content = "La licence prendra fin automatiquement si vous ne respectez pas les termes de cette licence. Dans ce cas, vous devrez désinstaller l'Application et cesser toute utilisation."
            )
        )
    ),
    LegalSection(
        title = "4. Informations de contact",
        subsections = listOf(
            Subsection(
                subtitle = "",
                content = "Pour toute question, commentaire ou demande concernant l'application \"Tanks Hunter: Quiz\", veuillez nous contacter à l'adresse suivante :\n\nEmail : tanks.hunterquiz@gmail.com\n\nNous nous efforçons de répondre à toutes les demandes dans les plus brefs délais."
            )
        )
    ),
    LegalSection(
        title = "5. Clause de non-responsabilité",
        subsections = listOf(
            Subsection(
                subtitle = "Contenu de l'Application",
                content = "L'application \"Tanks Hunter: Quiz\" fournit des tests éducatifs et informatifs sur les équipements militaires. Le contenu, y compris les textes et les images, est fourni à des fins éducatives et informatives uniquement. Nous ne garantissons pas l'exactitude, l'exhaustivité ou l'actualité des informations contenues dans l'Application."
            ),
            Subsection(
                subtitle = "Sources des contenus",
                content = "Les textes et images utilisés dans l'Application proviennent de sources publiques disponibles sur Internet. Bien que nous ayons pris soin de sélectionner des contenus libres de droits ou utilisés conformément à la législation applicable, nous ne pouvons garantir que tout le contenu est exempt de droits d'auteur ou d'autres restrictions. Si vous pensez que du contenu de l'Application viole vos droits, veuillez nous contacter à tanks.hunterquiz@gmail.com pour résoudre la situation."
            ),
            Subsection(
                subtitle = "Responsabilité",
                content = "Dans la mesure permise par la loi, l'équipe de développement de \"Tanks Hunter: Quiz\" ne sera pas responsable des dommages directs, indirects, accessoires, spéciaux ou consécutifs découlant de l'utilisation ou de l'incapacité à utiliser l'Application. L'Application est fournie \"telle quelle\", sans garantie d'aucune sorte, expresse ou implicite."
            ),
            Subsection(
                subtitle = "Utilisation à vos propres risques",
                content = "Vous utilisez l'Application à vos propres risques. Nous ne sommes pas responsables des erreurs ou omissions dans le contenu, ni des conséquences découlant de l'utilisation des informations fournies."
            ),
            Subsection(
                subtitle = "Contact",
                content = "Pour toute réclamation ou question concernant cette Clause de non-responsabilité, veuillez nous contacter à : tanks.hunterquiz@gmail.com \n\nSi vous souhaitez rester informé des mises à jour ou poser une question, rejoignez notre groupe Telegram : t.me/Tanks_Hunter"
            )
        )
    ),
    // Пустая последняя строка
    LegalSection(
        title = "",
        subsections = listOf(
            Subsection(
                subtitle = "",
                content = ""
            )
        )
    )
)