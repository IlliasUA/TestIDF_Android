package com.example.quizapp

object Recon_Data {
    val QUESTION = listOf(
        Question(
            image = "reco1.jpg",
            additionalImages = listOf("reco1_extra1.jpg", "reco1_extra2.jpg", "reco1_extra3.jpg", "reco1_extra4.jpg"),
            correct = "BRDM-2",
            options = listOf("BRDM-2", "BTR-60", "BTR-70", "VAB"),
            description = """
                Le BRDM-2 est un véhicule blindé de reconnaissance russe. Il a été développé en Union soviétique au début des années 1950.
                Le BRDM-2 est entré en service en 1962.
                Son armement est composé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm,
                identiques au véhicules blindés de transport de troupe russe BTR-60/BTR-70/BTR-80.
                Le BRDM-2, en différents versions, est utilisé par les forces armées de plus de 50 pays dans le monde.
                Equipage : 4
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco2.jpg",
            additionalImages = listOf("reco2_extra1.jpg", "reco2_extra2.jpg", "reco2_extra3.jpg", "reco2_extra4.jpg"),
            correct = "Dingo",
            options = listOf("Fuchs", "Eagle V", "Humvee", "Dingo"),
            description = """
                L'ATF Dingo est un véhicule militaire blindé produit par allemand.
                L'armement standard du Dingo est une tourelle de mitrailleuse de calibre 7,62 mm.
                Elle peut être remplacée par une mitrailleuse 12,7 mm ou par un lance-grenades automatique HK GMG.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco3.jpg",
            additionalImages = listOf("reco3_extra1.jpg", "reco3_extra2.jpg", "reco3_extra3.jpg"),
            correct = "Eagle V",
            options = listOf("PVP", "URO", "Eagle V", "Humvee"),
            description = """
                L’Eagle V est un véhicule blindé multirôle à roues 4×4 suisse.
                Il est développé dans les années 2010 par GDELS-Mowag, General Dynamics European Land Systems-Mowag.
                L’Eagle V est entré en service en 2015.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco4.jpg",
            additionalImages = listOf("reco4_extra1.jpg", "reco4_extra2.jpg", "reco4_extra3.jpg", "reco4_extra4.jpg"),
            correct = "Komatsu LAV",
            options = listOf("GAZ 2330 Tigr", "Komatsu LAV", "VBL", "Cobra"),
            description = """
                Le Komatsu LAV, Light Armored Vehicle, est un véhicule blindé léger à roues 4×4 japonais. Il est développé dans les années 1990.
                Le Komatsu LAV est entré en service en 2001. Il est optimisé pour les missions de reconnaissance et de patrouille.
                La caisse du véhicule est protégée contre la munition de 7,62 mm.
                Le véhicule peut recevoir en superstructure une mitrailleuse lourde de 12,7 mm ou une mitrailleuse de 7,62 mm.
                La version antichar est équipée des missiles Type 87 ou Type 01 LMAT. Equipage : 4 + 1.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco5.jpg",
            additionalImages = listOf("reco5_extra1.jpg", "reco5_extra2.jpg", "reco5_extra3.jpg", "reco5_extra4.jpg"),
            correct = "Serval",
            options = listOf("Serval", "Griffon", "Fennek", "Fuchs"),
            description = """
                Le VBMR-L (Véhicules Blindés Multi-Rôles Légers) Serval est un véhicule blindé à roues 4×4 français.
                Il est développé par Nexter Systems et Texelis. Le VBMR-L Serval est entré en service en 2022.
                Il sera développé en plusieurs versions:
                Transport de troupe, Poste de commandement, Observation d’artillerie, Véhicule du génie, Evacuation sanitaire, SA2R, Groupe de communication tactique, etc.
                Le VBMR-L Serval est équipé des moyens de communication et du système de d’information et de combat SCORPION (SICS).
                Armement: Mitrailleuse 12,7 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco6.jpg",
            additionalImages = listOf("reco6_extra1.jpg", "reco6_extra2.jpg", "reco6_extra3.jpg", "reco6_extra4.jpg"),
            correct = "GAZ 2330 Tigr",
            options = listOf("ZFB-05", "Komatsu LAV", "Eagle V", "GAZ 2330 Tigr"),
            description = """
                Le GAZ-2330 Tigr est un véhicule blindé à roues 4×4 russe. Il est développé au début des années 2000.
                Le Tigr est entré en service en 2006.
                Le GAZ-2330 Tigr est un véhicule blindé multirôle. Il est utilisé pour les missions de transport de troupe, patrouille, reconnaissance, port d’équipement.
                Pas d’armement organique.
                Equipage : 2 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco7.jpg",
            additionalImages = listOf("reco7_extra1.jpg", "reco7_extra2.jpg", "reco7_extra3.jpg", "reco7_extra4.jpg"),
            correct = "URO Vamtac",
            options = listOf("Humvee", "Tigre", "URO Vamtac", "Grizzly"),
            description = """
                Le Vamtac est un véhicule blindé de léger à roues 4×4 espagnol. Il est développé dans les années 1980-1990 par URO Vehiculos Especiales.
                Le Vamtac est entré en service en 1998.
                Le Vamtac est exporté en Arabie saoudite, en République Dominicaine, au Ghana, en Indonésie, en Irak, en Malaisie, au Maroc, au Portugal, en Roumanie et à Singapour.
                Il est également commercialisé dans sa version civile.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco8.jpg",
            additionalImages = listOf("reco8_extra1.jpg"),
            correct = "VBL",
            options = listOf("VBL", "Cobra", "ZFB-05", "Komatsu LAV"),
            description = """
                Le VBL, Véhicule Blindé Léger, est un véhicule blindé léger à roues 4×4 français. Il a été développé dans les années 1980.
                Le VBL est entré en service en 1990.
                Le VBL est particulièrement adapté pour les missions de reconnaissance par ses dimensions et son niveau sonore réduits.
                La version de base possède un équipage de trois hommes: pilote, chef de bord et éclaireur.
                Le VBL peut intégrer différents systèmes d’armes : mitrailleuse 7.62 mm sur une circulaire, mitrailleuse 12.7 mm dans un tourelleau, lance-grenades automatique de 40 mm,
                missiles antichar MILAN ou TOW. Le VBL possède la capacité d’amphibie et est propulsé dans l’eau par une hélice.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco9.jpg",
            additionalImages = listOf("reco9_extra1.jpg", "reco9_extra2.jpg", "reco9_extra3.jpg", "reco9_extra4.jpg"),
            correct = "Fennek",
            options = listOf("Fuchs", "Fennek", "Dingo", "Serval"),
            description = """
                Le Fennek est un véhicule blindé à roues 4×4 allemand. Il a été développé dans les années 1990.
                Le Fennek est entré en service en 2003.
                Le Fennek possède le poste de pilotage au centre à l’avant du véhicule et deux membres de l’équipage sont placés côte à côte dans la partie centrale.
                Le groupe motopropulseur est placé à l’arrière du véhicule.
                La version reconnaissance est équipée d’un module optique escamotable. Différents modèles sont développés pour les unités de reconnaissance,
                antichar, génie, défense sol-air, observation artillerie.
                Armement: 7,62 mm / 12,7 mm / Lance-grenade automatique de 40 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco10.jpg",
            additionalImages = listOf("reco10_extra1.jpg", "reco10_extra2.jpg", "reco10_extra3.jpg", "reco10_extra4.jpg"),
            correct = "Grizzly",
            options = listOf("Pandur-1", "M-113", "Grizzly", "Eagle V"),
            description = """
                Dévoilé au grand public pour la première fois lors de la Fête nationale du 14 juillet 2023.
                Conçu pour des missions de reconnaissance et d'intervention les forces spéciales françaises.
                Le Grizzly est équipé d'une tourelle circulaire capable de recevoir des armes lourdes, telles que le M2HB en calibre 12,7 mm ou
                le LGA en calibre 40 mm, ainsi que trois supports de mitrailleuses en calibre 7,62 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco11.jpg",
            additionalImages = listOf("reco11_extra1.jpg", "reco11_extra2.jpg", "reco11_extra3.jpg"),
            correct = "Humvee",
            options = listOf("Humvee", "URO Vamtac", "Dingo", "Eagle V"),
            description = """
                Le HMMWV, High Mobility Multipurpose Wheeled Vehicle, est une famille de véhicules à roues 4×4 américains.
                Il est développé dans les années 1980.
                Le HMMWV est entré en service en 1984.
                Le HMMWV est appelé plus communément Humvee. Il est très largement diffusé dans le monde.
                A partir de 2018, le HMMWV commence à être remplacé par le véhicule blindé de la catégorie MRAP JLTV.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco12.jpg",
            additionalImages = listOf("reco12_extra1.jpg", "reco12_extra2.jpg", "reco12_extra3.jpg", "reco12_extra4.jpg"),
            correct = "Fuchs",
            options = listOf("Fennek", "Pandur-2", "Piranha V", "Fuchs"),
            description = """
                Le TPz1 Fuchs est un véhicule blindé de transport de troupe à roues 6×6 allemand. Il est développé à la fin des années 1970.
                Armement: Mitrailleuse 7.62 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco13.jpg",
            additionalImages = listOf("reco13_extra1.jpg", "reco13_extra2.jpg", "reco13_extra3.jpg"),
            correct = "Piranha V",
            options = listOf("Pandur-1", "Pandur-2", "Piranha V", "Stryker"),
            description = """
                Le véhicule de combat d'infanterie Piranha V (VCI) est la variante de cinquième génération de la famille de véhicules Mowag Piranha.
                Lieu d’origine : Suisse.
                Le Piranha V est entré en service en 2015.
                Utilisé par: Danemark / Monaco / Espagne / Roumanie.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco14.jpg",
            additionalImages = listOf("reco14_extra1.jpg", "reco14_extra2.jpg", "reco14_extra3.jpg", "reco14_extra4.jpg"),
            correct = "BMR-600",
            options = listOf("Namer", "Pandur-1", "Piranha V", "BMR-600"),
            description = """
                Le BMR-600 est un véhicule blindé de transport de troupe à roues 6×6 espagnol. Il est développé dans les années 1970.
                Le BMR-600 est entré en service en 1979.
                Près de 1 500 BMR-600 ont été produits en différentes versions. Il a été exporté en Egypte, Arabie saoudite et Pérou.
                Armement: 12,7 mm, M2HB.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco15.jpg",
            additionalImages = listOf("reco15_extra1.jpg", "reco15_extra2.jpg", "reco15_extra3.jpg", "reco15_extra4.jpg"),
            correct = "Wiesel",
            options = listOf("VHM", "Wiesel", "Piranha V", "Cobra"),
            description = """
                Le Wiesel est un véhicule blindé de reconnaissance allemand. Il est développé dans les années 1980.
                Le Wiesel est entré en service en 1989.
                Le Wiesel est développé en deux principales versions :
                le Wiesel 1 Mk 20 armé d’un canon automatique a double alimentation Mk 20 Rh 202 de 20 mm destiné aux missions de reconnaissance et d’appui-feu
                et le Wiesel 1 TOW destiné au combat antichar.
                Equipage : 2 – Wiesel Mk20 / 3 – Wiesel TOW.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco16.jpg",
            additionalImages = listOf("reco16_extra1.jpg", "reco16_extra2.jpg", "reco16_extra3.jpg", "reco16_extra4.jpg"),
            correct = "Freccia",
            options = listOf("Freccia", "Stryker", "VBCI", "XA-180"),
            description = """
                Le VBM Freccia, Veicolo Blindato Medio, est un véhicule blindé de combat d’infanterie à roues 8×8 italien. Il est développé dans les années 2000.
                Le Freccia est entré en service en 2008. Le châssis du Freccia est développé à partir du châssis du char léger italien B1 Centauro.
                Le Freccia est équipé d’une tourelle biplace HITFIST 25 Plus armée d’un canon Oerlikon KBA de 25 mm.
                L’armement est complété par deux mitrailleuses de 7,62 mm et la capacité de recevoir les missiles antichars Spike.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco17.jpg",
            additionalImages = listOf("reco17_extra1.jpg", "reco17_extra2.jpg", "reco17_extra3.jpg", "reco17_extra4.jpg"),
            correct = "XA-180",
            options = listOf("ZTD-05", "Pandur-1", "XA-180", "Stryker"),
            description = """
                Le XA-180 est un véhicule blindé de transport de troupe à roues 6×6 finlandais. Il a été développé dans les années 1980.
                Le XA-180 est entré en service en 1984.
                Le XA-180 est connu également sous le nom Pasi, Panssari Sisu – Blindé Sisu en finlandais.
                Il est développé à la demande de l’armée finlandaise pour remplacer les véhicules blindés de transport de troupe BTR-60 soviétiques.
                Le XA-180 est développé en plusieurs versions et exporté aux Pays-Bas, Norvège, Suède, Irlande et Estonie.
                Armement: 12,7 mm ou 7,62 mm.
                Equipage : 2 + 10 à 16.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco18.jpg",
            additionalImages = listOf("reco15_extra1.jpg", "reco15_extra2.jpg", "reco15_extra3.jpg", "reco15_extra4.jpg"),
            correct = "Wiesel",
            options = listOf("VHM", "ZTD-05", "XA-180", "Wiesel"),
            description = """
                Le Wiesel est un véhicule blindé de reconnaissance allemand. Il est développé dans les années 1980.
                Le Wiesel est entré en service en 1989.
                Le Wiesel est développé en deux principales versions :
                le Wiesel 1 Mk 20 armé d’un canon automatique a double alimentation Mk 20 Rh 202 de 20 mm destiné aux missions de reconnaissance et d’appui-feu
                et le Wiesel 1 TOW destiné au combat antichar.
                Equipage : 2 – Wiesel Mk20 / 3 – Wiesel TOW.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco19.jpg",
            additionalImages = listOf("reco19_extra1.jpg", "reco19_extra2.jpg", "reco19_extra3.jpg", "reco19_extra4.jpg"),
            correct = "Pandur-2",
            options = listOf("Pandur-2", "Stryker", "Piranha V", "Pandur-1"),
            description = """
                Pandur II: véhicule blindé de transport de troupes originaire d'Autriche.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco20.jpg",
            additionalImages = listOf("reco20_extra1.jpg", "reco20_extra2.jpg", "reco20_extra3.jpg", "reco20_extra4.jpg", "reco20_extra5.jpg", "reco20_extra6.jpg"),
            correct = "Cobra",
            options = listOf("VBL", "ZFB-05", "Cobra", "Komatsu LAV"),
            description = """
                Le Cobra est un véhicule blindé de combat à roues 4×4 turc. Il est développé dans les années 1990.
                Le Cobra est employé par les forces armées de la Turquie, de la Géorgie, de l’Azerbaïdjan, du Pakistan,
                de l’Algérie, du Bahreïn, des Emirats arabes unis, du Kazakhstan, du Kosovo, du Nigeria, du Bangladesh de la Macédoine,
                du Monténégro et de la Slovénie.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco21.jpg",
            additionalImages = listOf("reco21_extra1.jpg", "reco21_extra2.jpg", "reco21_extra3.jpg", "reco21_extra4.jpg"),
            correct = "ULAN-30",
            options = listOf("Warrior", "ULAN-30", "Marder", "Bradley"),
            description = """
                Le Ulan est un véhicule blindé de combat d’infanterie autrichien. Il a été développé au début des années 1990.
                Le Ulan est entré en service en 2001.
                Canon : 30 mm, Mauser MK 30/2.
                Mitrailleuse coaxiale : 7,62 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco22.jpg",
            additionalImages = listOf("reco22_extra1.jpg", "reco22_extra2.jpg", "reco22_extra3.jpg", "reco22_extra4.jpg"),
            correct = "Pizarro",
            options = listOf("Bradley", "Puma", "Marder", "Pizarro"),
            description = """
                Le Pizarro est un véhicule blindé de combat d’infanterie espagnol. Il est développé au début des années 1990.
                Le Pizarro est entré en service en 2001.
                Le Pizarro est développé dans le cadre du programme ASCOD, Austrian Spanish Cooperation Development,
                du futur véhicule blindé de combat d’infanterie pour les forces armées autrichiennes et espagnoles.
                La version autrichienne, baptisée Ulan, possède plusieurs différences.
                Canon : 30 mm.
                Mitrailleuse coaxiale : 7,62 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco23.jpg",
            additionalImages = listOf("reco23_extra1.jpg", "reco23_extra2.jpg", "reco23_extra3.jpg", "reco23_extra4.jpg"),
            correct = "PTL-02",
            options = listOf("PTL-02", "BTR-80A", "VAB-HOT", "ZTD-05"),
            description = """
                Le PTL-02 est un véhicule à armement lourd chinois. Il est développé dans les années 1990 – début 2000.
                Le PTL-02 est entré en service en 2012.
                Le PTL-02 est développé pour doter les unités d’infanterie d’un appui-feu.
                Il emploie le châssis renforcé du véhicule blindé de transport de troupe à roues 6×6 WZ-551.
                L’armement principal est composé du canon de 100 mm. La dotation en munition est de 30 obus.
                Equipage : 4 + 1.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco24.jpg",
            additionalImages = listOf("reco24_extra1.jpg", "reco24_extra2.jpg", "reco24_extra3.jpg", "reco24_extra4.jpg"),
            correct = "Centauro",
            options = listOf("Freccia", "Centauro", "Pizarro", "Piranha V"),
            description = """
                Le B1 Centauro est un char léger à roues 8×8 italien. Il est développé dans les années 1980 par le consortium Iveco – Fiat.
                Le B1 Centauro est entré en service en 1991.
                Le B1 Centauro est destiné aux unités de reconnaissance.
                L’arrière du châssis possède une porte pour faciliter l’accès à bord et le chargement des munitions.
                En plus de son équipage, quatre fantassins peuvent prendre place à l’arrière du véhicule sur des sièges rabattables,
                à la place d’une partie du stock de munition. Le B1 Centauro est armé d’un canon de 105 mm stabilisé, à chargement manuel.
                Le châssis du B1 Centauro a donné naissance au véhicule d’infanterie VBM Freccia.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco25.jpg",
            additionalImages = listOf("reco25_extra1.jpg", "reco25_extra2.jpg", "reco25_extra3.jpg", "reco25_extra4.jpg"),
            correct = "AAV",
            options = listOf("VCR", "AAV", "Lasar-3", "ACV-1"),
            description = """
                L’AAV, Assault Amphibious Vehicle, est un véhicule blindé de transport de troupe amphibie américaine. Il a été développé des années 1970.
                L’AAV est entré en service en 1972 sous le nom LVTP-7.
                Mitrailleuse : M2HB 12.7 mm.
                Lance-grenades automatiques: Mk19 40 mm.
                Equipage : 3 + 21.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco26.jpg",
            additionalImages = listOf("reco25_extra1.jpg", "reco25_extra2.jpg", "reco25_extra3.jpg", "reco25_extra4.jpg"),
            correct = "AAV",
            options = listOf("AAV", "Foxhound_Ocelot", "Higuard", "ACV-1"),
            description = """
                L’AAV, Assault Amphibious Vehicle, est un véhicule blindé de transport de troupe amphibie américaine. Il a été développé des années 1970.
                L’AAV est entré en service en 1972 sous le nom LVTP-7.
                Mitrailleuse : M2HB 12.7 mm.
                Lance-grenades automatiques: Mk19 40 mm.
                Equipage : 3 + 21.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco27.jpg",
            additionalImages = listOf("reco27_extra1.jpg", "reco27_extra2.jpg", "reco27_extra3.jpg", "reco27_extra4.jpg"),
            correct = "Achzarit",
            options = listOf("FV430 Bulldog", "M-113", "Achzarit", "Ajax"),
            description = """
                L’Achzarit est un véhicule blindé de transport de troupe lourd israélien.
                Il est développé dans les années 1980 à partir du châssis du char de combat T-55.
                L’Achzarit est entré en service en 1988.
                L’Achzarit Mk II est entré en service en 1997.
                Il possède la nouvelle motorisation de 850 ch et le train de roulement renforcé des nouveaux galets, les barbotins et les chenilles.
                Mitrailleuse : 7.62 mm MAG-58 télé-opérée ou 12.7 mm télé-opérée.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco28.jpg",
            additionalImages = listOf("reco10_extra1.jpg", "reco10_extra2.jpg", "reco10_extra3.jpg", "reco10_extra4.jpg"),
            correct = "Grizzly",
            options = listOf("Grizzly", "Jackal", "GRF Vector", "K-1"),
            description = """
                Dévoilé au grand public pour la première fois lors de la Fête nationale du 14 juillet 2023.
                Conçu pour des missions de reconnaissance et d'intervention les forces spéciales françaises.
                Le Grizzly est équipé d'une tourelle circulaire capable de recevoir des armes lourdes, telles que le M2HB en calibre 12,7 mm ou
                le LGA en calibre 40 mm, ainsi que trois supports de mitrailleuses en calibre 7,62 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco29.jpg",
            additionalImages = listOf("reco29_extra1.jpg", "reco29_extra2.jpg", "reco29_extra3.jpg", "reco29_extra4.jpg"),
            correct = "Jackal",
            options = listOf("Grizzly", "Jackal", "GRF Vector", "K-1"),
            description = """
                L’HMT 400 est un véhicule blindé modulaire.
                Il est développé par Supacat à partir du châssis ATMP en version 4×4 HMT 400, 6×6 HMT 600 et 6×6 HMT Extenda Mk2.
                Le Jackal transporte trois membres d’équipage et peut être employé pour des missions de reconnaissance.
                Il est doté d’un important niveau de protection anti-mines et peut recevoir un kit de surblindage.
                L’armement est composé d’une mitrailleuse lourde de 12,7 mm ou d’un lance-grenade automatique et d’une mitrailleuse de 7,62 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco30.jpg",
            additionalImages = listOf("reco30_extra1.jpg", "reco30_extra2.jpg", "reco30_extra3.jpg", "reco30_extra4.jpg"),
            correct = "ZTD-05",
            options = listOf("BMD-1", "BMP-1", "ZTD-05", "ZBD-05"),
            description = """
                Le ZTD-05 est un Véhicule à Armement Lourd chinois. Il a été développé à la fin des années 1990.
                Le ZTD-05 est entré en service en 2005.
                Le ZTD-05 utilise le châssis du véhicule de combat d’infanterie amphibie chinois ZBD-05, appelé également Type-05.
                La famille des véhicules ZBD-ZTD développée pour les unités de l’infanterie de marine est optimisée pour les missions de débarquement.
                Il est équipé d’une tourelle biplace armée d’un canon stabilisé de 105 mm. Il est capable de tirer en mouvement sur terre et sur l’eau.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco31.jpg",
            additionalImages = listOf("reco30_extra1.jpg", "reco30_extra2.jpg", "reco30_extra3.jpg", "reco30_extra4.jpg"),
            correct = "ZTD-05",
            options = listOf("BMD-1", "BMP-1", "ZTD-05", "ZBD-05"),
            description = """
                Le ZTD-05 est un Véhicule à Armement Lourd chinois. Il a été développé à la fin des années 1990.
                Le ZTD-05 est entré en service en 2005.
                Le ZTD-05 utilise le châssis du véhicule de combat d’infanterie amphibie chinois ZBD-05, appelé également Type-05.
                La famille des véhicules ZBD-ZTD développée pour les unités de l’infanterie de marine est optimisée pour les missions de débarquement.
                Il est équipé d’une tourelle biplace armée d’un canon stabilisé de 105 mm. Il est capable de tirer en mouvement sur terre et sur l’eau.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco32.jpg",
            additionalImages = listOf("reco20_extra1.jpg", "reco20_extra2.jpg", "reco20_extra3.jpg", "reco20_extra4.jpg", "reco20_extra5.jpg", "reco20_extra6.jpg"),
            correct = "Cobra",
            options = listOf("VBL", "ZFB-05", "Cobra", "Komatsu LAV"),
            description = """
                Le Cobra est un véhicule blindé de combat à roues 4×4 turc. Il est développé dans les années 1990.
                Le Cobra est employé par les forces armées de la Turquie, de la Géorgie, de l’Azerbaïdjan, du Pakistan,
                de l’Algérie, du Bahreïn, des Emirats arabes unis, du Kazakhstan, du Kosovo, du Nigeria, du Bangladesh de la Macédoine,
                du Monténégro et de la Slovénie.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco33.jpg",
            additionalImages = listOf("reco33_extra1.jpg", "reco33_extra2.jpg", "reco33_extra3.jpg", "reco33_extra4.jpg"),
            correct = "BMO-T",
            options = listOf("BMO-T", "M-113", "Achzarit", "Namer"),
            description = """
                Le BMO-T est un véhicule blindé de transport de troupe lourd russe.
                Il est développé dans les années 1990 à partir du châssis du char de combat T-72.
                Le BMO-T est entré en service en 2001 en quantité restreinte.
                La Russie est l’unique utilisatrice des BMO-T.
                Mitrailleuse : 12.7 mm.
                Lance-roquettes : 30 PRO-A Shmel à effets thermobarique.
                Equipage : 2 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco34.jpg",
            additionalImages = listOf("reco34_extra1.jpg", "reco34_extra2.jpg", "reco34_extra3.jpg", "reco34_extra4.jpg"),
            correct = "Stryker",
            options = listOf("Pandur-2", "Namer", "Stryker", "Boxer"),
            description = """
                Le M1126 Stryker est un véhicule blindé transport de troupe à roues 8×8 américain.
                Il a été développé à la fin des années 1990.
                Le M1126 Stryker est entré en service en 2002.
                Le M1126 Stryker est développé a partir du LAV-III Kodiak canadien sur la base mécanique du véhicule blindé Piranha III.
                Il possède le tourelleau téléopéré RWS M151 Protecteur qui peut recevoir une mitrailleuse lourde M2HB de 12,7 mm
                ou un lance-grenade automatique Mk 19 de 40 mm.
                Equipage : 2 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco35.jpg",
            additionalImages = listOf("reco35_extra1.jpg", "reco35_extra2.jpg", "reco35_extra3.jpg", "reco27_extra4.jpg"),
            correct = "Namer",
            options = listOf("Achzarit", "Pandur-2", "ULAN-30", "Namer"),
            description = """
                Le Namer est un véhicule blindé de transport de troupe lourd israélien. Il est développé dans les années 2000.
                Le Namer est entré en service en 2008.
                Le Namer est développé à partir du châssis du char de combat Merkava Mk IV.
                Son armement est composé des mitrailleuses de 12,7 mm et/ou de 7,62 mm.
                En juillet 2017, le Namer est dévoilé en version VBCI, Véhicule Blindé de Combat d’Infanterie, lourd.
                Il est doté d’une tourelle téléopérée, armée d’un canon automatique de 30 mm.
                Mitrailleuse : 12.7 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco36.jpg",
            additionalImages = listOf("reco36_extra1.jpg", "reco36_extra2.jpg", "reco36_extra3.jpg", "reco36_extra4.jpg"),
            correct = "Bradley",
            options = listOf("Warrior", "Bradley", "Marder", "Puma"),
            description = """
                Le M2 Bradley est un véhicule de combat d’infanterie américain. Il est développé dans les années 1970.
                Le M2 Bradley est entré en service en 1981.
                Le M2 Bradley est armé d’un canon Bushmaster de 25 mm.
                A partir de 1986, le modèle M1A1 reçoit deux missiles antichar TOW.
                Le M2 Bradley a suivi plusieurs campagnes de modernisation améliorant ses capacités au combat, son niveau de protection,
                sa mobilité et divers équipements.
                Le modèle M2A3 intègre l’équipement de Numérisation de l’Espace de Bataille.
                A partir du M2 Bradley, une version de reconnaissance est développée sous le nom M3 Bradley.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco37.jpg",
            additionalImages = listOf("reco37_extra1.jpg", "reco37_extra2.jpg", "reco37_extra3.jpg", "reco37_extra4.jpg"),
            correct = "Marder",
            options = listOf("Warrior", "Bradley", "Marder", "Puma"),
            description = """
                Le SPz Marder est un véhicule de combat d’infanterie allemand. Il est développé dans les années 1960.
                Le SPz Marder est entré en service en 1971.
                Le SPz Marder est équipé d’une tourelle biplace en superstructure armée d’un canon automatique Rh 202 de 20 mm
                et d’une mitrailleuse MG3A1 de 7,62 mm.
                Le SPz Marder a été livré en Indonésie, en Jordanie et au Chili. Au sein des forces armées allemandes,
                le SPz Marder est en cours de remplacement par le nouveau véhicule de combat d’infanterie SPz Puma.
                Equipage : 3 + 6 à 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco38.jpg",
            additionalImages = listOf("reco38_extra1.jpg", "reco38_extra2.jpg", "reco38_extra3.jpg", "reco38_extra4.jpg"),
            correct = "Puma",
            options = listOf("Warrior", "Bradley", "Marder", "Puma"),
            description = """
                Le SPz Puma est un véhicule de combat d’infanterie lourd allemand. Il est développé dans les années 2000.
                Le SPz Puma est développé pour remplacer les SPz Marder au sein des unités d’infanterie mécanisée allemandes.
                Le SPz Puma est équipé d’une tourelle téléopérée armée d’un canon MK.30-2 de 30 mm est d’une mitrailleuse coaxiale de 5.56 mm.
                Il possède deux niveaux de protection. Le niveau A, Aérotransportable, avec la protection anti-mine complète et la protection balistique
                contre la munition de 30 mm de face et de 14,5 mm en latéral.
                La version SPz Puma niveau A reste aérotransportable par avion de transport A-400M.
                Equipage : 3 + 6.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco39.jpg",
            additionalImages = listOf("reco34_extra1.jpg", "reco34_extra2.jpg", "reco34_extra3.jpg", "reco34_extra4.jpg"),
            correct = "Stryker",
            options = listOf("Freccia", "BMR-600", "Pizarro", "Stryker"),
            description = """
                Le M1126 Stryker est un véhicule blindé transport de troupe à roues 8×8 américain.
                Il a été développé à la fin des années 1990.
                Le M1126 Stryker est entré en service en 2002.
                Le M1126 Stryker est développé a partir du LAV-III Kodiak canadien sur la base mécanique du véhicule blindé Piranha III.
                Il possède le tourelleau téléopéré RWS M151 Protecteur qui peut recevoir une mitrailleuse lourde M2HB de 12,7 mm
                ou un lance-grenade automatique Mk 19 de 40 mm.
                Equipage : 2 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco40.jpg",
            additionalImages = listOf("reco40_extra1.jpg", "reco40_extra2.jpg", "reco40_extra3.jpg", "reco40_extra4.jpg"),
            correct = "M-113",
            options = listOf("M-113", "VHM", "Bradley", "K-1"),
            description = """
                Le M113 est un véhicule blindé de transport de troupe américain. Il est développé dans les années 1950.
                Le M113 est entré en service en 1960.
                Le M113 est développé pour les unités d’infanterie pour des missions de transport d’un groupe de fantassin.
                Il a suivi plusieurs campagnes de modernisation.
                Le M113 est dérivé en une large gamme de véhicules spécialisés : poste de commandement, mortier, sanitaire, défense sol-air,
                véhicule du génie, lance-missile antichar, etc. Il a été adopté par de très nombreuses forces armées dans le monde.
                Mitrailleuse : 12.7 mm.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco41.jpg",
            additionalImages = listOf("reco41_extra1.jpg", "reco41_extra2.jpg", "reco41_extra3.jpg", "reco41_extra4.jpg"),
            correct = "Boxer",
            options = listOf("Stryker", "Boxer", "Pandur-2", "Piranha V"),
            description = """
                Le Boxer est un véhicule blindé de combat à roues 8×8 allemand. Il est développé dans les années 2000.
                Le Boxer est entré en service en 2009.
                Le Boxer possède une structure modulaire. Le châssis, avec le compartiment moteur et le poste de pilotage,
                peut recevoir différents modules spécialisés.
                Le Boxer est proposé à l’exportation avec différentes configurations d’équipement et de système d’arme.
                Ils remplacent une partie du parc des véhicules M113 et Fuchs TPz 1.
                Le Boxer est mis en service par la Lituanie sous l’appellation Vilkas,
                par l’Australie avec la production sous licence et par le Royaume-Uni.
                Mitrailleuse : 7,62 mm / 12,7 mm. / LGA : 40mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco42.jpg",
            additionalImages = listOf("reco14_extra1.jpg", "reco14_extra2.jpg", "reco14_extra3.jpg", "reco14_extra4.jpg"),
            correct = "BMR600",
            options = listOf("Boxer", "Pizarro", "Stryker", "BMR600"),
            description = """
                Le BMR-600 est un véhicule blindé de transport de troupe à roues 6×6 espagnol. Il est développé dans les années 1970.
                Le BMR-600, Blindado Medio sobre Ruedas – blindé moyen à roues, est développé pour équiper les unités d’infanterie de l’armée espagnole.
                Le véhicule peut accueillir deux membres d’équipage et onze fantassins.
                L’armement est composé d’une mitrailleuse lourde de 12,7 mm.
                Près de 1 500 BMR-600 ont été produits en différentes versions. Il a été exporté en Egypte, Arabie saoudite et Pérou.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco43.jpg",
            additionalImages = listOf("reco17_extra1.jpg", "reco17_extra2.jpg", "reco17_extra3.jpg", "reco17_extra4.jpg"),
            correct = "XA-180",
            options = listOf("XA-180", "Boxer", "Freccia", "BMR600"),
            description = """
                Le XA-180 est un véhicule blindé de transport de troupe à roues 6×6 finlandais. Il a été développé dans les années 1980.
                Le XA-180 est entré en service en 1984.
                Le XA-180 est connu également sous le nom Pasi, Panssari Sisu – Blindé Sisu en finlandais.
                Il est développé à la demande de l’armée finlandaise pour remplacer les véhicules blindés de transport de troupe BTR-60 soviétiques.
                Le XA-180 est développé en plusieurs versions et exporté aux Pays-Bas, Norvège, Suède, Irlande et Estonie.
                Armement: 12,7 mm ou 7,62 mm.
                Equipage : 2 + 10 à 16.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco44.jpg",
            additionalImages = listOf("reco16_extra1.jpg", "reco16_extra2.jpg", "reco16_extra3.jpg", "reco16_extra4.jpg"),
            correct = "Freccia",
            options = listOf("Boxer", "BMR600", "Freccia", "VBCI"),
            description = """
                Le VBM Freccia, Veicolo Blindato Medio, est un véhicule blindé de combat d’infanterie à roues 8×8 italien. Il est développé dans les années 2000.
                Le Freccia est entré en service en 2008. Le châssis du Freccia est développé à partir du châssis du char léger italien B1 Centauro.
                Le Freccia est équipé d’une tourelle biplace HITFIST 25 Plus armée d’un canon Oerlikon KBA de 25 mm.
                L’armement est complété par deux mitrailleuses de 7,62 mm et la capacité de recevoir les missiles antichars Spike.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco45.jpg",
            additionalImages = listOf("reco36_extra1.jpg", "reco36_extra2.jpg", "reco36_extra3.jpg", "reco36_extra4.jpg"),
            correct = "Bradley",
            options = listOf("Bradley", "M-113", "Warrior", "Marder"),
            description = """
                Le M2 Bradley est un véhicule de combat d’infanterie américain. Il est développé dans les années 1970.
                Le M2 Bradley est entré en service en 1981.
                Le M2 Bradley est armé d’un canon Bushmaster de 25 mm.
                A partir de 1986, le modèle M1A1 reçoit deux missiles antichar TOW.
                Le M2 Bradley a suivi plusieurs campagnes de modernisation améliorant ses capacités au combat, son niveau de protection,
                sa mobilité et divers équipements.
                Le modèle M2A3 intègre l’équipement de Numérisation de l’Espace de Bataille.
                A partir du M2 Bradley, une version de reconnaissance est développée sous le nom M3 Bradley.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco46.jpg",
            additionalImages = listOf("reco36_extra1.jpg", "reco36_extra2.jpg", "reco36_extra3.jpg", "reco36_extra4.jpg"),
            correct = "Bradley",
            options = listOf("Bradley", "M-113", "Warrior", "Marder"),
            description = """
                Le M2 Bradley est un véhicule de combat d’infanterie américain. Il est développé dans les années 1970.
                Le M2 Bradley est entré en service en 1981.
                Le M2 Bradley est armé d’un canon Bushmaster de 25 mm.
                A partir de 1986, le modèle M1A1 reçoit deux missiles antichar TOW.
                Le M2 Bradley a suivi plusieurs campagnes de modernisation améliorant ses capacités au combat, son niveau de protection,
                sa mobilité et divers équipements.
                Le modèle M2A3 intègre l’équipement de Numérisation de l’Espace de Bataille.
                A partir du M2 Bradley, une version de reconnaissance est développée sous le nom M3 Bradley.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco47.jpg",
            additionalImages = listOf("reco38_extra1.jpg", "reco38_extra2.jpg", "reco38_extra3.jpg", "reco38_extra4.jpg"),
            correct = "Puma",
            options = listOf("Stryker", "Warrior", "Bradley", "Puma"),
            description = """
                Le SPz Puma est un véhicule de combat d’infanterie lourd allemand. Il est développé dans les années 2000.
                Le SPz Puma est développé pour remplacer les SPz Marder au sein des unités d’infanterie mécanisée allemandes.
                Le SPz Puma est équipé d’une tourelle téléopérée armée d’un canon MK.30-2 de 30 mm est d’une mitrailleuse coaxiale de 5.56 mm.
                Il possède deux niveaux de protection. Le niveau A, Aérotransportable, avec la protection anti-mine complète et la protection balistique
                contre la munition de 30 mm de face et de 14,5 mm en latéral.
                La version SPz Puma niveau A reste aérotransportable par avion de transport A-400M.
                Equipage : 3 + 6.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco48.jpg",
            additionalImages = listOf("reco23_extra1.jpg", "reco23_extra2.jpg", "reco23_extra3.jpg", "reco23_extra4.jpg"),
            correct = "PTL-02",
            options = listOf("MLVM", "PTL-02", "Rosomak", "TAB-71"),
            description = """
                Le PTL-02 est un véhicule à armement lourd chinois. Il est développé dans les années 1990 – début 2000.
                Le PTL-02 est entré en service en 2012.
                Le PTL-02 est développé pour doter les unités d’infanterie d’un appui-feu.
                Il emploie le châssis renforcé du véhicule blindé de transport de troupe à roues 6×6 WZ-551.
                L’armement principal est composé du canon de 100 mm. La dotation en munition est de 30 obus.
                Equipage : 4 + 1.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco49.jpg",
            additionalImages = listOf("reco9_extra1.jpg", "reco9_extra2.jpg", "reco9_extra3.jpg", "reco9_extra4.jpg"),
            correct = "Fennek",
            options = listOf("Rosomak", "Fennek", "URO Vamtac", "Pandur-1"),
            description = """
                Le Fennek est un véhicule blindé à roues 4×4 allemand. Il a été développé dans les années 1990.
                Le Fennek est entré en service en 2003.
                Le Fennek possède le poste de pilotage au centre à l’avant du véhicule et deux membres de l’équipage sont placés côte à côte dans la partie centrale.
                Le groupe motopropulseur est placé à l’arrière du véhicule.
                La version reconnaissance est équipée d’un module optique escamotable. Différents modèles sont développés pour les unités de reconnaissance,
                antichar, génie, défense sol-air, observation artillerie.
                Armement: 7,62 mm / 12,7 mm / Lance-grenade automatique de 40 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco50.jpg",
            additionalImages = listOf("reco12_extra1.jpg", "reco12_extra2.jpg", "reco12_extra3.jpg", "reco12_extra4.jpg"),
            correct = "Fuchs",
            options = listOf("Pandur-1", "Fennek", "XA-180", "Fuchs"),
            description = """
                Le TPz1 Fuchs est un véhicule blindé de transport de troupe à roues 6×6 allemand. Il est développé à la fin des années 1970.
                Armement: Mitrailleuse 7.62 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco51.jpg",
            additionalImages = listOf("reco30_extra1.jpg", "reco30_extra2.jpg", "reco30_extra3.jpg", "reco30_extra4.jpg"),
            correct = "ZTD-05",
            options = listOf("BMD-2", "BMP-2", "ZBD-05", "ZTD-05"),
            description = """
                Le ZTD-05 est un Véhicule à Armement Lourd chinois. Il a été développé à la fin des années 1990.
                Le ZTD-05 est entré en service en 2005.
                Le ZTD-05 utilise le châssis du véhicule de combat d’infanterie amphibie chinois ZBD-05, appelé également Type-05.
                La famille des véhicules ZBD-ZTD développée pour les unités de l’infanterie de marine est optimisée pour les missions de débarquement.
                Il est équipé d’une tourelle biplace armée d’un canon stabilisé de 105 mm. Il est capable de tirer en mouvement sur terre et sur l’eau.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco52.jpg",
            additionalImages = listOf("reco52_extra1.jpg", "reco52_extra2.jpg", "reco52_extra3.jpg", "reco52_extra4.jpg"),
            correct = "Pandur-1",
            options = listOf("Pandur-1", "Pizarro", "Marder", "Rosomak"),
            description = """
                Le Pandur 1 est un véhicule blindé à roues transport de troupe, construit par la firme autrichienne.
                Le véhicule est armé avec une mitrailleuse de 7,62 mm ou de 12,7 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco53.jpg",
            additionalImages = listOf("reco24_extra1.jpg", "reco24_extra2.jpg", "reco24_extra3.jpg", "reco24_extra4.jpg"),
            correct = "Centauro",
            options = listOf("Freccia", "Centauro", "Pizarro", "Piranha V"),
            description = """
                Le B1 Centauro est un char léger à roues 8×8 italien. Il est développé dans les années 1980 par le consortium Iveco – Fiat.
                Le B1 Centauro est entré en service en 1991.
                Le B1 Centauro est destiné aux unités de reconnaissance.
                L’arrière du châssis possède une porte pour faciliter l’accès à bord et le chargement des munitions.
                En plus de son équipage, quatre fantassins peuvent prendre place à l’arrière du véhicule sur des sièges rabattables,
                à la place d’une partie du stock de munition. Le B1 Centauro est armé d’un canon de 105 mm stabilisé, à chargement manuel.
                Le châssis du B1 Centauro a donné naissance au véhicule d’infanterie VBM Freccia.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco54.jpg",
            additionalImages = listOf("reco21_extra1.jpg", "reco21_extra2.jpg", "reco21_extra3.jpg", "reco21_extra4.jpg"),
            correct = "ULAN-30",
            options = listOf("Bradley", "Warrior", "ULAN-30", "Marder"),
            description = """
                Le Ulan est un véhicule blindé de combat d’infanterie autrichien. Il a été développé au début des années 1990.
                Le Ulan est entré en service en 2001.
                Canon : 30 mm, Mauser MK 30/2.
                Mitrailleuse coaxiale : 7,62 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco55.jpg",
            additionalImages = listOf("reco9_extra1.jpg", "reco9_extra2.jpg", "reco9_extra3.jpg", "reco9_extra4.jpg"),
            correct = "Fennek",
            options = listOf("Fennek", "Pandur", "Fuchs", "Rosomak"),
            description = """
                Le Fennek est un véhicule blindé à roues 4×4 allemand. Il a été développé dans les années 1990.
                Le Fennek est entré en service en 2003.
                Le Fennek possède le poste de pilotage au centre à l’avant du véhicule et deux membres de l’équipage sont placés côte à côte dans la partie centrale.
                Le groupe motopropulseur est placé à l’arrière du véhicule.
                La version reconnaissance est équipée d’un module optique escamotable. Différents modèles sont développés pour les unités de reconnaissance,
                antichar, génie, défense sol-air, observation artillerie.
                Armement: 7,62 mm / 12,7 mm / Lance-grenade automatique de 40 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco56.jpg",
            additionalImages = listOf("reco15_extra1.jpg", "reco15_extra2.jpg", "reco15_extra3.jpg", "reco15_extra4.jpg"),
            correct = "Wiesel",
            options = listOf("VHM", "Wiesel", "Piranha", "BMR-600"),
            description = """
                Le Wiesel est un véhicule blindé de reconnaissance allemand. Il est développé dans les années 1980.
                Le Wiesel est entré en service en 1989.
                Le Wiesel est développé en deux principales versions :
                le Wiesel 1 Mk 20 armé d’un canon automatique a double alimentation Mk 20 Rh 202 de 20 mm destiné aux missions de reconnaissance et d’appui-feu
                et le Wiesel 1 TOW destiné au combat antichar.
                Equipage : 2 – Wiesel Mk20 / 3 – Wiesel TOW.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco57.jpg",
            additionalImages = listOf("reco40_extra1.jpg", "reco40_extra2.jpg", "reco40_extra3.jpg", "reco40_extra4.jpg"),
            correct = "M-113",
            options = listOf("Bradley", "M-113", "Marder", "Cobra"),
            description = """
                Le M113 est un véhicule blindé de transport de troupe américain. Il est développé dans les années 1950.
                Le M113 est entré en service en 1960.
                Le M113 est développé pour les unités d’infanterie pour des missions de transport d’un groupe de fantassin.
                Il a suivi plusieurs campagnes de modernisation.
                Le M113 est dérivé en une large gamme de véhicules spécialisés : poste de commandement, mortier, sanitaire, défense sol-air,
                véhicule du génie, lance-missile antichar, etc. Il a été adopté par de très nombreuses forces armées dans le monde.
                Mitrailleuse : 12.7 mm.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco58.jpg",
            additionalImages = listOf("reco13_extra1.jpg", "reco13_extra2.jpg", "reco13_extra3.jpg"),
            correct = "Piranha V",
            options = listOf("Piranha V", "Boumerang", "VBCI", "Boxer"),
            description = """
                Le véhicule de combat d'infanterie Piranha V (VCI) est la variante de cinquième génération de la famille de véhicules Mowag Piranha.
                Lieu d’origine : Suisse.
                Le Piranha V est entré en service en 2015.
                Utilisé par: Danemark / Monaco / Espagne / Roumanie.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco59.jpg",
            additionalImages = listOf("reco40_extra1.jpg", "reco40_extra2.jpg", "reco40_extra3.jpg", "reco40_extra4.jpg"),
            correct = "M-113",
            options = listOf("Bradley", "M-113", "Marder", "Cobra"),
            description = """
                Le M113 est un véhicule blindé de transport de troupe américain. Il est développé dans les années 1950.
                Le M113 est entré en service en 1960.
                Le M113 est développé pour les unités d’infanterie pour des missions de transport d’un groupe de fantassin.
                Il a suivi plusieurs campagnes de modernisation.
                Le M113 est dérivé en une large gamme de véhicules spécialisés : poste de commandement, mortier, sanitaire, défense sol-air,
                véhicule du génie, lance-missile antichar, etc. Il a été adopté par de très nombreuses forces armées dans le monde.
                Mitrailleuse : 12.7 mm.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco60.jpg",
            additionalImages = listOf("reco22_extra1.jpg", "reco22_extra2.jpg", "reco22_extra3.jpg", "reco22_extra4.jpg"),
            correct = "Pizarro",
            options = listOf("Piranha V", "Pandur", "Bradley", "Pizarro"),
            description = """
                Le Pizarro est un véhicule blindé de combat d’infanterie espagnol. Il est développé au début des années 1990.
                Le Pizarro est entré en service en 2001.
                Le Pizarro est développé dans le cadre du programme ASCOD, Austrian Spanish Cooperation Development,
                du futur véhicule blindé de combat d’infanterie pour les forces armées autrichiennes et espagnoles.
                La version autrichienne, baptisée Ulan, possède plusieurs différences.
                Canon : 30 mm.
                Mitrailleuse coaxiale : 7,62 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco61.jpg",
            additionalImages = listOf("reco29_extra1.jpg", "reco29_extra2.jpg", "reco29_extra3.jpg", "reco29_extra4.jpg"),
            correct = "Jackal",
            options = listOf("Coyote", "Grizzly", "Jackal", "Cobra"),
            description = """
                L’HMT 400 est un véhicule blindé modulaire.
                Il est développé par Supacat à partir du châssis ATMP en version 4×4 HMT 400, 6×6 HMT 600 et 6×6 HMT Extenda Mk2.
                Le Jackal transporte trois membres d’équipage et peut être employé pour des missions de reconnaissance.
                Il est doté d’un important niveau de protection anti-mines et peut recevoir un kit de surblindage.
                L’armement est composé d’une mitrailleuse lourde de 12,7 mm ou d’un lance-grenade automatique et d’une mitrailleuse de 7,62 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco62.jpg",
            additionalImages = listOf("reco19_extra1.jpg", "reco19_extra2.jpg", "reco19_extra3.jpg", "reco19_extra4.jpg"),
            correct = "Pandur-2",
            options = listOf("VBCI", "Pandur-2", "Piranha", "Boxer"),
            description = """
                Pandur II: véhicule blindé de transport de troupes originaire d'Autriche.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco63.jpg",
            additionalImages = listOf("reco33_extra1.jpg", "reco33_extra2.jpg", "reco33_extra3.jpg", "reco33_extra4.jpg"),
            correct = "BMO-T",
            options = listOf("BMO-T", "M-113", "BMP-Terminator", "TAB-71"),
            description = """
                Le BMO-T est un véhicule blindé de transport de troupe lourd russe.
                Il est développé dans les années 1990 à partir du châssis du char de combat T-72.
                Le BMO-T est entré en service en 2001 en quantité restreinte.
                La Russie est l’unique utilisatrice des BMO-T.
                Mitrailleuse : 12.7 mm.
                Lance-roquettes : 30 PRO-A Shmel à effets thermobarique.
                Equipage : 2 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco64.jpg",
            additionalImages = listOf("reco64_extra1.jpg", "reco64_extra2.jpg", "reco64_extra3.jpg", "reco64_extra4.jpg"),
            correct = "ZBD-05",
            options = listOf("BMD-2", "BMP-2", "ZBD-05", "ZTD-05"),
            description = """
                Le ZBD-05 est un véhicule de combat d’infanterie amphibie chinois. Il est développé dans les années 2000.
                Le ZBD-05 est entré en service en 2005.
                Le ZBD-05 est développé spécialement pour les unités des fusiliers-marins. Le véhicule a été dévoilé en 2006 sous l’appellation ZBD-2000.
                Pour améliorer ses qualités amphibies, le ZBD-05 est équipé d’un important pare-lame avant.
                Le ZBD-05 est équipé d’une tourelle biplace armée du canon automatique ZPT-99 de 30 mm, d’une mitrailleuse coaxiale de 7,62 mm et de missiles antichars HJ-73C.
                Le véhicule accueille trois membres d’équipage et neuf fantassins équipés.
                Plusieurs véhicules sont développés sur le châssis du ZTD-05.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco65.jpg",
            additionalImages = listOf("reco65_extra1.jpg", "reco65_extra2.jpg", "reco65_extra3.jpg", "reco65_extra4.jpg"),
            correct = "BMD-1",
            options = listOf("BMD-1", "BMD-2", "ZBD-05", "ZTD-05"),
            description = """
                Le BMD-1 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes.
                Il a été développé en Union soviétique au début des années 1960.
                Le BMD-1 est entré en service en 1969.
                La tourelle monoplace est armée d’un canon 2A28 Grom de 73 mm, d’une mitrailleuse coaxiale PKT de 7,62 mm.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco66.jpg",
            additionalImages = listOf("reco66_extra1.jpg", "reco66_extra2.jpg", "reco66_extra3.jpg", "reco66_extra4.jpg"),
            correct = "BMD-2",
            options = listOf("BMD-1", "BMD-2", "ZBD-05", "ZTD-05"),
            description = """
                Le BMD-2 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes.
                Il a été développé en Union soviétique au début des années 1980.
                Le BMD-2 est développé à partir du châssis du BMD-1.
                Il possède le nouveau système d’arme avec une tourelle monoplace armée du canon 2A42 de 30 mm et
                d’une mitrailleuse coaxiale PKT de 7,62 mm.
                Pour la lutte antichar, il est doté d’un système apte à tirer les missiles antichars 9M111 Fagot AT-4 ou 9M113 Konkurs AT-5.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco67.jpg",
            additionalImages = listOf("reco67_extra1.jpg", "reco67_extra2.jpg", "reco67_extra3.jpg", "reco67_extra4.jpg"),
            correct = "BMD-3",
            options = listOf("BMD-1", "BMD-2", "BMD-3", "BMD-4"),
            description = """
                Le BMD-3 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes.
                Il a été développé en Union soviétique dans les années 1980.
                Le BMD-3 est entré en service en 1990.
                Dans les années 1980, l’URSS cherche à développer un nouveau véhicule blindé de combat d’infanterie parachutable pour remplacer la famille des BMD-1 et BMD-2.
                Le BMD-3 est parachutable.
                Canon : 30 mm 2A42 x100.2 calibres.
                Mitrailleuse coaxiale : 7.62 mm PKT.
                Lance-grenade automatique de capot : 30 mm AG-17.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco68.jpg",
            additionalImages = listOf("reco68_extra1.jpg", "reco68_extra2.jpg", "reco68_extra3.jpg", "reco68_extra4.jpg"),
            correct = "BMD-4",
            options = listOf("BMP-2", "BMP-3", "BMD-3", "BMD-4"),
            description = """
                Le BMD-4 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes. Il est développé au début des années 2000.
                Le BMD-4 est entré en service en 2004 en quantité restreinte.
                Le BMD-4 possède le même châssis à cinq galets que son prédécesseur le BMD-3.
                Le véhicule reçoit une nouvelle tourelle Bakhtcha-U qui permet d’augmenter significativement la puissance de feu.
                La tourelle Bakhtcha-U est dérivée du système d’arme du BMP-3. Elle est équipée d’un canon 2A70 de 100 mm,
                d’un canon automatique 2A72 de 30 mm et d’une mitrailleuse coaxiale PKT de 7,62 mm.
                Le BMD-4 est parachutable et optimisé pour les opérations aéroportées.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco69.jpg",
            additionalImages = listOf("reco69_extra1.jpg", "reco69_extra2.jpg", "reco69_extra3.jpg", "reco69_extra4.jpg"),
            correct = "BMP-1",
            options = listOf("BMD-1", "BMP-1", "BMP-2", "BMD-3"),
            description = """
                Le BMP-1 est un véhicule blindé de combat d’infanterie soviétique. Il a été développé dans les années 1960.
                Le BMP-1 est entré en service en 1966.
                Il est considéré comme l’un des premiers véhicules de combat d’infanterie modernes, combinant mobilité, protection et puissance de feu.
                Le BMP-1 est équipé d’une tourelle monoplace armée d’un canon 2A28 Grom de 73 mm et d’une mitrailleuse coaxiale PKT de 7,62 mm.
                Il peut également tirer des missiles antichars 9M14 Malyutka (AT-3 Sagger).
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco69.jpg",
            additionalImages = listOf("reco69_extra1.jpg", "reco69_extra2.jpg", "reco69_extra3.jpg", "reco69_extra4.jpg"),
            correct = "BMP-1",
            options = listOf("BMD-1", "BMD-2", "BMP-1", "BMP-2"),
            description = """
                Le BMP-1 est un véhicule blindé de combat d’infanterie russe. Il a été développé en Union soviétique au début des années 1960.
                Le BMP-1 est entré en service en 1966.
                Le BMP-1, Boyevaya Machina Pekhoty – véhicule de combat d’infanterie, est le premier véhicule blindé de sa catégorie.
                Il est développé pour renforcer les capacités des unités d’infanterie mécanisées.
                Le BMP-1 est un véhicule amphibie et apte à combattre dans l’ambiance NRBC.
                Son équipage est composé du chef d’engin, du tireur, du pilote et de huit fantassins.
                Le BMP-1 est doté d’une tourelle monoplace armée d’un canon 2A28 Grom à âme lisse de 73 mm,
                d’une mitrailleuse coaxiale PKTM de 7,62 mm et du missile antichars 9M14M Malyutka, AT-3 Sagger.
                Les fantassins ont la capacité de tirer depuis l’intérieur du véhicule par neuf trappes de tir.
                Entre 1966 et 1988, plus de 20 000 BMP-1 ont été construits.
                En août 2018, une nouvelle version du BMP-1 est présentée sous le nom BMP-1AM Basurman.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco70.jpg",
            additionalImages = listOf("reco69_extra1.jpg", "reco69_extra2.jpg", "reco69_extra3.jpg", "reco69_extra4.jpg"),
            correct = "BMP-1",
            options = listOf("BMD-1", "BMD-2", "BMP-1", "BMP-2"),
            description = """
                Le BMP-1 est un véhicule blindé de combat d’infanterie russe. Il a été développé en Union soviétique au début des années 1960.
                Le BMP-1 est entré en service en 1966.
                Le BMP-1, Boyevaya Machina Pekhoty – véhicule de combat d’infanterie, est le premier véhicule blindé de sa catégorie.
                Il est développé pour renforcer les capacités des unités d’infanterie mécanisées.
                Le BMP-1 est un véhicule amphibie et apte à combattre dans l’ambiance NRBC.
                Son équipage est composé du chef d’engin, du tireur, du pilote et de huit fantassins.
                Le BMP-1 est doté d’une tourelle monoplace armée d’un canon 2A28 Grom à âme lisse de 73 mm,
                d’une mitrailleuse coaxiale PKTM de 7,62 mm et du missile antichars 9M14M Malyutka, AT-3 Sagger.
                Les fantassins ont la capacité de tirer depuis l’intérieur du véhicule par neuf trappes de tir.
                Entre 1966 et 1988, plus de 20 000 BMP-1 ont été construits.
                En août 2018, une nouvelle version du BMP-1 est présentée sous le nom BMP-1AM Basurman.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco71.jpg",
            additionalImages = listOf("reco71_extra1.jpg", "reco71_extra2.jpg", "reco71_extra3.jpg", "reco71_extra4.jpg"),
            correct = "BMP-2",
            options = listOf("BMD-1", "BMD-2", "BMP-1", "BMP-2"),
            description = """
                Le BMP-2 est un véhicule de combat d’infanterie russe. Il est développé en Union soviétique dans les années 1970.
                Le BMP-2 est entré en service en 1981.
                Le BMP-2 est développé sur la base du châssis du véhicule blindé de combat d’infanterie BMP-1.
                Le groupe motopropulseur est à l’avant droit et le poste du pilote à l’avant gauche. Il est amphibie et protégé NBC.
                Pour améliorer ses capacités de feu, le BMP-2 reçoit une tourelle biplace, armée d’un canon automatique 2A42 de 30 mm,
                une mitrailleuse coaxiale PKT de 7,62 mm et les missiles antichars.
                Le BMP-2 est adopté par de nombreuses armées dans le monde.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco72.jpg",
            additionalImages = listOf("reco71_extra1.jpg", "reco71_extra2.jpg", "reco71_extra3.jpg", "reco71_extra4.jpg"),
            correct = "BMP-2",
            options = listOf("ZTD-05", "BMP-2", "Marder", "ULAN-30"),
            description = """
                Le BMP-2 est un véhicule de combat d’infanterie russe. Il est développé en Union soviétique dans les années 1970.
                Le BMP-2 est entré en service en 1981.
                Le BMP-2 est développé sur la base du châssis du véhicule blindé de combat d’infanterie BMP-1.
                Le groupe motopropulseur est à l’avant droit et le poste du pilote à l’avant gauche. Il est amphibie et protégé NBC.
                Pour améliorer ses capacités de feu, le BMP-2 reçoit une tourelle biplace, armée d’un canon automatique 2A42 de 30 mm,
                une mitrailleuse coaxiale PKT de 7,62 mm et les missiles antichars.
                Le BMP-2 est adopté par de nombreuses armées dans le monde.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco73.jpg",
            additionalImages = listOf("reco73_extra1.jpg", "reco73_extra2.jpg", "reco73_extra3.jpg", "reco73_extra4.jpg"),
            correct = "BMP-3",
            options = listOf("BMP-2", "BMP-3", "BMD-3", "BMD-4"),
            description = """
                Le BMP-3 est un véhicule de combat d’infanterie russe. Il est développé en Union soviétique dans les années 1980.
                Le BMP-3 est entré en service en 1987.
                Le BMP-3 est le véhicule le plus fortement armé de sa catégorie. Son armement est composé du canon à basse pression 2A70 de 100 mm,
                du canon automatique 2A72 de 30 mm, de la mitrailleuse coaxiale PKT de 7.62 mm.
                Le BMP-3 peut tirer par le canon de 100 mm le missile antichar 9M117 du système 9K166-3 Basnya, AT-12 Swinger.
                Le châssis du BMP-3 adopte la configuration de la famille des véhicules blindé combat des unités de parachutistes russe BMD.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco74.jpg",
            additionalImages = listOf("reco_extra1.jpg", "reco_extra2.jpg", "reco_extra3.jpg", "reco_extra4.jpg"),
            correct = "BRDM-2",
            options = listOf("BTR-60", "BRDM-2", "VAB", "ZTD-05"),
            description = """
                Le BRDM-2 est un véhicule blindé de reconnaissance russe. Il a été développé en Union soviétique au début des années 1950.
                Le BRDM-2 est entré en service en 1962.
                Son armement est composé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm,
                identiques au véhicules blindés de transport de troupe russe BTR-60/BTR-70/BTR-80.
                Le BRDM-2, en différents versions, est utilisé par les forces armées de plus de 50 pays dans le monde.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco75.jpg",
            additionalImages = listOf("reco_extra1.jpg", "reco_extra2.jpg", "reco_extra3.jpg", "reco_extra4.jpg"),
            correct = "BRDM-2",
            options = listOf("BTR-60", "BRDM-2", "VAB", "ZTD-05"),
            description = """
                Le BRDM-2 est un véhicule blindé de reconnaissance russe. Il a été développé en Union soviétique au début des années 1950.
                Le BRDM-2 est entré en service en 1962.
                Son armement est composé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm,
                identiques au véhicules blindés de transport de troupe russe BTR-60/BTR-70/BTR-80.
                Le BRDM-2, en différents versions, est utilisé par les forces armées de plus de 50 pays dans le monde.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco76.jpg",
            additionalImages = listOf("reco76_extra1.jpg", "reco76_extra2.jpg", "reco76_extra3.jpg", "reco76_extra4.jpg"),
            correct = "BTR-60",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-60 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1950.
                Le BTR-60 est entré en service en 1960.
                La version BTR-60P au toit ouvert transporte deux membres d’équipage et 12 à 14 fantassins.
                L’armement est composé d’une mitrailleuse de 12,7 mm actionnée manuellement et d’une ou plusieurs mitrailleuses de 7,62 mm installées sur les flancs.
                La version BTR-60PA reçoit un toit et bénéficie de la protection NBC.
                La version BTR-60PB est équipée d’un tourelleau monoplace armé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm.
                Le BTR-60PB transporte trois membres d’équipage et 8 fantassins.
                Le BTR-60, en différentes versions, est très largement exporté dans le monde. Au sein de l’armée soviétique, puis russe, le BTR-60 est remplacé à partir de 1976 par le BTR-70.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco77.jpg",
            additionalImages = listOf("reco76_extra1.jpg", "reco76_extra2.jpg", "reco76_extra3.jpg", "reco76_extra4.jpg"),
            correct = "BTR-60",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-60 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1950.
                Le BTR-60 est entré en service en 1960.
                La version BTR-60P au toit ouvert transporte deux membres d’équipage et 12 à 14 fantassins.
                L’armement est composé d’une mitrailleuse de 12,7 mm actionnée manuellement et d’une ou plusieurs mitrailleuses de 7,62 mm installées sur les flancs.
                La version BTR-60PA reçoit un toit et bénéficie de la protection NBC.
                La version BTR-60PB est équipée d’un tourelleau monoplace armé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm.
                Le BTR-60PB transporte trois membres d’équipage et 8 fantassins.
                Le BTR-60, en différentes versions, est très largement exporté dans le monde. Au sein de l’armée soviétique, puis russe, le BTR-60 est remplacé à partir de 1976 par le BTR-70.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco78.jpg",
            additionalImages = listOf("reco78_extra1.jpg", "reco78_extra2.jpg", "reco78_extra3.jpg", "reco78_extra4.jpg"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1980.
                Le BTR-80 est entré en service en 1986.
                Le BTR-80 possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule.
                L’accès à bord est effectué par les portes latérales doubles.
                Mitrailleuse : 14.5 mm.
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco79.jpg",
            additionalImages = listOf("reco79_extra1.jpg", "reco79_extra2.jpg", "reco79_extra3.jpg", "reco79_extra4.jpg"),
            correct = "BTR-70",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-70 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1970.
                Le BTR-70 est entré en service en 1976.
                Le BTR-70 possède la même architecture que son prédécesseur le BTR-60.
                La version BTR-70 est équipée d’un tourelleau monoplace armé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco80.jpg",
            additionalImages = listOf("reco78_extra1.jpg", "reco78_extra2.jpg", "reco78_extra3.jpg", "reco78_extra4.jpg"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1980.
                Le BTR-80 est entré en service en 1986.
                Le BTR-80 possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule.
                L’accès à bord est effectué par les portes latérales doubles.
                Mitrailleuse : 14.5 mm.
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco81.jpg",
            additionalImages = listOf("reco78_extra1.jpg", "reco78_extra2.jpg", "reco78_extra3.jpg", "reco78_extra4.jpg"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1980.
                Le BTR-80 est entré en service en 1986.
                Le BTR-80 possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule.
                L’accès à bord est effectué par les portes latérales doubles.
                Mitrailleuse : 14.5 mm.
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco82.jpg",
            additionalImages = listOf("reco82_extra1.jpg", "reco82_extra2.jpg", "reco82_extra3.jpg", "reco82_extra4.jpg"),
            correct = "BTR-80A",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le transport de troupes blindé BTR-80A 8x8 est conçu et fabriqué par Russie.
                Il est destiné au transport de troupes sur le champ de bataille et à fournir un appui-feu rapproché.
                Il peut également mener des missions de reconnaissance, de soutien au combat et de patrouille.
                Les cinq premiers véhicules BTR-80A ont été livrés à la Direction principale de la sécurité présidentielle, basée à Moscou,
                et ont été présentés publiquement pour la première fois en mai 1995.
                Le BTR-80A est pratiquement identique au BTR-80, mais il est équipé d'une nouvelle tourelle monoplace armée d'un canon de 30 mm 2A72
                et d'une mitrailleuse PKTM de 7,62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco83.jpg",
            additionalImages = listOf("reco78_extra1.jpg", "reco78_extra2.jpg", "reco78_extra3.jpg", "reco78_extra4.jpg"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1980.
                Le BTR-80 est entré en service en 1986.
                Le BTR-80 possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule.
                L’accès à bord est effectué par les portes latérales doubles.
                Mitrailleuse : 14.5 mm.
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco84.jpg",
            additionalImages = listOf("reco84_extra1.jpg", "reco84_extra2.jpg", "reco84_extra3.jpg", "reco84_extra4.jpg"),
            correct = "JLTV Falcon",
            options = listOf("JLTV Falcon", "Dingo", "Grizzly", "Eagle V"),
            description = """
                Le JLTV, Joint Light Tactical Vehicle, est un véhicule blindé à roues 4×4 américain. Il a été développé dans les années 2010,
                dans le cadre du programme de remplacement du HMMWV. Le JLTV est entré en service en 2018.
                Le JLTV est développé à partir du véhicule blindé à roues L-ATV, Light Combat Tactical All-Terrain Vehicle.
                Il possède le niveau de protection balistique et la protection anti-mines du niveau d’un MRAP, Mine Resistant Ambush Protected.
                Le véhicule peut recevoir différents types d’armement avec des tourelleaux téléopérés.
                Le JLTV est activement promu à l’exportation.
                Armement: 12,7 mm à 20 mm.
                Equipage : 2 + 2 à 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco85.jpg",
            additionalImages = listOf("reco85_extra1.jpg", "reco85_extra2.jpg", "reco85_extra3.jpg", "reco85_extra4.jpg"),
            correct = "Caiman",
            options = listOf("JLTV Falcon", "Caiman", "Casspir", "Freccia"),
            description = """
                Un blindé de transport de troupes (BTT) de type MRAP avec un fond en forme de V.
                Pays : États-Unis. Période d'utilisation : 2007 à aujourd'hui. Équipage : jusqu'à 10 personnes.
                Capacité d'installation de tous les modules de combat connus.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco86.jpg",
            additionalImages = listOf("reco86_extra1.jpg", "reco86_extra2.jpg", "reco86_extra3.jpg", "reco86_extra4.jpg"),
            correct = "Casspir",
            options = listOf("JLTV Falcon", "Caiman", "Casspir", "Ajax"),
            description = """
                Le Casspir est un véhicule de transport de troupes créé pour la police et les forces armées sud-africaines dans les années 1970.
                Il se distingue par un large empattement et un plancher renforcé pour résister aux mines.
                Celui-ci dispose d’une mitrailleuse de 7,62 mm montée sur le toit, mais l’armement est souvent considérablement augmenté sur le terrain par les unités utilisant les véhicules.
                Afin de protéger le véhicule contre les mines, la partie inférieure de la caisse est surélevée, renforcée et en forme de V.
                Équipage: 2 (+10 passagers).
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco87.jpg",
            additionalImages = listOf("reco87_extra1.jpg", "reco87_extra2.jpg", "reco87_extra3.jpg", "reco87_extra4.jpg"),
            correct = "PVP",
            options = listOf("URO", "PVP", "Eagle V", "Komatsu LAV"),
            description = """
                Le PVP, Petit Véhicule Protégé, est un véhicule léger blindé à roues 4×4 français. Il a été développé dans les années 2000.
                Le PVP est entré en service en 2008.
                Le PVP possède la protection de niveau 2.
                Le PVP est employé par l’armée française et exporté au Chili (15 exemplaires), au Togo (6 exemplaires) et en Roumanie (16 exemplaires).
                Versions: PVP – 3 portes / PVP – 5 portes / PVP HD – 5 portes, allongé.
                Mitrailleuse : 7.62 mm.
                Equipage : 2 + 3 à 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco88.jpg",
            additionalImages = listOf("reco88_extra1.jpg", "reco88_extra2.jpg", "reco88_extra3.jpg", "reco88_extra4.jpg"),
            correct = "URAL Typhoon",
            options = listOf("Griffon", "URAL Typhoon", "URAL-4320", "GAZ 2330 Tigr"),
            description = """
                L’Ural-63095 / Ural-63099 Typhoon-U est un véhicule blindé multirôle à roues 6×6 russe. Il est développé dans les années 2010.
                Le Typhoon-U est entré en service en 2014.
                Le Typhoon-U fait partie des véhicules du type MRAP, Mine Resistant Ambush Protected. Il possède la caisse mono-coque en V et est équipé de sièges absorbant les chocs.
                Ils sont employés dans la région militaire Sud pour le transport de troupe, la reconnaissance, en véhicule poste de commandement, en véhicule NRBC.
                Le véhicule peut recevoir un tourelleau téléopéré équipé d’armements légers.
                Mitrailleuse : 12.7 mm en option.
                Equipage : 3 + 16 Ural-63095 / 2 + 12 Ural-63099.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco89.jpg",
            additionalImages = listOf("reco89_extra1.jpg", "reco89_extra2.jpg", "reco89_extra3.jpg", "reco89_extra4.jpg"),
            correct = "BMP Terminator",
            options = listOf("BMP Terminator", "BMO-T", "Marder", "Pizarro"),
            description = """
                Le BMPT Terminator est un véhicule blindé de combat lourd russe. Il est développé dans les années 2000-2010.
                Le BMPT est entré en service en 2011.
                Le BMPT, Boyévaya Machina Podderjki Tankov – véhicule de combat de soutien de char, est destiné à la mission d’appui-feu des unités de chars de combat par la destruction des moyens anti-char adverses.
                Le BMPT Terminator utilise le châssis du char de combat T-72 ou du T-90 avec le poste de pilotage à l’avant centre et la motorisation à l’arrière.
                Le BMPT possède une large panoplie d’armement. La tourelle biplace en superstructure est dotée de deux canons 2A42 de 30 mm,
                d’une mitrailleuse coaxiale PKT de 7,62 mm et de quatre missiles supersoniques anti-chars à guidage laser Ataka-T.
                Deux lances-grenades automatiques AG-17D sont installés à l’avant-gauche et l’avant-droite du véhicule.
                Les AG-17D sont commandés, indépendamment de l’armement principale, par deux membres d’équipage installés de part et d’autre du pilote.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco90.jpg",
            additionalImages = listOf("reco89_extra1.jpg", "reco89_extra2.jpg", "reco89_extra3.jpg", "reco89_extra4.jpg"),
            correct = "BMP Terminator",
            options = listOf("ULAN-30", "Achzarit", "Namer", "BMP Terminator"),
            description = """
                Le BMPT Terminator est un véhicule blindé de combat lourd russe. Il est développé dans les années 2000-2010.
                Le BMPT est entré en service en 2011.
                Le BMPT, Boyévaya Machina Podderjki Tankov – véhicule de combat de soutien de char, est destiné à la mission d’appui-feu des unités de chars de combat par la destruction des moyens anti-char adverses.
                Le BMPT Terminator utilise le châssis du char de combat T-72 ou du T-90 avec le poste de pilotage à l’avant centre et la motorisation à l’arrière.
                Le BMPT possède une large panoplie d’armement. La tourelle biplace en superstructure est dotée de deux canons 2A42 de 30 mm,
                d’une mitrailleuse coaxiale PKT de 7,62 mm et de quatre missiles supersoniques anti-chars à guidage laser Ataka-T.
                Deux lances-grenades automatiques AG-17D sont installés à l’avant-gauche et l’avant-droite du véhicule.
                Les AG-17D sont commandés, indépendamment de l’armement principale, par deux membres d’équipage installés de part et d’autre du pilote.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco91.jpg",
            additionalImages = listOf("reco89_extra1.jpg", "reco89_extra2.jpg", "reco89_extra3.jpg", "reco89_extra4.jpg"),
            correct = "BMP Terminator",
            options = listOf("ULAN-30", "Marder", "BMP Terminator", "Warrior"),
            description = """
                Le BMPT Terminator est un véhicule blindé de combat lourd russe. Il est développé dans les années 2000-2010.
                Le BMPT est entré en service en 2011.
                Le BMPT, Boyévaya Machina Podderjki Tankov – véhicule de combat de soutien de char, est destiné à la mission d’appui-feu des unités de chars de combat par la destruction des moyens anti-char adverses.
                Le BMPT Terminator utilise le châssis du char de combat T-72 ou du T-90 avec le poste de pilotage à l’avant centre et la motorisation à l’arrière.
                Le BMPT possède une large panoplie d’armement. La tourelle biplace en superstructure est dotée de deux canons 2A42 de 30 mm,
                d’une mitrailleuse coaxiale PKT de 7,62 mm et de quatre missiles supersoniques anti-chars à guidage laser Ataka-T.
                Deux lances-grenades automatiques AG-17D sont installés à l’avant-gauche et l’avant-droite du véhicule.
                Les AG-17D sont commandés, indépendamment de l’armement principale, par deux membres d’équipage installés de part et d’autre du pilote.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco92.jpg",
            additionalImages = listOf("reco71_extra1.jpg", "reco71_extra2.jpg", "reco71_extra3.jpg", "reco71_extra4.jpg"),
            correct = "BMP-2",
            options = listOf("BMD-1", "BMD-2", "BMP-1", "BMP-2"),
            description = """
                Le BMP-2 est un véhicule de combat d’infanterie russe. Il est développé en Union soviétique dans les années 1970.
                Le BMP-2 est entré en service en 1981.
                Le BMP-2 est développé sur la base du châssis du véhicule blindé de combat d’infanterie BMP-1.
                Le groupe motopropulseur est à l’avant droit et le poste du pilote à l’avant gauche. Il est amphibie et protégé NBC.
                Pour améliorer ses capacités de feu, le BMP-2 reçoit une tourelle biplace, armée d’un canon automatique 2A42 de 30 mm,
                une mitrailleuse coaxiale PKT de 7,62 mm et les missiles antichars.
                Le BMP-2 est adopté par de nombreuses armées dans le monde.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco93.jpg",
            additionalImages = listOf("reco93_extra1.jpg", "reco93_extra2.jpg", "reco93_extra3.jpg", "reco93_extra4.jpg"),
            correct = "Boomerang",
            options = listOf("Boomerang", "VBCI", "Stryker", "Freccia"),
            description = """
                Le Boomerang est un projet du véhicule blindé de combat à roues 8×8 russe. Il est en développement à partir des années 2010.
                La mise en service du Boomerang est prévue pour 2019-2020.
                Versions: K-16 Boomerang – VBTT, véhicule blindé de transport de troupe / K-17 Boomerang – VBCI, véhicule blindé de combat d’infanterie.
                Armement du K-16 Boomerang: Mitrailleuse - 12.7 mm.
                Armement du K-17 Boomerang: Mitrailleuse - 30 mm. Missile : 2×2 Kornet.
                Equipage : 3 + 7 à 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco94.jpg",
            additionalImages = listOf("reco93_extra1.jpg", "reco93_extra2.jpg", "reco93_extra3.jpg", "reco93_extra4.jpg"),
            correct = "Boomerang",
            options = listOf("Piranha V", "Boomerang", "VBCI", "Freccia"),
            description = """
                Le Boomerang est un projet du véhicule blindé de combat à roues 8×8 russe. Il est en développement à partir des années 2010.
                La mise en service du Boomerang est prévue pour 2019-2020.
                Versions: K-16 Boomerang – VBTT, véhicule blindé de transport de troupe / K-17 Boomerang – VBCI, véhicule blindé de combat d’infanterie.
                Armement du K-16 Boomerang: Mitrailleuse - 12.7 mm.
                Armement du K-17 Boomerang: Mitrailleuse - 30 mm. Missile : 2×2 Kornet.
                Equipage : 3 + 7 à 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco95.jpg",
            additionalImages = listOf("reco78_extra1.jpg", "reco78_extra2.jpg", "reco78_extra3.jpg", "reco78_extra4.jpg"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe. Il a été développé en Union soviétique au début des années 1980.
                Le BTR-80 est entré en service en 1986.
                Le BTR-80 possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule.
                L’accès à bord est effectué par les portes latérales doubles.
                Mitrailleuse : 14.5 mm.
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco96.jpg",
            additionalImages = listOf("reco96_extra1.jpg", "reco96_extra2.jpg", "reco96_extra3.jpg", "reco96_extra4.jpg"),
            correct = "MTLB",
            options = listOf("MTLB", "Piranha V", "BRDM-2", "Namer"),
            description = """
                Le MT-LB est un véhicule blindé de transport multirôle russe. Il est développé en Union soviétique dans les années 1960.
                Le MT-LB est entré en service en 1964.
                Le MT-LB est conçu comme un tracteur de canon d’artillerie et un transporteur de matériel. Par la suite,
                le MT-LB a été adopté en version transport de troupe, véhicule d’évacuation sanitaire et la plate-forme d’installation de différents systèmes et équipements.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco97.jpg",
            additionalImages = listOf("reco97_extra1.jpg", "reco97_extra2.jpg", "reco97_extra3.jpg", "reco97_extra4.jpg"),
            correct = "VBCI",
            options = listOf("Piranha V", "Boomerang", "VBCI", "Stryker"),
            description = """
                Le VBCI, Véhicule Blindé de Combat d’Infanterie, est un véhicule blindé de combat à roues 8×8 français. Il est développé dans les années 2000.
                Le VBCI est entré en service en 2008.
                Le VBCI est développé pour remplacer le véhicule de combat d’infanterie chenillé AMX-10P.
                Le VBCI est mis en service au sein des unités de l’infanterie de l’Armée française en 2008 en deux versions : VBCI-VCI,
                Véhicule de Combat d’Infanterie, et VBCI-VPC, Véhicule Poste de Commandement.
                La version VBCI-VCI est équipée de la tourelle monoplace Tarask armée du canon automatique M811 de 25 mm à double alimentation et
                de la mitrailleuse coaxiale MAG 58 de 7,62 mm. Elle peut être utilisée par le chef d’engin en fonction télécommandée.
                La version VBCI-VPC est équipée de deux stations SIR et est armée d’une mitrailleuse téléopérée de 12,7 mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco98.jpg",
            additionalImages = listOf("reco98_extra1.jpg", "reco98_extra2.jpg", "reco98_extra3.jpg", "reco98_extra4.jpg"),
            correct = "BTR-4 Bucephale",
            options = listOf("Ural Typhoon", "BTR-80A", "BTR-4 Bucephale", "MTLB"),
            description = """
                Le BTR-4 est un véhicule blindé de combat à roues 8×8 ukrainien. Il est développé dans les années 2000.
                Le BTR-4 est entré en service en 2011 à l’armée irakienne et en 2014 à l’armée ukrainienne.
                L’architecture du BTR-4 diffère de la lignée de la famille des BTR-60/70/80 soviétiques.
                Le BTR-4 est équipée d’une tourelle téléopérée, armée d’un canon de 30 mm.
                Equipage: 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco99.jpg",
            additionalImages = listOf("reco99_extra1.jpg", "reco99_extra2.jpg", "reco99_extra3.jpg", "reco99_extra4.jpg"),
            correct = "Scorpion",
            options = listOf("Scorpion", "Wiesel", "VHM", "Warrior"),
            description = """
                Le FV101 Scorpion est un véhicule blindé de reconnaissance britannique. Il est développé dans les années 1960.
                Le FV101 Scorpion est entré en service en 1973.
                Le FV101 Scorpion est développé comme un véhicule de reconnaissance.
                Il est armé du canon L23A1 de 76 mm et de la mitrailleuse coaxiale L43A1 de 7,62 mm.
                Scorpion a quitté le service actif de l’armée britannique en 1994.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco100.jpg",
            additionalImages = listOf("reco92_extra1.jpg", "reco92_extra2.jpg", "reco92_extra3.jpg", "reco92_extra4.jpg"),
            correct = "BMP-2",
            options = listOf("BMP-1", "BMP-2", "BMD-1", "BMD-2"),
            description = """
                Le BMP-2 est un véhicule de combat d’infanterie russe. Il est développé en Union soviétique dans les années 1970.
                Le BMP-2 est entré en service en 1981.
                Le BMP-2 est développé sur la base du châssis du véhicule blindé de combat d’infanterie BMP-1.
                Le groupe motopropulseur est à l’avant droit et le poste du pilote à l’avant gauche. Il est amphibie et protégé NBC.
                Pour améliorer ses capacités de feu, le BMP-2 reçoit une tourelle biplace, armée d’un canon automatique 2A42 de 30 mm,
                une mitrailleuse coaxiale PKT de 7,62 mm et les missiles antichars.
                Le BMP-2 est adopté par de nombreuses armées dans le monde.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco101.jpg",
            additionalImages = listOf("reco89_extra1.jpg", "reco89_extra2.jpg", "reco89_extra3.jpg", "reco89_extra4.jpg"),
            correct = "BMP-Terminator",
            options = listOf("ULAN-30", "Achzarit", "Namer", "BMP-Terminator"),
            description = """
                Le BMPT Terminator est un véhicule blindé de combat lourd russe. Il est développé dans les années 2000-2010.
                Le BMPT est entré en service en 2011.
                Le BMPT, Boyévaya Machina Podderjki Tankov – véhicule de combat de soutien de char, est destiné à la mission d’appui-feu des unités de chars de combat par la destruction des moyens anti-char adverses.
                Le BMPT Terminator utilise le châssis du char de combat T-72 ou du T-90 avec le poste de pilotage à l’avant centre et la motorisation à l’arrière.
                Le BMPT possède une large panoplie d’armement. La tourelle biplace en superstructure est dotée de deux canons 2A42 de 30 mm,
                d’une mitrailleuse coaxiale PKT de 7,62 mm et de quatre missiles supersoniques anti-chars à guidage laser Ataka-T.
                Deux lances-grenades automatiques AG-17D sont installés à l’avant-gauche et l’avant-droite du véhicule.
                Les AG-17D sont commandés, indépendamment de l’armement principale, par deux membres d’équipage installés de part et d’autre du pilote.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco102.jpg",
            additionalImages = listOf("reco102_extra1.jpg", "reco102_extra2.jpg", "reco102_extra3.jpg", "reco102_extra4.jpg"),
            correct = "MaxxPro",
            options = listOf("Casspir", "Higuard", "MaxxPro", "Caiman"),
            description = """
                MaxxPro est un véhicule militaire blindé de transport de troupes ou de dépannage fabriqué par États-Unis.
                C'est un véhicule de type MRAP avec une coque en V pour se protéger contre les mines terrestres.
                Le MaxxPro est armé uniquement d'une tourelle sur le toit pouvant accueillir un fusil mitrailleur de 7,62 mm ou 12,7 mm mais
                également d'autres types d'arme d'infanterie comme un missile TOW ou un lance grenades automatique Mk19.
                Équipage: 2 plus 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco103.jpg",
            additionalImages = listOf("reco103_extra1.jpg", "reco103_extra2.jpg", "reco103_extra3.jpg", "reco103_extra4.jpg"),
            correct = "ACV-1",
            options = listOf("ACV-1", "AAV", "VBCI", "Higuard"),
            description = """
                Le SUPERAV est développé par Iveco et BAE Systems dans le cadre du programme ACV, Amphibious Combat Vehicle, pour le Corps des Marines des États-Unis.
                Canon : jusque 30 mm.
                Mitrailleuse : 12.7 mm.
                Lance-grenades automatique : Mk19 40 mm.
                Equipage : 3 + 13.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco104.jpg",
            additionalImages = listOf("reco103_extra1.jpg", "reco103_extra2.jpg", "reco103_extra3.jpg", "reco103_extra4.jpg"),
            correct = "ACV-1",
            options = listOf("ACV-1", "Boomerang", "VBCI", "Higuard"),
            description = """
                Le SUPERAV est développé par Iveco et BAE Systems dans le cadre du programme ACV, Amphibious Combat Vehicle, pour le Corps des Marines des États-Unis.
                Canon : jusque 30 mm.
                Mitrailleuse : 12.7 mm.
                Lance-grenades automatique : Mk19 40 mm.
                Equipage : 3 + 13.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco105.jpg",
            additionalImages = listOf("reco105_extra1.jpg", "reco105_extra2.jpg", "reco105_extra3.jpg", "reco105_extra4.jpg"),
            correct = "Ajax",
            options = listOf("Pizarro", "Piranha V", "Ajax", "M-113"),
            description = """
                L’Ajax est un véhicule blindé de combat britannique. Il est développé dans les années 2010.
                L’Ajax est entré en service en 2019.
                L’Ajax est destiné aux missions de reconnaissance et de combat.
                L’Ajax emploie le châssis chenillé du véhicule blindé ASCOD 2.
                Il est équipé d’une tourelle armée d’un canon CT40/40 CTAS de 40 mm qui emploie la munition télescopée et une mitrailleuse coaxiale L94A1 de 7,62 mm. Le véhicule est doté d’un tourelleau téléopéré.
                Equipage : 2 + 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco106.jpg",
            additionalImages = listOf("reco106_extra1.jpg", "reco106_extra2.jpg", "reco106_extra3.jpg", "reco106_extra4.jpg"),
            correct = "Aravis",
            options = listOf("Cougar", "Serval", "Higuard", "Aravis"),
            description = """
                Le VBHP, Véhicule Blindé Hautement Protégé, Aravis est un véhicule blindé à roues 4×4 français. Il est développé dans les années 2000.
                Le VBHP Aravis est entré en service en 2009.
                Canon : 20 mm.
                Mitrailleuse coaxiale : 7,62 mm / 12,7 mm.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco107.jpg",
            additionalImages = listOf("reco41_extra1.jpg", "reco41_extra2.jpg", "reco41_extra3.jpg", "reco41_extra4.jpg"),
            correct = "Boxer",
            options = listOf("Boxer", "Boomerang", "Eitan", "Higuard"),
            description = """
                Le Boxer est un véhicule blindé de combat à roues 8×8 allemand. Il est développé dans les années 2000.
                Le Boxer est entré en service en 2009.
                Le Boxer possède une structure modulaire. Le châssis, avec le compartiment moteur et le poste de pilotage, peut recevoir différents modules spécialisés.
                Le Boxer est proposé à l’exportation avec différentes configurations d’équipement et de système d’arme.
                Ils remplacent une partie du parc des véhicules M113 et Fuchs TPz 1.
                Le Boxer est mis en service par la Lituanie sous l’appellation Vilkas, par l’Australie avec la production sous licence et par le Royaume-Uni.
                Mitrailleuse : 7,62 mm / 12,7 mm. / LGA : 40mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco108.jpg",
            additionalImages = listOf("reco41_extra1.jpg", "reco41_extra2.jpg", "reco41_extra3.jpg", "reco41_extra4.jpg"),
            correct = "Boxer",
            options = listOf("LAV-25", "Boxer", "ACV-1", "Higuard"),
            description = """
                Le Boxer est un véhicule blindé de combat à roues 8×8 allemand. Il est développé dans les années 2000.
                Le Boxer est entré en service en 2009.
                Le Boxer possède une structure modulaire. Le châssis, avec le compartiment moteur et le poste de pilotage, peut recevoir différents modules spécialisés.
                Le Boxer est proposé à l’exportation avec différentes configurations d’équipement et de système d’arme.
                Ils remplacent une partie du parc des véhicules M113 et Fuchs TPz 1.
                Le Boxer est mis en service par la Lituanie sous l’appellation Vilkas, par l’Australie avec la production sous licence et par le Royaume-Uni.
                Mitrailleuse : 7,62 mm / 12,7 mm. / LGA : 40mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco109.jpg",
            additionalImages = listOf("reco41_extra1.jpg", "reco41_extra2.jpg", "reco41_extra3.jpg", "reco41_extra4.jpg"),
            correct = "Boxer",
            options = listOf("Griffon", "Ajax", "Eitan", "Boxer"),
            description = """
                Le Boxer est un véhicule blindé de combat à roues 8×8 allemand. Il est développé dans les années 2000.
                Le Boxer est entré en service en 2009.
                Le Boxer possède une structure modulaire. Le châssis, avec le compartiment moteur et le poste de pilotage, peut recevoir différents modules spécialisés.
                Le Boxer est proposé à l’exportation avec différentes configurations d’équipement et de système d’arme.
                Ils remplacent une partie du parc des véhicules M113 et Fuchs TPz 1.
                Le Boxer est mis en service par la Lituanie sous l’appellation Vilkas, par l’Australie avec la production sous licence et par le Royaume-Uni.
                Mitrailleuse : 7,62 mm / 12,7 mm. / LGA : 40mm.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco110.jpg",
            additionalImages = listOf("reco110_extra1.jpg", "reco110_extra2.jpg", "reco110_extra3.jpg", "reco110_extra4.jpg"),
            correct = "BRM-3K",
            options = listOf("BTR-D", "BRM-3K", "2S25 Sprut", "BMP-Terminator"),
            description = """
                Le BRM-3K Rys (Lynx en russe) est un véhicule blindé de reconnaissance russe. Il est développé dans les années 1990.
                Le BRM-3K Rys est entré en service en 1995.
                Le BRM-3K Rys est développé sur le châssis du véhicule blindé de combat d’infanterie BMP-3 pour les unités de reconnaissance.
                Ses missions sont: observation, détection d’objectifs, détermination de position des objectifs détectés et transmission des renseignements aux échelons supérieurs.
                Le BRM-3K est équipé d’un radar terrestre à effet Doppler 1RL-133-3, d’un télémètre laser, d’une caméra thermique,
                d’une système de navigation, d’un système de transmission des données à une distance de 100 km en mouvement et 350 km à l’arrêt.
                Canon : 30 mm.
                Equipage : 3 + 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco111.jpg",
            additionalImages = listOf("reco111_extra1.jpg", "reco111_extra2.jpg", "reco111_extra3.jpg", "reco111_extra4.jpg"),
            correct = "BTR-D",
            options = listOf("BTR-D", "BRM-3K Rys", "2S25 Sprut", "BMP-Terminator"),
            description = """
                Le BTR-D est un véhicule blindé de transport de troupe russe destiné aux unités de parachutistes.
                Il a été développé en Union soviétique au début des années 1970.
                Le BTR-D est entré en service en 1974.
                Le BTR-D est développé à partir du châssis du VBCI BMD-1.
                Il offre un volume interne supérieur pour installé différents équipements et systèmes d’armes.
                Le BTR-D est dépourvu d’une tourelle et peut recevoir une ou deux mitrailleuses de 7,62 mm. Amphibie.
                Le châssis du BTR-D a servi pour différents véhicules blindés.
                Mitrailleuse : 7.62 mm PKT.
                Equipage : 3 + 10.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco112.jpg",
            additionalImages = listOf("reco111_extra1.jpg", "reco111_extra2.jpg", "reco111_extra3.jpg", "reco111_extra4.jpg"),
            correct = "BTR-D",
            options = listOf("BTR-D", "BRM-3K Rys", "2S25 Sprut", "BMP-Terminator"),
            description = """
                Le BTR-D est un véhicule blindé de transport de troupe russe destiné aux unités de parachutistes.
                Il a été développé en Union soviétique au début des années 1970.
                Le BTR-D est entré en service en 1974.
                Le BTR-D est développé à partir du châssis du VBCI BMD-1.
                Il offre un volume interne supérieur pour installé différents équipements et systèmes d’armes.
                Le BTR-D est dépourvu d’une tourelle et peut recevoir une ou deux mitrailleuses de 7,62 mm. Amphibie.
                Le châssis du BTR-D a servi pour différents véhicules blindés.
                Mitrailleuse : 7.62 mm PKT.
                Equipage : 3 + 10.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco113.jpg",
            additionalImages = listOf("reco113_extra1.jpg", "reco113_extra2.jpg", "reco113_extra3.jpg", "reco113_extra4.jpg"),
            correct = "Patria",
            options = listOf("Eitan", "Fennek", "Patria", "Fuchs"),
            description = """
                Le Patria 6×6 est développé par la société finlandaise.
                Le véhicule peut accueillir deux membres d’équipage et dix fantassins.
                L’armement standard est composé d’une mitrailleuse lourde de 12,7 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco114.jpg",
            additionalImages = listOf("reco114_extra1.jpg", "reco114_extra2.jpg", "reco114_extra3.jpg", "reco114_extra4.jpg"),
            correct = "Cougar",
            options = listOf("Caiman", "MaxxPro", "Aravis", "Cougar"),
            description = """
                Le Cougar est un véhicule blindé du type MRAP américain. Il est développé dans les années 2000.
                Le Cougar est entré en service en 2002.
                Le MRAP, Mine Resistant Ambush Protected, Cougar est conçu pour fournir une meilleure protection à son équipage face aux mines et aux engins explosifs.
                Le Cougar est développé en version 4×4 et 6×6.
                Mitrailleuse : 12,7 mm / 7,62 mm.
                Equipage : 2 + 4 Cougar 4×4 / 2 + 8 Cougar 6×6.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco115.jpg",
            additionalImages = listOf("reco115_extra1.jpg", "reco115_extra2.jpg", "reco115_extra3.jpg", "reco115_extra4.jpg"),
            correct = "Eitan",
            options = listOf("Eitan", "VBCI", "Lazar-3", "ACV-1"),
            description = """
                L’Eitan est un projet de véhicule blindé de combat à roues 8×8 israélien. Il est en cours de développement.
                Le premier démonstrateur du Eitan est dévoilé par le ministère israélien de la Défense le 1er septembre 2016.
                Il est en développement pour remplacer la flotte des véhicules blindés de transport de troupe chenillés M113.
                L’Eitan peut être décliné en différentes versions: VBTT, VBCI à roues, commandement, évacuation médicale, porte-mortier, génie, etc.
                Armement: Mitrailleuse : 12.7 mm / 7.62 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco116.jpg",
            additionalImages = listOf("reco116_extra1.jpg", "reco116_extra2.jpg", "reco116_extra3.jpg", "reco116_extra4.jpg"),
            correct = "ERC-90 Sagaie",
            options = listOf("AMX-10RC", "ERC-90 Sagaie", "AMX-13", "Aravis"),
            description = """
                L’ERC-90 est un VAL, Véhicule à Armement Lourd, à roues 6×6 français. Il a été développé dans les années 1970.
                L’ERC-90 est entré en service en 1979.
                L’ERC-90 est principalement utilisé dans les unités de reconnaissance.
                L’ERC-90 possède la capacité amphibie et est propulsé dans l’eau avec deux hélices.
                Canon : 90 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco117.jpg",
            additionalImages = listOf("reco117_extra1.jpg", "reco117_extra2.jpg", "reco117_extra3.jpg", "reco117_extra4.jpg"),
            correct = "Foxhound",
            options = listOf("Cougar", "MaxxPro", "Caiman", "Foxhound"),
            description = """
                Le LPPV, Light Protection Patrol Vehicle, Foxhound est un véhicule blindé à roues 4×4 britannique. Il est développé dans les années 2000.
                Le Foxhound est entré en service en 2011.
                Mitrailleuse : 2 x 7.62 mm.
                Equipage : 2 + 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco118.jpg",
            additionalImages = listOf("reco118_extra1.jpg", "reco118_extra2.jpg", "reco118_extra3.jpg", "reco118_extra4.jpg"),
            correct = "Scimitar",
            options = listOf("Scorpion", "Scimitar", "Warrior", "Wiesel"),
            description = """
                Le FV107 Scimitar est un véhicule blindé de reconnaissance britannique. Il est développé dans les années 1960.
                Le FV107 Scimitar est entré en service en 1971.
                Le FV107 Scimitar est développé comme un véhicule de reconnaissance.
                Il est équipé d’une tourelle biplace armée du canon automatique L21 Rarden de 30 mm et de la mitrailleuse coaxiale de 7,62 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco120.jpg",
            additionalImages = listOf("reco120_extra1.jpg", "reco120_extra2.jpg", "reco120_extra3.jpg", "reco120_extra4.jpg"),
            correct = "GRF Vector",
            options = listOf("Grizzly", "GRF Vector", "Jackal", "LMV Lince"),
            description = """
                Le GRF est un véhicule des Forces spéciales néerlandais. Il est développé dans les années 2010.
                Le GRF (Ground force) est une plate-forme tactique légère aérotransportable, spécialement conçue pour les unités des Forces spéciales.
                Le GRF peut recevoir différentes configurations et intégrer un large panel d’armements et d’équipements.
                Armement: M2 Browning 12,7 mm. / Lance-grenades automatique HK GMG 40 mm.
                Equipage : 3 + 1.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco121.jpg",
            additionalImages = listOf("reco121_extra1.jpg", "reco121_extra2.jpg", "reco121_extra3.jpg", "reco121_extra4.jpg"),
            correct = "Higuard",
            options = listOf("Higuard", "BRM-3K Rys", "Foxhound", "Titus"),
            description = """
                Le Higuard est un véhicule blindé français Higuard du type MRAP. Il est développé dans les années 2010.
                Le Higuard est entré en service en 2012.
                La caisse en V prononcé offre un important niveau de protection contre les mines et les IED.
                Le véhicule transporte deux membres d’équipage et dix fantassins.
                Le Higuard peut recevoir un tourelleau téléopéré armé d’une mitrailleuse lourde de 12,7 mm, une mitrailleuse de 7,62 mm ou lance-grenades automatique de 40 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco122.jpg",
            additionalImages = listOf("reco84_extra1.jpg", "reco84_extra2.jpg", "reco84_extra3.jpg", "reco84_extra4.jpg"),
            correct = "JLTV Falcon",
            options = listOf("Aravis", "Foxhound", "Dingo", "JLTV Falcon"),
            description = """
                Le JLTV, Joint Light Tactical Vehicle, est un véhicule blindé à roues 4×4 américain. Il a été développé dans les années 2010,
                dans le cadre du programme de remplacement du HMMWV. Le JLTV est entré en service en 2018.
                Le JLTV est développé à partir du véhicule blindé à roues L-ATV, Light Combat Tactical All-Terrain Vehicle.
                Il possède le niveau de protection balistique et la protection anti-mines du niveau d’un MRAP, Mine Resistant Ambush Protected.
                Le véhicule peut recevoir différents types d’armement avec des tourelleaux téléopérés.
                Le JLTV est activement promu à l’exportation.
                Armement: 12,7 mm à 20 mm.
                Equipage : 2 + 2 à 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco123.jpg",
            additionalImages = listOf("reco84_extra1.jpg", "reco84_extra2.jpg", "reco84_extra3.jpg", "reco84_extra4.jpg"),
            correct = "JLTV Falcon",
            options = listOf("Aravis", "Foxhound", "Dingo", "JLTV Falcon"),
            description = """
                Le JLTV, Joint Light Tactical Vehicle, est un véhicule blindé à roues 4×4 américain. Il a été développé dans les années 2010,
                dans le cadre du programme de remplacement du HMMWV. Le JLTV est entré en service en 2018.
                Le JLTV est développé à partir du véhicule blindé à roues L-ATV, Light Combat Tactical All-Terrain Vehicle.
                Il possède le niveau de protection balistique et la protection anti-mines du niveau d’un MRAP, Mine Resistant Ambush Protected.
                Le véhicule peut recevoir différents types d’armement avec des tourelleaux téléopérés.
                Le JLTV est activement promu à l’exportation.
                Armement: 12,7 mm à 20 mm.
                Equipage : 2 + 2 à 3.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco124.jpg",
            additionalImages = listOf("reco124_extra1.jpg", "reco124_extra2.jpg", "reco124_extra3.jpg", "reco124_extra4.jpg"),
            correct = "Lazar-3",
            options = listOf("Typhoon-K", "ACV-1", "Lazar-3", "LAV-25"),
            description = """
                Le Lazar-3 est un véhicule blindé de combat à roues 8×8 serbe. Il a été développé les années 2010.
                Le Lazar-3 est entré en service en 2018.
                Le véhicule est doté de cinq vitres de chaque côté et deux vitres à l’arrière permettant un bon niveau d’observation.
                Chaque vitre est munie d’une trappe de tir pour armement léger.
                Dans sa version VBCI, véhicule blindé de combat d’infanterie, le Lazar-3 peut recevoir une tourelle armée d’un canon automatique de 20 mm ou de 30 mm.
                La version VBTT, véhicule blindé de transport de troupe, est équipée d’un tourelleau téléopéré armé d’une mitrailleuse lourde de 12,7 mm.
                Armement : 30 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco125.jpg",
            additionalImages = listOf("reco124_extra1.jpg", "reco124_extra2.jpg", "reco124_extra3.jpg", "reco124_extra4.jpg"),
            correct = "Lazar-3",
            options = listOf("Typhoon-K", "ACV-1", "Lazar-3", "LAV-25"),
            description = """
                Le Lazar-3 est un véhicule blindé de combat à roues 8×8 serbe. Il a été développé les années 2010.
                Le Lazar-3 est entré en service en 2018.
                Le véhicule est doté de cinq vitres de chaque côté et deux vitres à l’arrière permettant un bon niveau d’observation.
                Chaque vitre est munie d’une trappe de tir pour armement léger.
                Dans sa version VBCI, véhicule blindé de combat d’infanterie, le Lazar-3 peut recevoir une tourelle armée d’un canon automatique de 20 mm ou de 30 mm.
                La version VBTT, véhicule blindé de transport de troupe, est équipée d’un tourelleau téléopéré armé d’une mitrailleuse lourde de 12,7 mm.
                Armement : 30 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco126.jpg",
            additionalImages = listOf("reco124_extra1.jpg", "reco124_extra2.jpg", "reco124_extra3.jpg", "reco124_extra4.jpg"),
            correct = "Lazar-3",
            options = listOf("Typhoon-K", "ACV-1", "Lazar-3", "LAV-25"),
            description = """
                Le Lazar-3 est un véhicule blindé de combat à roues 8×8 serbe. Il a été développé les années 2010.
                Le Lazar-3 est entré en service en 2018.
                Le véhicule est doté de cinq vitres de chaque côté et deux vitres à l’arrière permettant un bon niveau d’observation.
                Chaque vitre est munie d’une trappe de tir pour armement léger.
                Dans sa version VBCI, véhicule blindé de combat d’infanterie, le Lazar-3 peut recevoir une tourelle armée d’un canon automatique de 20 mm ou de 30 mm.
                La version VBTT, véhicule blindé de transport de troupe, est équipée d’un tourelleau téléopéré armé d’une mitrailleuse lourde de 12,7 mm.
                Armement : 30 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco127.jpg",
            additionalImages = listOf("reco127_extra1.jpg", "reco127_extra2.jpg", "reco127_extra3.jpg", "reco127_extra4.jpg"),
            correct = "LMV Lince",
            options = listOf("LMV Lince", "GRF_Vector", "Eagle V", "URO Vamtac"),
            description = """
                Le LMV Lince est un véhicule blindé multirôle à roues 4×4 italien. Il est développé dans les années 1990 par Iveco. 
                Le LMV Lince est entré en service en 2001. Le LMV, Light Multirole Vehicle, Lince (Lynx en italien) est également appelé M65. 
                Il est conçu pour les missions de liaison, d’escorte de convoi et de reconnaissance. Son blindage protège contre l’armement de petit calibre. 
                Un kit de surblindage offre la protection contre la munition de 12,7 mm. Le LMV Lince est largement exporté dans le monde. 
                Il est employé par l’armée britannique sous le nom Panther. Au sein de l’armée russe, il est employé sous le nom Rys, assemblé sous licence par Kamaz.
                Equipage : 1 + 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco128.jpg",
            additionalImages = listOf("reco128_extra1.jpg", "reco128_extra2.jpg", "reco128_extra3.jpg", "reco128_extra4.jpg"),
            correct = "Luchs",
            options = listOf("Lazar-3", "BTR-D", "Luchs", "LAV-25"),
            description = """
                Le SpPz Luchs est un véhicule blindé de reconnaissance à roues 8×8 allemand. Il est développé dans les années 1960-1970. 
                Le SpPz Luchs est entré en service en 1975. Le SpPz, Spähpanzer, Luchs (Lynx) est développé pour les unités de reconnaissance. 
                Afin d’optimiser sa capacité de mobilité, il est doté d’une architecture originale. Le châssis est doté de deux postes de pilotage installés à l’avant et à l’arrière. 
                Le véhicule peut repartir dans le sens inverse après avoir été pris en compte par le pilote arrière. 
                Le SpPz Luchs est un véhicule amphibie, propulsé dans l’eau avec deux hélices. Le SpPz Luchs est équipé d’une tourelle biplace, armée du canon MK-20 Rh-202 de 20 mm. 
                En 1985, le modèle Luchs A2 reçoit une caméra thermique. Au sein de l’armée allemande, le Luchs est remplacé par le véhicule de reconnaissance à roue 4×4 Fennek.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco129.jpg",
            additionalImages = listOf("reco129_extra1.jpg", "reco129_extra2.jpg", "reco129_extra3.jpg", "reco129_extra4.jpg"),
            correct = "Pars-3",
            options = listOf("Foxhound", "Pars-3", "FV430 Bulldog", "Piranha V"),
            description = """
                Le Pars III est un véhicule blindé de combat à roues 8×8 turc. Il est développé dans les années 2000-2010. 
                Le Pars III est entré en service en 2017. Le Pars III, d’un poids maximal de 30 tonnes, est équipé d’une tourelle mono-place Saber 
                armée du canon automatique Orbital ATK M242 Bushmaster à double approvisionnement et d’une mitrailleuse coaxiale de 7,62 mm. 
                Son équipage est de trois hommes et de neuf fantassins équipés.
                Armement : 25 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco130.jpg",
            additionalImages = listOf("reco113_extra1.jpg", "reco113_extra2.jpg", "reco113_extra3.jpg", "reco113_extra4.jpg"),
            correct = "Patria",
            options = listOf("Patria", "Aravis", "Centauro", "Freccia"),
            description = """
                Le Patria 6×6 est développé par la société finlandaise. Le véhicule peut accueillir deux membres d’équipage et dix fantassins. 
                L’armement standard est composé d’une mitrailleuse lourde de 12,7 mm.
                Equipage : 2 + 10.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco131.jpg",
            additionalImages = listOf("reco131_extra1.jpg", "reco131_extra2.jpg", "reco131_extra3.jpg", "reco131_extra4.jpg"),
            correct = "LAV-25",
            options = listOf("Patria", "Centauro", "Foxhound", "LAV-25"),
            description = """
                Le LAV-25, Light Armoured Vehicle 25 mm, est un véhicule blindé de reconnaissance américain. Il est développé dans les années 1970-1980. 
                Le LAV-25 est entré en service en 1983. Le LAV-25 est employé au sein des unités de reconnaissance. 
                Le LAV-25 est basé sur le châssis du véhicule blindé à roues 8×8 Piranha. 
                Le LAV-25 est équipé d’une tourelle biplace dérivée du véhicule de combat d’infanterie M2 Bradley. 
                L’armement est composé du canon automatique M242 Bushmaster de 25 mm, d’une mitrailleuse coaxiale M240 de 7,62 mm.
                Equipage : 3 + 6.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco132.jpg",
            additionalImages = listOf("reco132_extra1.jpg", "reco132_extra2.jpg", "reco132_extra3.jpg", "reco132_extra4.jpg"),
            correct = "Titus",
            options = listOf("Serval", "Foxhound", "Titus", "Higuard"),
            description = """
                Le Titus est un véhicule blindé à roues 6×6 français. Il est développé dans les années 2010 par Nexter Systems. 
                Le véhicule peut accueillir jusqu’à douze fantassins en plus de son équipage de deux à trois hommes. 
                Le Titus est doté d’un important niveau de protection balistique et anti-mines. 
                Il peut recevoir un tourelleau téléopéré armé d’un canon automatique de 20 mm, une mitrailleuse lourde de 12,7 mm ou 
                un lance-grenades automatique de 40 mm.
                Equipage : 2-3 + 12.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco134.jpg",
            additionalImages = listOf("reco134_extra1.jpg", "reco134_extra2.jpg", "reco134_extra3.jpg", "reco134_extra4.jpg"),
            correct = "Typhoon-K",
            options = listOf("GAZ 2330 Tigr", "URAL-4320", "Ural Typhoon", "Typhoon-K"),
            description = """
                Le KamAZ-53949 Typhoon-K est un véhicule blindé multirôle à roues 4×4 russe. Il est développé dans les années 2010. 
                Le Typhoon-K est entré en service en 2019. Le Typhoon-K 4×4 fait partie des véhicules du type MRAP, Mine Resistant Ambush Protected. 
                Il possède la caisse mono-coque en V et est équipé de sièges absorbant les chocs.
                Armement : 12,7 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco135.jpg",
            additionalImages = listOf("reco135_extra1.jpg", "reco135_extra2.jpg", "reco135_extra3.jpg", "reco135_extra4.jpg"),
            correct = "Griffon",
            options = listOf("Higuard", "Griffon", "Aravis", "Foxhound"),
            description = """
                Le VBMR Griffon (Véhicule Blindé Multi-Rôles) est un véhicule blindé de transport de troupe à roues 6×6 français. 
                Il est développé par Nexter Systems. Le VBMR Griffon est entré en service en 2019.
                Armement : 12,7 mm / Lance-grenades automatique : 40 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco136.jpg",
            additionalImages = listOf("reco5_extra1.jpg", "reco5_extra2.jpg", "reco5_extra3.jpg", "reco5_extra4.jpg"),
            correct = "Serval",
            options = listOf("Serval", "Foxhound", "Typhoon-K", "Higuard"),
            description = """
                Le VBMR-L (Véhicules Blindés Multi-Rôles Légers) Serval est un véhicule blindé à roues 4×4 français. 
                Il est développé par Nexter Systems et Texelis. Le VBMR-L Serval est entré en service en 2022. 
                Il sera développé en plusieurs versions: Transport de troupe, Poste de commandement, Observation d’artillerie, 
                Véhicule du génie, Evacuation sanitaire, SA2R, Groupe de communication tactique, etc. 
                Le VBMR-L Serval est équipé des moyens de communication et du système de d’information et de combat SCORPION (SICS).
                Armement : Mitrailleuse 12,7 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco137.jpg",
            additionalImages = listOf("reco137_extra1.jpg", "reco137_extra2.jpg", "reco137_extra3.jpg", "reco137_extra4.jpg"),
            correct = "VCR Panhard",
            options = listOf("Patria", "VCR Panhard", "ERC-90-Sagaie", "Luchs"),
            description = """
                Le VCR, Véhicule de Combat à Roues, est un véhicule blindé de combat à roues 6×6 français. Il est développé dans les années 1970. 
                Le VCR est entré en service en 1979. Le VCR est basé sur le véhicule à roues 4×4 M3. 
                Il possède les mêmes train de roulement et motorisation que le véhicule à armement lourd ERC-90 Sagaie. 
                Le VCR est produit en plusieurs versions. La version principale est le VCR/TT, Transport de Troupes. 
                Il peut recevoir une mitrailleuse de 7,62 mm ou une mitrailleuse lourde de 12,7 mm. 
                Les versions spécifiques sont équipées de missiles HOT, de missiles Milan ou d’un canon de 20 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco138.jpg",
            additionalImages = listOf("reco137_extra1.jpg", "reco137_extra2.jpg", "reco137_extra3.jpg", "reco137_extra4.jpg"),
            correct = "VCR Panhard",
            options = listOf("Patria", "VCR Panhard", "ERC-90-Sagaie", "Luchs"),
            description = """
                Le VCR, Véhicule de Combat à Roues, est un véhicule blindé de combat à roues 6×6 français. It is developed in the 1970s. 
                Le VCR est entré en service en 1979. Le VCR est basé sur le véhicule à roues 4×4 M3. 
                Il possède les mêmes train de roulement et motorisation que le véhicule à armement lourd ERC-90 Sagaie. 
                Le VCR est produit en plusieurs versions. La version principale est le VCR/TT, Transport de Troupes. 
                Il peut recevoir une mitrailleuse de 7,62 mm ou une mitrailleuse lourde de 12,7 mm. 
                Les versions spécifiques sont équipées de missiles HOT, de missiles Milan ou d’un canon de 20 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco139.jpg",
            additionalImages = listOf("reco139_extra1.jpg"),
            correct = "GBC 180",
            options = listOf("URAL 4320", "VCR Panhard", "TRM 2000", "GBC 180"),
            description = """
                GBC 180 - Gazelle Berliet Cargo (180 ch). Il est principalement destiné à transporter du personnel, du fret ou des shelters 
                et à assurer des missions spécialisées de dépannage (lot 7) et de ravitaillement en carburant (CCT).
                Tous les GBC 180 « cabine torpédo plateau ridelles bâche » sont prédisposés pour recevoir un affût de circulaire 12,7.
                Le GBC 180 est un véhicule tactique 6 x 6, pouvant emmener trois personnes en cabine.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco140.jpg",
            additionalImages = listOf("reco140_extra1.jpg, reco140_extra2.jpg"),
            correct = "ACMAT VT-4",
            options = listOf("Ford Ranger", "GAS Tiger", "ACMAT VT-4", "Peugeot P-4"),
            description = """
                Véhicule de commandement et de liaison léger, non blindé, le  véhicule tactique 4X4 (VT4) est de type 4x4 militarisé, 
                destiné à assurer des liaisons au profit de 5 soldats ou 4 combattants équipés FELIN dans un contexte opérationnel 
                (opérations intérieures type Sentinelle ou opérations extérieures en phase de stabilisation et normalisation) 
                et de préparation opérationnelle (sécurité des activités, manœuvres, formation et entraînement).
                Le VT4 est un véhicule léger tactique polyvalent remplaçant le véhicule P4 (Peugeot 4 roues motrices). 
                Il allie modernité, puissance, capacité de franchissement et dispose d’un profil de roulage complet 
                (sur route, en tout chemin et en tout-terrain).
                Pré-équipé pour recevoir le kit de grilles de protection des vitres
                Pré-équipé pour recevoir deux postes PR4G,
                un système de commandement type ordinateur tactile et un GPS
                Équipé de systèmes d’occultation de sources lumineuses (black-out).
                Poids à vide (PV) : 2,66 tonnes.
                Charge utile : 770 kg.
                Autonomie : 800 km.
                Équipages: 5 soldats ou 4 combattants avec tenue et équipement FELIN.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco141.jpg",
            additionalImages = listOf("reco141_extra1.jpg, reco141_extra2.jpg"),
            correct = "Peugeot P-4",
            options = listOf("Ford Ranger", "GAS Tiger", "ACMAT VT-4", "Peugeot P-4"),
            description = """
                Le Peugeot P4 est un véhicule léger à quatre roues motrices produit par le constructeur automobile français Peugeot de 1982 à 1992.
                Bien qu'équipé et motorisé par Peugeot, le P4 est une variante du Mercedes-Benz Classe G construit sous licence.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        ),
        Question(
            image = "reco142.jpg",
            additionalImages = listOf("reco142_extra1.jpg, reco142_extra2.jpg"),
            correct = "Masstech T4",
            options = listOf("Ford Ranger", "Masstech T4", "ACMAT VT-4", "Peugeot P-4"),
            description = """
                Le Masstech T4 est un véhicule militaire léger français, basé sur la Toyota Land Cruiser HZJ 76 
                et modifié par la société Technamm. En particulier, le véhicule est doté d'une radio PR4G, d'un terminal SITEL et d'un GPS DAGR. 
                Il peut embarquer quatre combattants équipés ou cinq passagers. Déployé uniquement en France avec l'Opération Sentinelle, 
                son moteur, rustique et compatible avec les carburants africains et militaires, autorise son envoi en OPEX.
            """.trimIndent(),
            moreInfo = null,
            category = "recon"
        )
    )
}