package com.example.quizapp

object Test_bm2 {
    val QUESTION = listOf(
        Question(
            image = "bm2_question1.webp",
            additionalImages = listOf("bm2_question1_extra1.jpg", "bm2_question1_extra2.jpg", "bm2_question1_extra3.jpg"),
            correct = "BRDM-2",
            options = listOf("BTR-D", "BMO-T", "BTR-60", "BRDM-2"),
            category = "bm2",
            description = """
                Le BRDM-2 est un véhicule blindé de reconnaissance russe. Il a été développé en Union soviétique au début des années 1950. 
                Son armement est composé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm, 
                identiques aux véhicules blindés de transport de troupe russe BTR-60/BTR-70/BTR-80. 
                Le BRDM-2, en différents versions, est utilisé par les forces armées de plus de 50 pays dans le monde. 
                Entré en service en 1962.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question2.jpg",
            additionalImages = listOf("bm2_question2_extra1.jpg", "bm2_question2_extra2.jpg", "bm2_question2_extra3.jpg", "bm2_question2_extra4.jpg", "bm2_question2_extra5.jpg"),
            correct = "BRM-1K",
            options = listOf("BRM-1K", "BMP-1", "BRM-3K", "BMD-1"),
            category = "bm2",
            description = """
                Le BRM-1K est un véhicule blindé de reconnaissance russe. Il est développé à partir du véhicule blindé de combat d’infanterie BMP-1 dans les années 1970. 
                Il possède la même architecture que le BMP-1 avec la motorisation à l’avant droit et le poste de pilotage à l’avant gauche. 
                Le BRM-1K a reçu une nouvelle tourelle biplace contrairement à la tourelle monoplace du BMP-1. 
                L’armement est composé du canon 2A28 Grom de 73 mm, identique au BMP-1.
                Canon : 73 mm 2A28 Grom.
                Equipage : 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question3.jpg",
            additionalImages = listOf("bm2_question3_extra1.jpg", "bm2_question3_extra2.jpg", "bm2_question3_extra3.jpg"),
            correct = "BRM-3K",
            options = listOf("BTR-D", "BMO-T", "BRM-1K", "BRM-3K"),
            category = "bm2",
            description = """
                Le BRM-3K Rys (Lynx en russe) est un véhicule blindé de reconnaissance russe, entré en service en 1995. 
                Il est développé sur le châssis du véhicule blindé de combat d’infanterie BMP-3 pour les unités de reconnaissance. 
                Ses missions sont : observation, détection d’objectifs, détermination de position des objectifs détectés et transmission des renseignements aux échelons supérieurs. 
                Le BRM-3K est équipé d’un radar terrestre à effet Doppler 1RL-133-3, d’un télémètre laser, d’une caméra thermique, 
                d’une système de navigation, d’un système de transmission des données à une distance de 100 km en mouvement et 350 km à l’arrêt. 
                Canon : 30 mm.
                Equipage : 3 + 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question4.jpg",
            additionalImages = listOf("bm2_question4_extra1.jpg", "bm2_question4_extra2.jpg", "bm2_question4_extra3.jpg"),
            correct = "Scimitar",
            options = listOf("Scorpion", "Scimitar", "Warrior", "Wiesel"),
            category = "bm2",
            description = """
                Le FV107 Scimitar est un véhicule blindé de reconnaissance britannique, entré en service en 1971. 
                Il est équipé d’une tourelle biplace armée du canon automatique L21 Rarden de 30 mm et de la mitrailleuse coaxiale de 7,62 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question5.jpg",
            additionalImages = listOf("bm2_question5_extra1.jpg", "bm2_question5_extra2.jpg", "bm2_question5_extra3.jpg"),
            correct = "BMP-1",
            options = listOf("BMD-1", "BMD-2", "BMP-1", "BMP-2"),
            category = "bm2",
            description = """
                Le BMP-1 est un véhicule blindé de combat d’infanterie russe, entré en service en 1966. 
                Il est le premier véhicule blindé de sa catégorie, développé pour renforcer les capacités des unités d’infanterie mécanisées. 
                Le BMP-1 est un véhicule amphibie et apte à combattre dans l’ambiance NRBC. 
                Son équipage est composé du chef d’engin, du tireur, du pilote et de huit fantassins. 
                Le BMP-1 est doté d’une tourelle monoplace armée d’un canon 2A28 Grom à âme lisse de 73 mm, 
                d’une mitrailleuse coaxiale PKTM de 7,62 mm et du missile antichars 9M14M Malyutka, AT-3 Sagger. 
                Les fantassins ont la capacité de tirer depuis l’intérieur du véhicule par neuf trappes de tir. 
                Entre 1966 et 1988, plus de 20 000 BMP-1 ont été construits. En août 2018, une nouvelle version du BMP-1 est présentée sous le nom BMP-1AM Basurman.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question6.jpg",
            additionalImages = listOf("bm2_question6_extra1.jpg", "bm2_question6_extra2.jpg", "bm2_question6_extra3.jpg"),
            correct = "BMP-2",
            options = listOf("BMP-1", "BMP-2", "BRM-1K", "BRM-3K"),
            category = "bm2",
            description = """
                Le BMP-2 est un véhicule de combat d’infanterie russe, entré en service en 1981. 
                Il est développé sur la base du châssis du véhicule blindé de combat d’infanterie BMP-1. 
                Le groupe motopropulseur est à l’avant droit et le poste du pilote à l’avant gauche. Il est amphibie et protégé NBC. 
                Pour améliorer ses capacités de feu, le BMP-2 reçoit une tourelle biplace, armée d’un canon automatique 2A42 de 30 mm, 
                une mitrailleuse coaxiale PKT de 7,62 mm et les missiles antichars. 
                Adopté par de nombreuses armées dans le monde.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question7.jpg",
            additionalImages = listOf("bm2_question7_extra1.jpg", "bm2_question7_extra2.jpg", "bm2_question7_extra3.jpg"),
            correct = "BMP-3",
            options = listOf("BMD-3", "BMP-3", "BRM-1K", "BRM-3K"),
            category = "bm2",
            description = """
                Le BMP-3 est un véhicule de combat d’infanterie russe, entré en service en 1987. 
                Il est le véhicule le plus fortement armé de sa catégorie. 
                Son armement est composé du canon à basse pression 2A70 de 100 mm, du canon automatique 2A72 de 30 mm, 
                de la mitrailleuse coaxiale PKT de 7.62 mm. 
                Le BMP-3 peut tirer par le canon de 100 mm le missile antichar 9M117 du système 9K166-3 Basnya, AT-12 Swinger. 
                Le châssis du BMP-3 adopte la configuration de la famille des véhicules blindé combat des unités de parachutistes russe BMD.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question8.jpg",
            additionalImages = listOf("bm2_question8_extra1.jpg", "bm2_question8_extra2.jpg", "bm2_question8_extra3.jpg"),
            correct = "MTLB",
            options = listOf("BTR-60", "BTR-D", "MTLB", "BMO-T"),
            category = "bm2",
            description = """
                Le MTLB est un véhicule blindé de transport multirôle russe, entré en service en 1964. 
                Il est conçu comme un tracteur de canon d’artillerie et un transporteur de matériel. 
                Par la suite, le MTLB a été adopté en version transport de troupe, véhicule d’évacuation sanitaire et la plate-forme d’installation de différents systèmes et équipements.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question9.jpg",
            additionalImages = listOf("bm2_question9_extra1.jpg", "bm2_question9_extra2.jpg", "bm2_question9_extra3.jpg"),
            correct = "BMD-1",
            options = listOf("BMP-1", "BMP-2", "ZBD-05", "BMD-1"),
            category = "bm2",
            description = """
                Le BMD-1 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes, entré en service en 1969. 
                La tourelle monoplace est armée d’un canon 2A28 Grom de 73 mm, d’une mitrailleuse coaxiale PKT de 7,62 mm.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question10.jpg",
            additionalImages = listOf("bm2_question10_extra1.jpg", "bm2_question10_extra2.jpg", "bm2_question10_extra3.jpg"),
            correct = "BMD-2",
            options = listOf("BMD-2", "BMP-2", "ZTD-05", "BMD-1"),
            category = "bm2",
            description = """
                Le BMD-2 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes, développé dans les années 1980. 
                Il est développé à partir du châssis du BMD-1. Il possède le nouveau système d’arme avec une tourelle monoplace armée du canon 2A42 de 30 mm 
                et d’une mitrailleuse coaxiale PKT de 7,62 mm. 
                Pour la lutte antichar, il est doté d’un système apte à tirer les missiles antichars 9M111 Fagot AT-4 ou 9M113 Konkurs AT-5.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question11.jpg",
            additionalImages = listOf("bm2_question11_extra1.jpg", "bm2_question11_extra2.jpg", "bm2_question11_extra3.jpg"),
            correct = "BMD-3",
            options = listOf("BRM-3K", "BMP-3", "BMD-3", "BTR-4 Bucephale"),
            category = "bm2",
            description = """
                Le BMD-3 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes, entré en service en 1990. 
                Il est parachutable. 
                Canon : 30 mm 2A42 x100.2 calibres. 
                Mitrailleuse coaxiale : 7.62 mm PKT. 
                Lance-grenade automatique de capot : 30 mm AG-17.
                Equipage : 2 + 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question12.jpg",
            additionalImages = listOf("bm2_question12_extra1.jpg", "bm2_question12_extra2.jpg", "bm2_question12_extra3.jpg"),
            correct = "BMD-4",
            options = listOf("BRM-3K", "BMP-3", "BMD-3", "BMD-4"),
            category = "bm2",
            description = """
                Le BMD-4 est un véhicule blindé de combat d’infanterie russe destiné aux unités de parachutistes, entré en service en 2004. 
                Il possède le même châssis à cinq galets que son prédécesseur le BMD-3. 
                Le véhicule reçoit une nouvelle tourelle Bakhtcha-U qui permet d’augmenter significativement la puissance de feu. 
                La tourelle Bakhtcha-U est dérivée du système d’arme du véhicule blindé de combat d’infanterie BMP-3. 
                Canon : 100 mm / 30 mm. 
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 2 + 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question13.jpg",
            additionalImages = listOf("bm2_question13_extra1.jpg", "bm2_question13_extra2.jpg", "bm2_question13_extra3.jpg"),
            correct = "BTR-60",
            options = listOf("BTR-4", "BTR-60", "BTR-70", "BTR-80"),
            category = "bm2",
            description = """
                Le BTR-60 est un véhicule blindé transport de troupe à roues 8×8 russe, entré en service en 1960. 
                La version BTR-60P au toit ouvert transporte deux membres d’équipage et 12 à 14 fantassins. 
                L’armement est composé d’une mitrailleuse de 12,7 mm actionnée manuellement et d’une ou plusieurs mitrailleuses de 7,62 mm installées sur les flancs. 
                La version BTR-60PA reçoit un toit et bénéficie de la protection NBC. 
                La version BTR-60PB est équipée d’un tourelleau monoplace armé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm. 
                Très largement exporté dans le monde.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question14.jpg",
            additionalImages = listOf("bm2_question14_extra1.jpg", "bm2_question14_extra2.webp", "bm2_question14_extra3.jpg"),
            correct = "BTR-70",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            category = "bm2",
            description = """
                Le BTR-70 est un véhicule blindé transport de troupe à roues 8×8 russe, entré en service en 1976. 
                Il possède la même architecture que son prédécesseur le BTR-60. 
                La version BTR-70 est équipée d’un tourelleau monoplace armé de la mitrailleuse lourde KPVT de 14,5 mm et de la mitrailleuse coaxiale PKT de 7,62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question15.jpg",
            additionalImages = listOf("bm2_question15_extra1.jpg", "bm2_question15_extra2.jpg", "bm2_question15_extra3.webp"),
            correct = "BTR-80",
            options = listOf("BTR-60", "BTR-70", "BTR-80", "BTR-80A"),
            category = "bm2",
            description = """
                Le BTR-80 est un véhicule blindé transport de troupe à roues 8×8 russe, entré en service en 1986. 
                Il possède la même architecture que son prédécesseur le BTR-70, avec la motorisation à l’arrière du véhicule. 
                L’accès à bord est effectué par les portes latérales doubles. 
                Mitrailleuse : 14.5 mm. 
                Mitrailleuse coaxiale : 7.62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question16.jpg",
            additionalImages = listOf("bm2_question16_extra1.jpg", "bm2_question16_extra2.jpg", "bm2_question16_extra3.jpg"),
            correct = "PATRIA XA-180",
            options = listOf("TPZ FUCHS", "PATRIA XA-180", "BMR-600", "PIRANHA-2"),
            category = "bm2",
            description = """
                Le XA-180 est un véhicule blindé de transport de troupe à roues 6×6 finlandais, entré en service en 1984. 
                Il est développé à la demande de l’armée finlandaise pour remplacer les véhicules blindés de transport de troupe BTR-60 soviétiques. 
                Le XA-180 est réalisé à partir du poids-lourd Sisu SA-150. 
                Le XA-180 est développé en plusieurs versions et exporté aux Pays-Bas, Norvège, Suède, Irlande et Estonie. 
                Les XA-180 en différentes versions ont été déployés en Afghanistan, Bosnie, Irak, Kosovo, Liban, Somalie, Tchad. 
                Mitrailleuse : 12,7 mm ou 7,62 mm.
                Equipage : 2 + 10 à 16.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question17.jpg",
            additionalImages = listOf("bm2_question17_extra1.jpg", "bm2_question17_extra2.jpg", "bm2_question17_extra3.jpg"),
            correct = "TPZ FUCHS",
            options = listOf("TPZ FUCHS", "PATRIA XA-180", "BMR-600", "PIRANHA-2"),
            category = "bm2",
            description = """
                Le TPz1 Fuchs est un véhicule blindé de transport de troupe à roues 6×6 allemand, développé à la fin des années 1970. 
                Armement : Mitrailleuse 7.62 mm.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question18.jpg",
            additionalImages = listOf("bm2_question18_extra1.jpg", "bm2_question18_extra2.jpg", "bm2_question18_extra3.jpg"),
            correct = "BMR-600",
            options = listOf("TPZ FUCHS", "PATRIA XA-180", "BMR-600", "PIRANHA-2"),
            category = "bm2",
            description = """
                Le BMR-600 est un véhicule blindé de transport de troupe à roues 6×6 espagnol, entré en service en 1979. 
                Près de 1 500 BMR-600 ont été produits en différentes versions. 
                Il a été exporté en Egypte, Arabie saoudite et Pérou. 
                Armement : 12,7 mm, M2HB.
                Equipage : 3 + 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question19.webp",
            additionalImages = listOf("bm2_question19_extra1.webp", "bm2_question19_extra2.webp", "bm2_question19_extra3.jpg"),
            correct = "PIRANHA-2",
            options = listOf("TPZ FUCHS", "PATRIA XA-180", "BMR-600", "PIRANHA-2"),
            category = "bm2",
            description = """
                Le Piranha 2 est un véhicule blindé de transport de troupe à roues 4x4/6×6/8x8 suisse. 
                Le Piranha 2 était une version améliorée de la famille réussie de véhicules de combat à roues de MOWAG. 
                Peut-être équipé d’une tourelle, de forme plate, formant un angle à sa base, placée sur la gauche, 
                au centre du toit du véhicule, avec une mitrailleuse de 12,7 mm et une mitrailleuse de 7,62 mm.
                Equipage : 3 + 9.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question20.jpg",
            additionalImages = listOf("bm2_question20_extra1.jpg", "bm2_question20_extra2.jpg", "bm2_question20_extra3.jpg"),
            correct = "M1126 STRIKER",
            options = listOf("M1126 STRIKER", "PATRIA XA-180", "BMR-600", "GTK Boxer"),
            category = "bm2",
            description = """
                Le M1126 Stryker est un véhicule blindé transport de troupe à roues 8×8 américain, entré en service en 2002. 
                Il est développé à partir du LAV-III Kodiak canadien sur la base mécanique du véhicule blindé Piranha III. 
                Il possède le tourelleau téléopéré RWS M151 Protecteur qui peut recevoir une mitrailleuse lourde M2HB de 12,7 mm 
                ou un lance-grenade automatique Mk 19 de 40 mm. 
                En juin 2021, l’armée américaine a sélectionné la tourelle téléopérée Samson armée d’un canon de 30 mm pour équiper les véhicules blindés de combat Stryker.
                Equipage : 2 + 9.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question21.jpg",
            additionalImages = listOf("bm2_question21_extra1.jpg", "bm2_question21_extra2.jpg", "bm2_question21_extra3.jpg"),
            correct = "M-113",
            options = listOf("Wiesel", "M-113", "Bradley", "FV430 Bulldog"),
            category = "bm2",
            description = """
                Le M113 est un véhicule blindé de transport de troupe américain, entré en service en 1960. 
                Il est développé pour les unités d’infanterie pour des missions de transport d’un groupe de fantassin. 
                Il a suivi plusieurs campagnes de modernisation. 
                Le M113 est dérivé en une large gamme de véhicules spécialisés : poste de commandement, mortier, sanitaire, défense sol-air, 
                véhicule du génie, lance-missile antichar, etc. 
                Adopté par de très nombreuses forces armées dans le monde.
                Mitrailleuse : 12.7 mm.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question22.jpg",
            additionalImages = listOf("bm2_question22_extra1.jpg", "bm2_question22_extra2.jpg", "bm2_question22_extra3.jpg"),
            correct = "SPZ Marder",
            options = listOf("MCV-80 Warrior", "M1126 STRIKER", "SPZ Marder", "FV430 Bulldog"),
            category = "bm2",
            description = """
                Le SPz Marder est un véhicule de combat d’infanterie allemand, entré en service en 1971. 
                Il est équipé d’une tourelle biplace en superstructure armée d’un canon automatique Rh 202 de 20 mm 
                et d’une mitrailleuse MG3A1 de 7,62 mm. 
                Livré en Indonésie, en Jordanie et au Chili.
                Equipage : 3 + 6 à 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question23.jpg",
            additionalImages = listOf("bm2_question23_extra1.jpg", "bm2_question23_extra2.jpg", "bm2_question23_extra3.jpg"),
            correct = "SPZ Puma",
            options = listOf("MCV-80 Warrior", "M2A2 Bradley", "SPZ Marder", "SPZ Puma"),
            category = "bm2",
            description = """
                Le SPz Puma est un véhicule de combat d’infanterie lourd allemand, développé dans les années 2000. 
                Il est développé pour remplacer les SPz Marder au sein des unités d’infanterie mécanisée allemandes. 
                Le SPz Puma est équipé d’une tourelle téléopérée armée d’un canon MK.30-2 de 30 mm et d’une mitrailleuse coaxiale de 5.56 mm. 
                Il possède deux niveaux de protection. Le niveau A, Aérotransportable, avec la protection anti-mine complète et la protection balistique 
                contre la munition de 30 mm de face et de 14,5 mm en latéral.
                Equipage : 3 + 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question24.jpg",
            additionalImages = listOf("bm2_question24_extra1.jpg", "bm2_question24_extra2.jpg", "bm2_question24_extra3.jpg"),
            correct = "M2A2 Bradley",
            options = listOf("MCV-80 Warrior", "SPZ Marder", "M2A2 Bradley", "SPZ Puma"),
            category = "bm2",
            description = """
                Le M2A2 Bradley est un véhicule de combat d’infanterie américain, entré en service en 1981. 
                Il est armé d’un canon Bushmaster de 25 mm. 
                A partir de 1986, le modèle M1A1 reçoit deux missiles antichar TOW. 
                Le M2A2 Bradley a suivi plusieurs campagnes de modernisation améliorant ses capacités au combat, son niveau de protection, 
                sa mobilité et divers équipements. 
                Une version de reconnaissance est développée sous le nom M3 Bradley.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question25.webp",
            additionalImages = listOf("bm2_question25_extra1.webp", "bm2_question25_extra2.webp", "bm2_question25_extra3.webp"),
            correct = "MCV-80 Warrior",
            options = listOf("MCV-80 Warrior", "SPZ Marder", "M2A2 Bradley", "SPZ Puma"),
            category = "bm2",
            description = """
                Le MCV-80 Warrior est un véhicule de combat d’infanterie britannique, entré en service en 1987. 
                Il est armé d’un canon automatique L21A1 Rarden de 30 mm et d’une mitrailleuse coaxiale L94A1 de 7,62 mm.
                Equipage : 3 + 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question26.webp",
            additionalImages = listOf("bm2_question26_extra1.webp", "bm2_question26_extra2.webp", "bm2_question26_extra3.webp"),
            correct = "BMP-KSHM",
            options = listOf("BMP-1", "BMP-1K", "BMP-KSHM", "BMR-2 CP"),
            category = "bm2",
            description = """
                Le BMP-KSHM est un véhicule blindé de commandement basé sur le châssis du véhicule BMP-1 d'origine russe, entré en service en 1972. 
                Pour son autodéfense, le BMP-KSHM est équipé d’une mitrailleuse PK de 7,62 mm. 
                Bien qu’il conserve la tourelle monoplace standard du BMP-1, son canon de 76 mm a été retiré et remplacé par une antenne télescopique plus grande, 
                appelée HAWK EYE. 
                Le BMP-1KSh est entièrement amphibie : sa propulsion dans l’eau est assurée par ses chenilles.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question27.webp",
            additionalImages = listOf("bm2_question27_extra1.webp", "bm2_question27_extra2.webp", "bm2_question27_extra3.webp"),
            correct = "R-142 NSA DEIMOS",
            options = listOf("URAL Typhoon", "1S91", "1V18 KLYON", "R-142 NSA DEIMOS"),
            category = "bm2",
            description = """
                Le R-142 Deimos est un véhicule de commandement et transmissions tactique (niveau bataillon/régiment) des forces terrestres russes. 
                Il permet d’établir des liaisons radio bidirectionnelles avec des stations homologues en toutes conditions (jour/nuit, toute saison), 
                à l’arrêt comme en mouvement (jusqu’à 40 km/h). 
                Portée opérationnelle : 2000 km en mode télégraphique (réception auditive).
                Equipage : 3 à 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question28.webp",
            additionalImages = listOf("bm2_question28_extra1.webp", "bm2_question28_extra2.jpg", "bm2_question28_extra3.jpg"),
            correct = "T-55",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "bm2",
            description = """
                Le T-55 est un char de combat russe, entré en service en 1958. 
                Il est très largement diffusé dans le monde. 
                Il a été produit sous licence en Pologne, en Tchécoslovaquie et en Roumanie. 
                Depuis sa mise en service, le T-55 a pris part à de nombreux conflits. 
                Il reste en service dans plusieurs forces armées. 
                Canon : 100 mm. 
                Mitrailleuse : 12,7 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question29.jpg",
            additionalImages = listOf("bm2_question29_extra1.jpg", "bm2_question29_extra2.jpg", "bm2_question29_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "bm2",
            description = """
                Le T-62 est un char moyen soviétique, entré en service en 1961. 
                Armement principal : canon de 115 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question30.jpg",
            additionalImages = listOf("bm2_question30_extra1.jpg", "bm2_question30_extra2.jpg", "bm2_question30_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "bm2",
            description = """
                Le T-64 est un char de combat soviétique, entré en service en 1965. 
                Il est le premier char soviétique à intégrer le chargement automatique. 
                Canon : 125 mm. 
                Mitrailleuse : 12.7 mm. 
                Missile antichar : AT-8 pour le T-64B.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question31.jpg",
            additionalImages = listOf("bm2_question31_extra1.jpg", "bm2_question31_extra2.jpg", "bm2_question31_extra3.jpg"),
            correct = "T-72",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "bm2",
            description = """
                Le T-72 est un char de combat russe, entré en service en 1973. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question32.webp",
            additionalImages = listOf("bm2_question32_extra1.jpg", "bm2_question32_extra2.jpg", "bm2_question32_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "bm2",
            description = """
                Le T-80 est un char de combat russe, développé dans les années 1980. 
                Armement : Canon - 125 mm. 
                Mitrailleuse : 12.7 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question33.webp",
            additionalImages = listOf("bm2_question33_extra1.jpg", "bm2_question33_extra2.jpg", "bm2_question33_extra3.jpg"),
            correct = "T-90",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "bm2",
            description = """
                Le T-90 est un char de combat russe, entré en service en 1992. 
                Armement : Canon - 125 mm. 
                Mitrailleuse : 12.7 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question34.webp",
            additionalImages = listOf("bm2_question34_extra1.jpg", "bm2_question34_extra2.jpg", "bm2_question34_extra3.jpg"),
            correct = "T-14",
            options = listOf("TYPE-10", "T-14", "Ariete", "Mercava-3"),
            category = "bm2",
            description = """
                Le T-14 Armata est un projet de char de combat russe, en développement depuis le début des années 2010. 
                Il est équipé d’une tourelle inhabitée et téléopérée. 
                Toute la dotation en munition est stockée dans le chargeur automatique installé dans le puits de la tourelle. 
                Les trois membres d’équipage sont placés à l’avant du châssis. 
                Canon : 125 mm. 
                Mitrailleuse : 12.7 mm ou 7.62 mm. 
                Lancement de la production en série annoncé pour 2019.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question35.webp",
            additionalImages = listOf("bm2_question35_extra1.jpg", "bm2_question35_extra2.jpg", "bm2_question35_extra3.webp"),
            correct = "M1A1 Abrams",
            options = listOf("Leopard-2", "Challenger-2", "M1A1 Abrams", "Mercava-4"),
            category = "bm2",
            description = """
                Le M1A1 Abrams est un char de combat américain, entré en service en 1980. 
                Les premiers modèles du M1 Abrams sont dotés d’un canon M68 de 105 mm. 
                A partir de la version M1A1, il est armé du canon M256 de 120 mm. 
                Canon : 120 mm. 
                Mitrailleuse : 12.7 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question36.webp",
            additionalImages = listOf("bm2_question36_extra1.webp", "bm2_question36_extra2.webp", "bm2_question36_extra3.jpg", "bm2_question36_extra4.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "Chieftain", "Magach-7", "M-60"),
            category = "bm2",
            description = """
                Le M60 est un char de combat américain, développé dans les années 1950. 
                Il a été très largement exporté dans le monde. 
                Il est encore en service dans plusieurs forces armées. 
                Armement : Canon - 105 mm. 
                Mitrailleuse : 12,7 mm Browning M2. 
                Plusieurs campagnes de modernisation : M60A1 (1962), M60A2 (1970), M60A3 (1975).
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question37.jpg",
            additionalImages = listOf("bm2_question37_extra1.jpg", "bm2_question37_extra2.webp", "bm2_question37_extra3.webp"),
            correct = "Leopard-1",
            options = listOf("AMX-30", "Leopard-1", "Challenger-2", "M1A1 Abrams"),
            category = "bm2",
            description = """
                Le Leopard 1 est un char de combat allemand, entré en service en 1965. 
                Il a été largement exporté dans le monde. 
                Actuellement, il est en service en Turquie, au Brésil et au Chili. 
                A partir de février 2023, l’Allemagne livre le Leopard 1 à l’Ukraine. 
                Le châssis du Leopard 1 a servi de base pour différents véhicules de soutien. 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question38.jpg",
            additionalImages = listOf("bm2_question38_extra1.jpg", "bm2_question38_extra2.webp", "bm2_question38_extra3.jpg"),
            correct = "Leopard-2",
            options = listOf("Leclerc", "Leopard-1", "Challenger-2", "Leopard-2"),
            category = "bm2",
            description = """
                Le Leopard 2 est un char de combat allemand, développé dans les années 1970. 
                Armement : Canon - 120 mm. 
                Mitrailleuse : 7,62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question39.jpg",
            additionalImages = listOf("bm2_question39_extra1.jpg", "bm2_question39_extra2.jpg", "bm2_question39_extra3.webp"),
            correct = "Chieftain",
            options = listOf("Challenger-1", "Chieftain", "Leopard-1", "Challenger-2"),
            category = "bm2",
            description = """
                Le Chieftain est un char de combat britannique, entré en service en 1966. 
                Il a été développé par Leyland Motors. 
                Le Chieftain a été employé dans les unités blindées britanniques jusqu’à son remplacement par le char de combat Challenger 1. 
                Il a été exporté en Iran, Irak, Koweït, Jordanie et Oman. 
                En Jordanie, il est employé sous l’appellation Khalid et en Iran sous l’appellation Shir. 
                Armement : Canon - 120 mm. 
                Mitrailleuse : 7.62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question40.webp",
            additionalImages = listOf("bm2_question40_extra1.jpg", "bm2_question40_extra2.jpg", "bm2_question40_extra3.jpg"),
            correct = "Challenger-1",
            options = listOf("Challenger-1", "Chieftain", "Leopard-1", "Challenger-2"),
            category = "bm2",
            description = """
                Le Challenger 1 est un char de combat britannique, entré en service en 1983. 
                La modernisation du Challenger 1 a donné naissance au Challenger 2. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question41.webp",
            additionalImages = listOf("bm2_question41_extra1.jpg", "bm2_question41_extra2.jpg", "bm2_question41_extra3.jpg"),
            correct = "Challenger-2",
            options = listOf("Challenger-1", "Chieftain", "Leopard-1", "Challenger-2"),
            category = "bm2",
            description = """
                Le Challenger 2 est un char de combat britannique, entré en service en 1998. 
                Il est connu sous l’appellation FV4030. 
                Challenger 2 est le char principal de l’armée britannique. 
                Il a participé à l’engagement au Kosovo et en Irak. 
                Le Challenger 2 a été exporté à Oman en 38 exemplaires. 
                En 2021, l’armée britannique recevra 148 chars modernisés sous l’appellation Challenger 3. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question42.jpg",
            additionalImages = listOf("bm2_question42_extra1.jpg", "bm2_question42_extra2.jpg", "bm2_question42_extra3.jpg"),
            correct = "Ariete",
            options = listOf("Challenger-1", "Challenger-2", "Leopard-1", "Ariete"),
            category = "bm2",
            description = """
                Le C1 Ariete est un char de combat italien, entré en service en 1995. 
                Armement : Canon - 120 mm. 
                Mitrailleuse : 12.7 mm, 7.62 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question43.webp",
            additionalImages = listOf("bm2_question43_extra1.jpg", "bm2_question43_extra2.webp", "bm2_question43_extra3.webp"),
            correct = "ZTZ-98",
            options = listOf("T-72", "T-80", "ZTZ-98", "ZTZ-99"),
            category = "bm2",
            description = """
                Le Type 98 (ZTZ-98) est un char de combat principal chinois, introduit en 1999 lors d’un défilé militaire à Pékin. 
                Il ressemble au T-72, mais la tourelle est soudée. 
                Son armement principal est un canon à âme lisse de 125 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question44.webp",
            additionalImages = listOf("bm2_question44_extra1.webp", "bm2_question44_extra2.webp", "bm2_question44_extra3.webp"),
            correct = "ZTZ-99",
            options = listOf("T-72", "T-80", "ZTZ-98", "ZTZ-99"),
            category = "bm2",
            description = """
                Le ZTZ-99 (Type-99) est un char de combat chinois, entré en service en 2003. 
                Il est équipé d’une caméra thermique et d’une lunette panoramique stabilisée du chef d’engin avec la capacité hunter-killer. 
                Le ZTZ-99 est équipé d’un système de contre-mesure tout azimut laser installé sur le toit de la tourelle. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question45.webp",
            additionalImages = listOf("bm2_question45_extra1.jpg", "bm2_question45_extra2.webp", "bm2_question45_extra3.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-3", "Merkava-4", "Magach-7", "Abrams"),
            category = "bm2",
            description = """
                Le Merkava Mk IV est un char de combat israélien, entré en service en 2004. 
                Le châssis du Merkava Mk IV a servi de base pour le véhicule blindé de transport de troupe lourd Namer. 
                Israël est l’unique utilisateur du char Merkava Mk IV. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm/12,7 mm. 
                Mortier : 60 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question46.jpg",
            additionalImages = listOf("bm2_question46_extra1.jpg", "bm2_question46_extra2.jpg", "bm2_question46_extra3.jpg"),
            correct = "PT-91",
            options = listOf("ZTZ-99", "TYPE-98", "PT-91", "T-64"),
            category = "bm2",
            description = """
                Le PT-91 Twardy est un char de combat polonais, entré en service en 1995. 
                Il est développé par Bumar Labedy à partir du char T-72M1. 
                Le PT-91 Twardy a été produit en 232 exemplaires pour les besoins de l’armée polonaise. 
                Entre 2007 et 2010, il a été exporté en Malaisie en 48 exemplaires sous le nom PT-91M Pendekar. 
                Armement : Canon - 125 mm. 
                Mitrailleuse coaxiale : 7.62 mm. 
                Mitrailleuse : 12.7 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question47.jpg",
            additionalImages = listOf("bm2_question47_extra1.webp", "bm2_question47_extra2.webp"),
            correct = "RPG-7",
            options = listOf("AT-5", "AT-7", "RPG-7", "SPG-9"),
            category = "bm2",
            description = """
                Le RPG-7 est un lance-roquette antichar russe, entré en service en 1961. 
                C’est un lance-roquette antichar léger, épaulable, rechargeable. 
                Le tube du lance-roquette peut tirer une large gamme de roquettes. 
                Le RPG-7 peut être équipé d’une lunette de tir PGO-7V ou d’une lunette de nuit infrarouge NSP-2. 
                Très largement répandu dans le monde, en service dans près de 70 pays.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question48.jpg",
            additionalImages = listOf("bm2_question48_extra1.jpg", "bm2_question48_extra2.jpg", "bm2_question48_extra3.jpg"),
            correct = "AT-4",
            options = listOf("AT-3", "AT-4", "AT-5", "AT-7"),
            category = "bm2",
            description = """
                L’AT-4 Spigot / 9K111 Fagot est un missile antichar russe, entré en service en 1973. 
                Il est filoguidé, semi-automatique, portable par une équipe de deux hommes. 
                Il est composé du poste de tir avec support 9P135 et du missile 9M111. 
                Le missile est conditionné dans un tube qui sert de tube de lancement et d’emballage tactique. 
                Calibre : 119 mm. 
                Perforation : 200 mm. 
                Portée maximale : 2000 m. 
                Distance de tir minimale : 70 m. 
                Adopté par de nombreuses armées dans le monde.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question49.webp",
            additionalImages = listOf("bm2_question49_extra1.jpg", "bm2_question49_extra2.jpg", "bm2_question49_extra3.webp"),
            correct = "AT-7",
            options = listOf("AT-3", "AT-4", "AT-5", "AT-7"),
            category = "bm2",
            description = """
                Le 9K115 Metis ou AT-7 Saxhorn est un système portable antichar soviétique/russe de niveau compagnie, 
                avec guidage semi-automatique par commande filaire, appartenant à la deuxième génération de missiles antichars (ATGM). 
                Il est conçu pour engager des cibles blindées, fixes ou mobiles, ainsi que des points fortifiés, 
                observables visuellement, à des distances comprises entre 40 et 2000 mètres.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question50.jpg",
            additionalImages = listOf("bm2_question50_extra1.webp", "bm2_question50_extra2.jpg", "bm2_question50_extra3.jpg"),
            correct = "SPG-9",
            options = listOf("AT-5", "RPG-7", "AT-7", "SPG-9"),
            category = "bm2",
            description = """
                Le SPG-9 (Lance en russe) est un lance-grenades antichar lourd soviétique, mis en service en 1963. 
                Ce système nécessite un équipage de quatre hommes capables de le transporter démonté (en position de marche) sur de longues distances. 
                Calibre : 73 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question51.webp",
            additionalImages = listOf("bm2_question51_extra1.jpg", "bm2_question51_extra2.webp", "bm2_question51_extra3.webp"),
            correct = "2A-29",
            options = listOf("122D-30", "152D-20", "2A-29", "2A-36"),
            category = "bm2",
            description = """
                Le MT-12 ou 2A-29 est un canon antichar soviétique à âme lisse de 100 mm, 
                qui a servi comme artillerie antichar tractée principale dans l’armée soviétique du début des années 1970 à la fin des années 1980. 
                Il a été largement utilisé lors de la guerre russo-ukrainienne.
                Calibre : 100 mm.
                Equipage : 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question52.webp",
            additionalImages = listOf("bm2_question52_extra1.webp", "bm2_question52_extra2.webp", "bm2_question52_extra3.webp"),
            correct = "2S1 Gvozdika",
            options = listOf("2S1 Gvozdika", "2S3 Akatsiya", "2S5 Giatsint", "2S7 Pion"),
            category = "bm2",
            description = """
                Le 2S1 Gvozdika est un canon automoteur soviétique de calibre 122 mm. 
                Il est en réalité essentiellement une version allongée du MT-LB, avec un obusier 2A-18 (D-30) monté sur une tourelle. 
                Véhicule semi-amphibie, il peut également opérer par tempête de neige et sur les marais. 
                Il est protégé contre les effets du NRBC et dispose d’une vision optique infrarouge.
                Calibre : 122 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question53.jpg",
            additionalImages = listOf("bm2_question53_extra1.jpg", "bm2_question53_extra2.jpg", "bm2_question53_extra3.jpg"),
            correct = "2S3 Akatsiya",
            options = listOf("2S1 Gvozdika", "2S3 Akatsiya", "2S5 Giatsint", "2S7 Pion"),
            category = "bm2",
            description = """
                Le 2S3 Akatsia est un canon automoteur soviétique conçu à la fin des années 1960, entré en service en 1971. 
                L’Akatsia est conçu sur le châssis GM-123 qui est également utilisé par le 2S4 Tioulpan et le 2S5 Guiatsint-S. 
                Il est compatible avec toutes les munitions de 152 mm utilisées par les canons tractés D-20, ML-20 et D-1. 
                Encore en service dans dix-huit pays.
                Calibre : 152 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question54.jpg",
            additionalImages = listOf("bm2_question54_extra1.jpg", "bm2_question54_extra2.jpg"),
            correct = "2S5 Giatsint",
            options = listOf("2S1 Gvozdika", "2S3 Akatsiya", "2S5 Giatsint", "2S7 Pion"),
            category = "bm2",
            description = """
                Le 2S5 Giatsint est un canon d’artillerie automoteur de 152 mm russe, entré en service en 1976. 
                Canon : 152 mm, 2A37. 
                Portée : 30.5 km. 
                Cadence de tir : 5-6 obus par minute.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question55.webp",
            additionalImages = listOf("bm2_question55_extra1.jpg", "bm2_question55_extra2.webp", "bm2_question55_extra3.webp"),
            correct = "2S7 Pion",
            options = listOf("2S1 Gvozdika", "2S3 Akatsiya", "2S5 Giatsint", "2S7 Pion"),
            category = "bm2",
            description = """
                Le 2S7 Pion est un canon d’artillerie automoteur russe de 203 mm, entré en service en 1975. 
                Il est apte à tirer des obus conventionnels et des obus à charge nucléaire. 
                Sa distance de tir maximale est de 47 km. 
                Le système d’arme est installé sur un châssis chenillé à sept galets. 
                Le train de roulement est dérivé du char de combat T-80. 
                Canon : 2A44, 203 mm.
                Equipage : 7 (2S7) / 6 (2S7M).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question56.jpg",
            additionalImages = listOf("bm2_question56_extra1.jpg", "bm2_question56_extra2.jpg", "bm2_question56_extra3.jpg"),
            correct = "PZH-2000",
            options = listOf("M109 Paladin", "PZH-2000", "AS-90", "2S19 Msta"),
            category = "bm2",
            description = """
                Le PzH 2000 est un canon d’artillerie automoteur allemand, entré en service en 1998. 
                Il est développé pour remplacer le canon d’artillerie automoteur M109 d’origine américaine. 
                Il est armé d’un canon de 155 mm permettant une portée de tir de 56 km. 
                Le chargement automatisé autorise une cadence de tir très élevée de 8 à 10 coups/mn. 
                Le châssis du PzH 2000 est dérivé du châssis du char de combat allemand Leopard-2. 
                Portée : 30 km / 56 km obus autopropulsé.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question57.webp",
            additionalImages = listOf("bm2_question57_extra1.webp", "bm2_question57_extra2.jpg", "bm2_question57_extra3.jpg"),
            correct = "M-109 Paladin",
            options = listOf("M-109 Paladin", "PzH 2000", "AS-90", "2S19 Msta"),
            category = "bm2",
            description = """
                Le M109 est un canon d’artillerie automoteur américain, entré en service en 1963. 
                Il est très largement exporté dans le monde. 
                Canon : 155 mm. 
                Portée M109 : 14,6 km. 
                Portée M109A5 : 22 km / 30 km obus autopropulsé. 
                Cadence de tir : 3-4 cps/min.
                Equipage : 6 (M109) / 4 (M109A6).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question58.jpg",
            additionalImages = listOf("bm2_question58_extra1.jpg", "bm2_question58_extra2.jpg", "bm2_question58_extra3.jpg"),
            correct = "2S19 Msta",
            options = listOf("2S19 Msta", "AS-90", "PZH-2000", "M109 Paladin"),
            category = "bm2",
            description = """
                Le 2S19 Msta-S est un canon d’artillerie automoteur russe, entré en service en 1989. 
                Il est armé d’un canon 2A65 de 152 mm. 
                Son châssis est dérivé du châssis du char de combat T-80. 
                Le 2S19 Msta-S est équipé d’un système de rechargement en munition installé à l’arrière du véhicule. 
                Portée : 6.5 km à 24.7 km / 28.5 km obus autopropulsé. 
                Cadence de tir : 8 obus par minute.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question59.jpg",
            additionalImages = listOf("bm2_question59_extra1.jpg", "bm2_question59_extra2.jpg", "bm2_question59_extra3.webp"),
            correct = "2S35 Koalitsiya",
            options = listOf("2S19 Msta", "2S35 Koalitsiya", "PZH-2000", "PLZ-07"),
            category = "bm2",
            description = """
                Le 2S35 Koalitsiya-SV est un canon d’artillerie automoteur russe, en développement depuis le début des années 2010. 
                Le châssis est dérivé du char T-90. 
                Pour la production à grande échelle, le système d’arme du 2S35 sera installé sur le châssis Armata. 
                Canon : 2A88 152 mm. 
                Portée : 40 km / 70 km obus autopropulsé. 
                Cadence de tir soutenue : 10 obus par minute.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question60.jpg",
            additionalImages = listOf("bm2_question60_extra1.jpg", "bm2_question60_extra2.jpg", "bm2_question60_extra3.webp"),
            correct = "2S4 Tioulpan",
            options = listOf("2S1 Gvozdika", "2S3 Akatsiya", "2S4 Tioulpan", "2S5 Giatsint"),
            category = "bm2",
            description = """
                Le 2S4 Tioulpan est un mortier automoteur lourd russe de 240 mm.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question61.jpg",
            additionalImages = listOf("bm2_question61_extra1.jpg", "bm2_question61_extra2.jpg", "bm2_question61_extra3.jpg"),
            correct = "PLZ-07",
            options = listOf("M109 Paladin", "PLZ-07", "2S3 Akatsiya", "2S35 Koalitsiya"),
            category = "bm2",
            description = """
                Le PLZ-07 est un canon d’artillerie automoteur de 122 mm chinois, développé dans les années 2000. 
                Il emploie le châssis du véhicule blindé de combat d’infanterie ZBD-04. 
                Le PLZ-07 est doté de la capacité amphibie, propulsé dans l’eau avec le mouvement des chenilles. 
                Le PLZ-07 est armé d’un canon de 122 mm dérivé du canon soviétique D-30. 
                La distance de tir maximale est de 18 km avec un obus standard et 27 km avec la munition propulsée.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question62.webp",
            additionalImages = listOf("bm2_question62_extra1.jpg", "bm2_question62_extra2.webp", "bm2_question62_extra3.jpg"),
            correct = "2A-36",
            options = listOf("2A-29", "152-D20", "2A-36", "130 M-46"),
            category = "bm2",
            description = """
                Le 2A-36 Guiatsint-B est une pièce d’artillerie moyenne utilisée par les Forces armées de la fédération de Russie, développé à partir de 1968. 
                Il existe en version canon automoteur chenillé sans tourelle 2S5 Guiatsint-S. 
                Le 2A-36 est largement utilisé par les forces armées ukrainiennes depuis 2014 contre les séparatistes du Donbass puis contre la Russie. 
                L’armée russe les remet en service en 2022 en raison d’un besoin important en pièces d’artillerie. 
                Le canon est de calibre 152 mm et est rayé. 
                La portée de tir maximale avec l’obus HE-FRAG standard est de 28,5 km et de 30 à 33 km.
                Equipage : 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question63.jpg",
            additionalImages = listOf("bm2_question63_extra1.webp", "bm2_question63_extra2.webp", "bm2_question63_extra3.webp"),
            correct = "105 M-101",
            options = listOf("105 M-101", "122 D-30", "2A-29", "2A-36"),
            category = "bm2",
            description = """
                Le M101 (105 mm) est un obusier américain de la Seconde Guerre mondiale, entré en production en série à partir de 1941. 
                Il a été (ou est encore) en service dans les armées de nombreux pays à travers le monde.
                Calibre : 105 mm.
                Equipage : 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question64.webp",
            additionalImages = listOf("bm2_question64_extra1.webp", "bm2_question64_extra2.jpg"),
            correct = "122 D-30",
            options = listOf("105 M-101", "122 D-30", "130 M-46", "152 D-20"),
            category = "bm2",
            description = """
                L’obusier D-30 de 122 mm est une pièce d’artillerie de campagne soviétique considérée comme robuste et utilisable dans des conditions difficiles, entré en service dans les années 1960.
                Calibre : 122 mm.
                Equipage : 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question65.jpg",
            additionalImages = listOf("bm2_question65_extra1.jpg", "bm2_question65_extra2.jpg", "bm2_question65_extra3.jpg"),
            correct = "130 M-46",
            options = listOf("122 D-30", "130 M-46", "152 D-20", "155 M-577"),
            category = "bm2",
            description = """
                Le canon M-46 de 130 mm est une pièce d’artillerie tractée soviétique des années 1950, 
                destinée à l’artillerie de corps d’armée.
                Calibre : 130 mm.
                Equipage : 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question66.jpg",
            additionalImages = listOf("bm2_question66_extra1.webp", "bm2_question66_extra2.jpg", "bm2_question66_extra3.webp"),
            correct = "2A-65",
            options = listOf("122 D-30", "2A-36", "152 D-20", "2A-65"),
            category = "bm2",
            description = """
                Le 2A65 Msta-B est une obusier tractée russe/soviétique de 152 mm, conçue pour détruire les véhicules blindés et les fortifications. 
                Sa partie artillerie présente une conception similaire à celle du howitzer automoteur 2S19 de même calibre.
                Calibre : 152 mm.
                Equipage : 6.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question67.jpg",
            additionalImages = listOf("bm2_question67_extra1.jpg", "bm2_question67_extra2.jpg", "bm2_question67_extra3.jpg"),
            correct = "155 M-777",
            options = listOf("122 D-30", "2A-36", "152 D-20", "155 M-777"),
            category = "bm2",
            description = """
                Le M-777 est un obusier tracté américain de calibre 155 mm. 
                Il est utilisé par les forces terrestres des États-Unis, de l’Australie, du Canada, de l’Inde, de l’Arabie saoudite et de l’Ukraine.
                Calibre : 155 mm.
                Equipage : 7.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question68.webp",
            additionalImages = listOf("bm2_question68_extra1.webp", "bm2_question68_extra2.jpg", "bm2_question68_extra3.webp"),
            correct = "152 D-20",
            options = listOf("122 D-30", "2A-36", "152 D-20", "155 M-777"),
            category = "bm2",
            description = """
                Le canon-obusier de 152 mm M1955, également connu sous le nom de D-20, 
                a été fabriqué en Union soviétique dans les années 1950.
                Calibre : 152 mm.
                Equipage : 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question69.jpg",
            additionalImages = listOf("bm2_question69_extra1.jpg", "bm2_question69_extra2.jpg", "bm2_question69_extra3.webp"),
            correct = "BM-21 Grad",
            options = listOf("BM-21 Grad", "BM-27 Uragan", "BM-30 Smerch", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le BM-21 Grad est un lance-roquette multiple de 122 mm russe, entré en service en 1963. 
                Il possède le châssis à roues 6×6 du poids-lourd Ural-375D. 
                Il est équipé de 40 tubes de lancement de roquette de 122 mm. 
                La portée maximale varie selon le type de munition entre 14 km et 20,75 km. 
                La nouvelle gamme de munition allonge la distance de tir jusqu’à 40 km. 
                La production est arrêtée en 1988.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question70.webp",
            additionalImages = listOf("bm2_question70_extra1.jpg", "bm2_question70_extra2.webp", "bm2_question70_extra3.jpg"),
            correct = "BM-30 Smerch",
            options = listOf("BM-21 Grad", "BM-27 Uragan", "BM-30 Smerch", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le BM-30 Smerch ou 9A52-2 Smerch-M est un lance-roquettes multiple lourd automoteur de 300 mm, entré en service en 1989. 
                Il est conçu pour tirer une salve complète de 12 projectiles à propergol solide. 
                Ce système est destiné à neutraliser le personnel, les blindés et les cibles non protégées dans les zones de concentration, 
                les batteries d’artillerie, les postes de commandement et les dépôts de munitions. 
                Un programme visant à le remplacer par le Tornado-S a débuté en 2018.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question71.jpg",
            additionalImages = listOf("bm2_question71_extra1.jpg", "bm2_question71_extra2.webp", "bm2_question71_extra3.jpg"),
            correct = "BM-27 Uragan",
            options = listOf("BM-21 Grad", "BM-27 Uragan", "BM-30 Smerch", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le BM-27 Uragan est un système de lance-roquettes multiples automoteur conçu en Union soviétique, entré en service à la fin des années 1970. 
                Il est capable de lancer des roquettes de 220 mm à partir de 16 tubes de lancement montés à l’arrière d’un châssis ZIL-135 8x8. 
                Une salve complète de 16 roquettes peut être tirée en 20 secondes et peut engager des cibles dans un rayon de 35 kilomètres.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question72.jpg",
            additionalImages = listOf("bm2_question72_extra1.jpg", "bm2_question72_extra2.webp", "bm2_question72_extra3.webp"),
            correct = "SCUD-B",
            options = listOf("SCUD-B", "BM-30 Smerch", "RM-70", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le SCUD-B est un missile balistique à portée intermédiaire d’origine russe, 
                capable d’emporter une charge nucléaire ou chimique, avec une portée maximale effective de 550 km.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question73.jpg",
            additionalImages = listOf("bm2_question73_extra1.jpg", "bm2_question73_extra2.jpg", "bm2_question73_extra3.jpg"),
            correct = "M-270 MLRS",
            options = listOf("SCUD-B", "BM-30 Smerch", "RM-70", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le M270 MLRS, Multiple Launch Rocket System, est un lance-roquettes multiple américain, entré en service en 1983. 
                Il est installé sur le châssis chenillé dérivé du véhicule blindé de combat d’infanterie M2 Bradley. 
                Sa cabine est protégée contre l’armement de petit calibre. 
                Il est équipé de deux blocs de six roquettes de 227 mm. 
                La production est arrêtée en 2003.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question74.jpg",
            additionalImages = listOf("bm2_question74_extra1.jpg", "bm2_question74_extra2.webp", "bm2_question74_extra3.webp"),
            correct = "RM-70 Vampir",
            options = listOf("BM-21 Grad", "BM-27 Uragan", "RM-70 Vampir", "M-270 MLRS"),
            category = "bm2",
            description = """
                Le lance-roquettes multiple RM-70 est une version de l'armée tchécoslovaque et une variante plus lourde du BM-21 Grad, 
                offrant des performances supérieures à ce système d'artillerie à saturation de zone, introduit en 1971. 
                Le RM-70 remplace le camion Ural-375D 6x6 par un châssis Tatra T813 Kolos 8x8 comme plateforme porteuse 
                pour le lanceur de 40 roquettes. 
                Le nouveau véhicule porteur offre suffisamment d'espace pour emporter un pack supplémentaire de 40 roquettes de 122 mm, 
                permettant un rechargement automatique.
                Calibre : 122 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question75.jpg",
            additionalImages = listOf(),
            correct = "82PM-41",
            options = listOf("82PM-41", "2B-11", "2A-65", "2A-29"),
            category = "bm2",
            description = """
                Le mortier 82PM-41, M-41 ou mortier de 82 mm modèle 1941 était un mortier soviétique 
                de calibre 82 millimètres développé pendant la Seconde Guerre mondiale comme mortier d'infanterie, 
                avec production commencée en 1941.
                Calibre : 82 mm.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question76.jpg",
            additionalImages = listOf("bm2_question76_extra1.webp", "bm2_question76_extra2.jpg", "bm2_question76_extra3.webp"),
            correct = "LAV-M",
            options = listOf("RCH155 Boxer", "LAV-M", "BTR-D", "RM-70 Vampir"),
            category = "bm2",
            description = """
                Le LAV-M, Light Armored Vehicle – Mortar, est un mortier automoteur américain, entré en service en 1986. 
                Il emploie le châssis du véhicule blindé de reconnaissance LAV-25. 
                L’armement est composé du mortier M252 de 81 mm. 
                Pour son auto-défense, le véhicule est équipé de la mitrailleuse M240E1 de 7,62 mm installée sur le toit. 
                Le stock de munition est de 99 obus de mortier de 81 mm et 1000 cartouches de 7,62 mm.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question77.webp",
            additionalImages = listOf("bm2_question77_extra1.webp", "bm2_question77_extra2.webp"),
            correct = "MDK-2",
            options = listOf("PZM", "PMM-2", "MDK-2", "PTS"),
            category = "bm2",
            description = """
                Le MDK-2M est un véhicule à chenilles excavateur de tranchées de l'armée soviétique.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question78.jpg",
            additionalImages = listOf("bm2_question78_extra1.webp", "bm2_question78_extra2.jpg", "bm2_question78_extra3.webp"),
            correct = "PZM",
            options = listOf("PZM", "PMM-2", "MDK-2", "PTS"),
            category = "bm2",
            description = """
                Le PZM est un véhicule à roues excavateur de tranchées de l'armée soviétique.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question79.jpg",
            additionalImages = listOf("bm2_question79_extra1.jpg", "bm2_question79_extra2.webp", "bm2_question79_extra3.jpg"),
            correct = "GMZ-3",
            options = listOf("PZM", "PMM-2", "MDK-2", "GMZ-3"),
            category = "bm2",
            description = """
                Le GMZ-3 est un poseur de mines sur chenilles soviétique. 
                Il est conçu pour la pose mécanisée de mines antichars, antipistes et anti-fonds dans le sol ou la neige 
                sur le trajet des chars et des véhicules blindés ennemis.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question80.jpg",
            additionalImages = listOf("bm2_question80_extra1.jpg", "bm2_question80_extra2.jpg"),
            correct = "UMZ-3",
            options = listOf("PZM", "UMZ-3", "MDK-2", "GMZ-3"),
            category = "bm2",
            description = """
                L’UMZ (Universalnyy Minnyy Zagraditel – Poseur de mines universel) est un véhicule du génie soviétique 
                destiné au minage à distance par projection, construit sur la base d’un camion ZIL-131.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question81.webp",
            additionalImages = listOf("bm2_question81_extra1.webp", "bm2_question81_extra2.jpg", "bm2_question81_extra3.webp"),
            correct = "UR-77",
            options = listOf("MTU-72", "UMZ-3", "IMR-2", "UR-77"),
            category = "bm2",
            description = """
                L’UR-77 « Meteorit » est un véhicule de combat soviétique, un lanceur automoteur à roquettes pour le déminage, 
                conçu sur la base de l’obusier automoteur 2S1 « Gvozdika », produit en série depuis 1978.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question82.jpg",
            additionalImages = listOf("bm2_question82_extra1.jpg", "bm2_question82_extra2.jpg", "bm2_question82_extra3.jpg"),
            correct = "M-136 Volcano",
            options = listOf("UR-77 Meteorit", "M-270 MLRS", "M-136 Volcano", "Roland-2"),
            category = "bm2",
            description = """
                Le système de mines éparpillables M136 Volcano (Vehicle-Launched Scatterable Mine System) 
                est un système automatisé de pose de mines développé par l’armée américaine dans les années 1980. 
                Le système utilise des conteneurs préchargés contenant plusieurs mines antipersonnel (AP) et/ou antichar (AT), 
                qui sont dispersées sur une large zone lors de leur éjection.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question83.jpg",
            additionalImages = listOf("bm2_question83_extra1.jpg", "bm2_question83_extra2.jpg", "bm2_question83_extra3.webp"),
            correct = "KMT-5",
            options = listOf("UR-77", "KMT-5", "TMM-3", "Roland-2"),
            category = "bm2",
            description = """
                Le KMT-5 est un chasse-mines de chars soviétique du milieu du XXᵉ siècle, 
                conçu pour la reconnaissance des champs de mines et l’ouverture de passages. 
                Ces brèches permettent ensuite le mouvement des blindés non équipés de dispositifs antimines.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question84.webp",
            additionalImages = listOf("bm2_question84_extra1.webp", "bm2_question84_extra2.webp", "bm2_question84_extra3.webp"),
            correct = "IMR-2",
            options = listOf("IMR-2", "UMZ-3", "MTU-72", "GMZ-3"),
            category = "bm2",
            description = """
                Le IMR-2 est un char du génie russe, introduit en 1982. 
                Encore en service dans les forces armées russes.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question85.jpg",
            additionalImages = listOf("bm2_question85_extra1.webp", "bm2_question85_extra2.jpg", "bm2_question85_extra3.jpg"),
            correct = "MTU-72",
            options = listOf("PTS-2", "UMZ-3", "MTU-72", "TMM-3"),
            category = "bm2",
            description = """
                Le poseur de pont blindé MTU-72 est un véhicule du génie militaire conçu pour déployer un pont d’assaut, 
                permettant aux chars et autres véhicules militaires de franchir des tranchées et des obstacles aquatiques, 
                mis en service en 1974.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question86.jpg",
            additionalImages = listOf("bm2_question86_extra1.jpg", "bm2_question86_extra2.jpg", "bm2_question86_extra3.jpg"),
            correct = "TMM-3",
            options = listOf("PTS-2", "TMM-3", "MTU-72", "TMM-6"),
            category = "bm2",
            description = """
                Le TMM-3 est conçu pour établir des franchissements sur des obstacles d’une largeur maximale de 40 mètres 
                et d’une profondeur allant jusqu’à 3 mètres, permettant le passage de véhicules à roues et à chenilles d’un poids maximal de 60 tonnes. 
                Pays : URSS/Russie.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question87.jpg",
            additionalImages = listOf("bm2_question87_extra1.jpg", "bm2_question87_extra2.jpg", "bm2_question87_extra3.jpg"),
            correct = "TMM-6",
            options = listOf("PTS-2", "TMM-3", "MTU-72", "TMM-6"),
            category = "bm2",
            description = """
                Le TMM-6 est un véhicule poseur de pont russe, développé entre 1999 et la fin des années 2000.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question88.jpg",
            additionalImages = listOf("bm2_question88_extra1.jpg", "bm2_question88_extra2.jpg", "bm2_question88_extra3.jpg"),
            correct = "PTS-2",
            options = listOf("PTS-2", "TMM-3", "GMZ", "TMM-6"),
            category = "bm2",
            description = """
                Le PTS-2 est un véhicule de transport de troupes amphibie chenillé militaire soviétique 
                capable de transporter des véhicules sur son toit et de les transporter sur l’eau à la manière d’un bac ou d’un ferry.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question89.jpg",
            additionalImages = listOf("bm2_question89_extra1.jpg", "bm2_question89_extra2.jpg", "bm2_question89_extra3.jpg"),
            correct = "Souvim",
            options = listOf("PTS-2", "TMM-3", "Souvim", "TMM-6"),
            category = "bm2",
            description = """
                Le système d’ouverture d’itinéraire miné (SOUVIM) permet de sécuriser un itinéraire faiblement pollué 
                par des mines antichar ou antipersonnel, détectables ou à pression. 
                Il a pour vocation de participer aux missions d’appui à la mobilité en assurant, 
                sur de longues distances, l’ouverture rapide d’itinéraires faiblement minés (minage de harcèlement) 
                en 2e échelon ou en zone arrière des grandes unités (zone des flux logistiques) dans un conflit de basse intensité.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question90.jpg",
            additionalImages = listOf("bm2_question90_extra1.webp"),
            correct = "1V18 Klyon",
            options = listOf("1V18 Klyon", "9K31 Strela", "9K33 Osa", "1S91 Straight Flush"),
            category = "bm2",
            description = """
                Le 1V18 Klyon est un véhicule de commandement soviétique, conçu sur la base du châssis du BTR-60.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question91.webp",
            additionalImages = listOf("bm2_question91_extra1.webp", "bm2_question91_extra2.jpg", "bm2_question91_extra3.jpg"),
            correct = "1S91 Straight Flush",
            options = listOf("9K31 Strela", "9K33 Osa", "1V18 Klyon", "1S91 Straight Flush"),
            category = "bm2",
            description = """
                Le 1S91 (code OTAN - Straight Flush) est un radar mobile de défense aérienne à longue portée d’origine soviétique, 
                actuellement en service dans les forces armées russes. 
                Conçu pour fonctionner avec le système de missiles anti-aériens 2K12 Kub, il est monté sur un châssis chenillé GM-568.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question92.webp",
            additionalImages = listOf("bm2_question92_extra1.webp", "bm2_question92_extra2.jpg", "bm2_question92_extra3.jpg"),
            correct = "RQ7 SHADOW-200",
            options = listOf("RQ7 SHADOW-200", "MQ-9 Reaper", "Falco EVO", "S-70 Okhotnik-B"),
            category = "bm2",
            description = """
                Le RQ-7A Shadow 200 est un drone de reconnaissance développé par les États-Unis. 
                Opérateurs : Australie, États-Unis, Suède, Roumanie.
                Equipage : 0.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question93.jpg",
            additionalImages = listOf("bm2_question93_extra1.jpg", "bm2_question93_extra2.jpg", "bm2_question93_extra3.jpg"),
            correct = "SU-25",
            options = listOf("SU-25", "SU-27", "SU-57", "MIG-31"),
            category = "bm2",
            description = """
                Le SU-25 Gratch est un avion d’attaque au sol, de soutien aérien rapproché et de lutte antichar développé par l’URSS dans les années 1970.
                Equipage : 1.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question94.webp",
            additionalImages = listOf("bm2_question94_extra1.jpg", "bm2_question94_extra2.jpg", "bm2_question94_extra3.jpg"),
            correct = "F-5E",
            options = listOf("Typhoon", "F-5E", "SU-25", "MIG-31"),
            category = "bm2",
            description = """
                Le Northrop F-5E est une famille d’avions de chasse légers et maniables, conçus et fabriqués par Northrop aux États-Unis, 
                avec début de fabrication en 1962.
                Equipage : 1.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question95.jpg",
            additionalImages = listOf("bm2_question95_extra1.jpg", "bm2_question95_extra2.jpg", "bm2_question95_extra3.jpg"),
            correct = "Typhoon",
            options = listOf("F-5E", "Tornado", "SU-25", "Typhoon"),
            category = "bm2",
            description = """
                Le Typhoon est un avion de chasse européen, entré en service en 2004. 
                Il est également connu sous l’appellation EF-2000. 
                C’est un chasseur bi-réacteur, multirôle, développé par le consortium Eurofighter, 
                composé du Royaume-Uni, de l’Allemagne, de l’Italie et de l’Espagne. 
                Canon : 27 mm, Mauser BK-27. 
                Pylônes externes : 8 pylônes sous aile, 5 sous fuselage, 9 000 kg d’armement. 
                Missiles air-air : AIM-9 Sidewinder, AIM-120 AMRAAM, AIM-132 AMRAAM. 
                Missiles air-sol : AGM-88 HARM, AGM-65 Maverick, Storm Shadow. 
                Bombes : Paveway II. 
                Premier vol le 27 mars 1994.
                Equipage : 1.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question96.jpg",
            additionalImages = listOf("bm2_question96_extra1.jpg", "bm2_question96_extra2.jpg", "bm2_question96_extra3.webp"),
            correct = "SA-6",
            options = listOf("SA-6", "SA-7", "SA-8", "Roland-2"),
            category = "bm2",
            description = """
                Le 2K12 Koub (code OTAN : SA-6 Gainful) est un système d’armes antiaérien équipé de missiles sol-air, 
                construit à plus de 500 exemplaires entre 1968 et 1985. 
                Il fonctionne avec un radar mobile le 1S91.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question97.jpg",
            additionalImages = listOf("bm2_question97_extra1.jpg", "bm2_question97_extra2.jpg", "bm2_question97_extra3.jpg"),
            correct = "SA-7",
            options = listOf("SA-6", "SA-7", "SA-8", "Roland-2"),
            category = "bm2",
            description = """
                Le 9K32 Strela-2, désigné par l’OTAN SA-7 « Grail », est un système de missile sol-air portatif très courte portée 
                de conception soviétique, guidé par infrarouge et doté d’une charge militaire hautement explosive.
                Equipage : 1.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question98.jpg",
            additionalImages = listOf("bm2_question98_extra1.webp", "bm2_question98_extra2.webp", "bm2_question98_extra3.webp"),
            correct = "SA-8",
            options = listOf("SA-6", "SA-7", "SA-8", "Roland-2"),
            category = "bm2",
            description = """
                Le 9K33 Osa (code OTAN : SA-8 Gecko) est un véhicule antiaérien équipé de missiles sol-air à haute mobilité 
                et de très courte portée, de conception soviétique. 
                La vitesse maximale de ses missiles selon les versions est de Mach 2,4, 
                l’altitude minimale d’engagement est de 25 mètres, la portée maximale effective en altitude est de 5 000 mètres. 
                Premier système de missiles de défense aérienne incorporant ses propres radars d’engagement sur un véhicule unique.
                Equipage : 5.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question99.webp",
            additionalImages = listOf("bm2_question99_extra1.jpg", "bm2_question99_extra2.jpg", "bm2_question99_extra3.jpg"),
            correct = "Roland-2",
            options = listOf("SA-6", "SA-7", "SA-8", "Roland-2"),
            category = "bm2",
            description = """
                Le missile franco-allemand Roland est un missile de courte portée sol-air. 
                Le système antiaérien à courte portée Roland est monté sur châssis blindé (AMX-30R pour la France, Marder pour l’Allemagne) 
                ou sur cabine aérotransportable à roues. 
                Un des rares missiles étrangers achetés par l’armée américaine.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question100.jpg",
            additionalImages = listOf("bm2_question100_extra1.jpg", "bm2_question100_extra2.jpg", "bm2_question100_extra3.jpg"),
            correct = "2S-6 Tunguska",
            options = listOf("SA-6", "SA-7", "2S-6 Tunguska", "96K6 Pantsir-S"),
            category = "bm2",
            description = """
                Le 2S-6 Tunguska (NATO : SA-19 Grison) est un système d’artillerie anti-aérienne automotrice et de missiles sol-air d’origine soviétique, 
                développé dans les années 1970. 
                Son rôle principal est de protéger les unités blindées et d’infanterie contre les hélicoptères et les avions d’attaque au sol. 
                Principaux utilisateurs : Russie, Inde.
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question101.jpg",
            additionalImages = listOf("bm2_question101_extra1.jpg", "bm2_question101_extra2.jpg", "bm2_question101_extra3.jpg"),
            correct = "AH-64",
            options = listOf("UH-1", "MI-28", "AH-64", "KA-50"),
            category = "bm2",
            description = """
                L’AH-64 Apache est un hélicoptère d’attaque américain, entré en service en 1984. 
                La version AH-64D Apache Longbow peut recevoir un radar millimétrique AN/APG-78 Longbow installé au-dessus du rotor principal. 
                La version AH-64E Apache Guardian est la plus moderne actuellement en service. 
                Canon : 30 mm M230, dotations 1 200 obus. 
                Missiles air-sol : jusqu’à 16. 
                Missiles air-air : 2. 
                Roquettes : 70 mm. 
                Largement exporté dans le monde.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question102.jpg",
            additionalImages = listOf("bm2_question102_extra1.jpg", "bm2_question102_extra2.jpg", "bm2_question102_extra3.jpg"),
            correct = "MI-8",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            category = "bm2",
            description = """
                Le Mi-8 est un hélicoptère de transport multirôle russe, entré en service en 1965. 
                Il a donné naissance au Mi-14 Haze à la capacité amphibie, aux Mi-17 et Mi-171 version exportation. 
                Le Mi-8 est très largement diffusé dans le monde. 
                Plus de 50 pays l’utilisent au sein de leurs forces armées ou en version civile.
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question103.jpg",
            additionalImages = listOf("bm2_question103_extra1.jpg", "bm2_question103_extra2.jpg", "bm2_question103_extra3.jpg"),
            correct = "MI-24",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            category = "bm2",
            description = """
                Le Mi-24 est un hélicoptère d’attaque russe, entré en service en 1972. 
                Il est conçu avec l’emploi de nombreux systèmes de l’hélicoptère de transport Mi-8. 
                Il peut recevoir une large panoplie d’armements : roquette, missile et canon en nacelle. 
                Le Mi-24 peut embarquer un groupe de huit fantassins dans le compartiment derrière le poste de pilotage. 
                La première version Mi-24A possède la cabine à trois membres d’équipage en configuration 1 + 2. 
                A partir du modèle Mi-24B, elle est remplacée par une cabine en tandem. 
                Mitrailleuse : Yak-B rotative 4 x 12.7 mm. 
                Canon : GSh-23L 23 mm, GSh-30K 30 mm. 
                Roquettes : S-5, S-8, S-13, S-24. 
                Missiles air-sol : Shturm-V, Ataka-M. 
                Missiles air-air : Igla-V, R-60M, R-63V. 
                Plus de 3 500 exemplaires produits, tous modèles confondus.
                Equipage : 2 + 8.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question104.jpg",
            additionalImages = listOf("bm2_question104_extra1.jpg", "bm2_question104_extra2.jpg", "bm2_question104_extra3.jpg"),
            correct = "MI-28",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            category = "bm2",
            description = """
                Le Mi-28 (Havoc – code OTAN) est un hélicoptère d’attaque russe, entré en service en 2009. 
                Canon : 30 mm 2A42.
                Equipage : 2.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question105.jpg",
            additionalImages = listOf("bm2_question105_extra1.jpg", "bm2_question105_extra2.jpg", "bm2_question105_extra3.jpg"),
            correct = "KA-50",
            options = listOf("KA-27", "MI-28", "KA-50", "KA-52"),
            category = "bm2",
            description = """
                Le Ka-50 Black Shark (Hokum-A en dénomination OTAN) est un hélicoptère de reconnaissance et d’attaque russe, 
                entré en service en 1995. 
                Premier vol le 17 juin 1982.
                Equipage : 1.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question106.jpg",
            additionalImages = listOf("bm2_question106_extra1.jpg", "bm2_question106_extra2.jpg", "bm2_question106_extra3.jpg"),
            correct = "UH-1",
            options = listOf("UH-1", "H225-M", "UH-60", "Caracal"),
            category = "bm2",
            description = """
                Le UH-1, plus connu sous le nom de Huey, est un hélicoptère militaire américain, déployé au Vietnam à partir de 1963. 
                Les Huey étaient utilisés pour les évacuations médicales (MedEvac), le commandement et contrôle, 
                les assauts aériens, le transport de troupes et de matériel, et comme hélicoptères de combat armés (gunships).
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "bm2_question107.jpg",
            additionalImages = listOf("bm2_question107_extra1.jpg", "bm2_question107_extra2.jpg", "bm2_question107_extra3.jpg"),
            correct = "UH-60",
            options = listOf("UH-1", "H225-M", "UH-60", "Caracal"),
            category = "bm2",
            description = """
                L’UH-60 Black Hawk est un hélicoptère de transport multirôle américain, entré en service en 1979. 
                Il est dérivé en de très nombreuses versions : pour les Forces spéciales, navalisé pour la Marine, 
                pour les gardes-côtes, les missions de recherche et de sauvetage, l’évacuation médicale, 
                le transport VIP et les versions pour le transport de matériel spécifique : radar, C3 Command-Control-Communication, armement. 
                Produit à plus de quatre mille exemplaires pour une trentaine de pays.
                Equipage : 2 + 11.
            """.trimIndent(),
            moreInfo = null
        )
    )
}

