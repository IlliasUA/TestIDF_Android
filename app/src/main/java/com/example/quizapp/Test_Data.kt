package com.example.quizapp

object Test_Data {
    val QUESTION = listOf(
        Question(
            image = "char1.jpg",
            additionalImages = listOf("char1_extra1.jpg", "char1_extra2.jpg"),
            correct = "Abrams",
            options = listOf("Abrams", "Leopard-2", "Challenger-2", "M-109 Paladin"),
            category = "chars",
            description = """
                Le M1 Abrams est un char de combat américain, développé dans les années 1970. 
                Il est entré en service en 1980. 
                Les premiers modèles du M1 Abrams étaient dotés d’un canon M68 de 105 mm. 
                À partir de la version M1A1, il est armé du canon M256 de 120 mm. 
                Canon : 120 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char2.jpg",
            additionalImages = listOf("char2_extra1.jpg", "char2_extra2.jpg"),
            correct = "Ariete",
            options = listOf("Leclerc", "Leopard-1", "Ariete", "T-14"),
            category = "chars",
            description = """
                Le C1 Ariete est un char de combat italien, développé dans les années 1980-1990. 
                Il est entré en service en 1995. 
                Canon : 120 mm. 
                Mitrailleuses : 12,7 mm, 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char3.jpg",
            additionalImages = listOf("char3_extra1.jpg", "char3_extra2.jpg", "char3_extra3.jpg"),
            correct = "Challenger-1",
            options = listOf("Chieftain", "Abrams", "M-60", "Challenger-1"),
            category = "chars",
            description = """
                Le Challenger 1 est un char de combat britannique, développé dans les années 1980. 
                Il est entré en service en 1983. 
                La modernisation du Challenger 1 a donné naissance au Challenger 2, mis en service en 1998. 
                Au Royaume-Uni, le Challenger 1 a été retiré du service avec l’arrivée du Challenger 2. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char4.jpg",
            additionalImages = listOf("char4_extra1.jpg", "char4_extra2.jpg", "char4_extra3.jpg"),
            correct = "Challenger-2",
            options = listOf("Leclerc", "Leopard-2", "Challenger-2", "Abrams"),
            category = "chars",
            description = """
                Le Challenger 2 est un char de combat britannique, développé dans les années 1990. 
                Il est entré en service en 1998. 
                Connu sous l’appellation FV4030, il est le char principal de l’armée britannique. 
                Il a participé aux engagements au Kosovo et en Irak. 
                Exporté à Oman en 38 exemplaires. 
                En 2021, l’armée britannique a annoncé la modernisation de 148 chars sous l’appellation Challenger 3. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char5.jpg",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.jpg"),
            correct = "Chieftain",
            options = listOf("Challenger-1", "Chieftain", "Leopard-1", "Challenger-2"),
            category = "chars",
            description = """
                Le Chieftain est un char de combat britannique, développé dans les années 1950-1960 par Leyland Motors. 
                Il est entré en service en 1966. 
                Il a suivi plusieurs campagnes de modernisation jusqu’à la version Chieftain Mk-11. 
                Employé dans les unités blindées britanniques jusqu’à son remplacement par le Challenger 1 en 1983. 
                Exporté en Iran, Irak, Koweït, Jordanie (sous l’appellation Khalid) et Oman (sous l’appellation Shir). 
                Versions : Chieftain Mk.1 (1966), Chieftain Mk.5 (1972), Khalid, Shir. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char6.jpg",
            additionalImages = listOf("char6_extra1.jpg", "char6_extra2.jpg"),
            correct = "Leopard-2",
            options = listOf("Leopard-2", "PZH-2000", "Leopard-1", "Abrams"),
            category = "chars",
            description = """
                Le Leopard 2 est un char de combat allemand, développé dans les années 1970. 
                Versions : Leopard 2A0 (1979), Leopard 2A1 (1982), Leopard 2A2 (1983), Leopard 2A3 (1984), 
                Leopard 2A4 (1985), Leopard 2A5 (2000), Leopard 2A6 (2001), Leopard 2A7 (2014), 
                Leopard 2A7V (2021), Leopard 2A8 (2023). 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char7.jpg",
            additionalImages = listOf("char7_extra1.jpg", "char7_extra2.jpg", "char7_extra3.jpg"),
            correct = "Leopard-1",
            options = listOf("Ariete", "Tigre", "Panther", "Leopard-1"),
            category = "chars",
            description = """
                Le Leopard 1 est un char de combat allemand, développé dans les années 1950-1960. 
                Il est entré en service en 1965. 
                Largement exporté, il est actuellement en service en Turquie, au Brésil et au Chili. 
                Depuis février 2023, l’Allemagne livre des Leopard 1 à l’Ukraine. 
                Le châssis du Leopard 1 a servi de base pour divers véhicules de soutien. 
                Versions : Leopard 1A1, 1A1A1, 1A2, 1A3, 1A4, 1A5. 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char8.jpg",
            additionalImages = listOf("char8_extra1.jpg", "char8_extra2.jpg", "char8_extra3.jpg"),
            correct = "T-55",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-55 est un char de combat russe, développé en Union soviétique dans les années 1950. 
                Il est entré en service en 1958. 
                Très largement diffusé, il a été produit sous licence en Pologne, Tchécoslovaquie et Roumanie. 
                Il a participé à de nombreux conflits et reste en service dans plusieurs forces armées. 
                Versions : T-55K, T-55A, T-55AK, T-55AM. 
                Canon : 100 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char9.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("M-60", "AMX-30", "Magach-7", "T-64"),
            category = "chars",
            description = """
                Le M60 est un char de combat américain, développé dans les années 1950. 
                Très largement exporté, il reste en service dans plusieurs forces armées. 
                Il a suivi plusieurs campagnes de modernisation : M60A1 (1962), M60A2 (1970), M60A3 (1975). 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm Browning M2. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char10.jpg",
            additionalImages = listOf("char10_extra1.jpg", "char10_extra2.jpg"),
            correct = "Magach-7",
            options = listOf("Merkava-3", "Magach-7", "M-60", "Chieftain"),
            category = "chars",
            description = """
                Les chars Magach sont des versions adaptées aux besoins israéliens des chars américains M48 et M60. 
                Versions : Magach 5, Magach 6B Gal Batash, Magach 6M Tadach, Magach 7A, Magach 7C. 
                Canon : 105 mm. 
                Mitrailleuses : 7,62 mm, 12,7 mm Browning. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char11.jpg",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-3", "Merkava-4", "Magach-7", "Abrams"),
            category = "chars",
            description = """
                Le Merkava Mk IV est un char de combat israélien, développé dans les années 1990. 
                Il est entré en service en 2004. 
                Son châssis a servi de base pour le véhicule blindé de transport de troupes lourd Namer. 
                Israël est l’unique utilisateur du Merkava Mk IV. 
                Canon : 120 mm. 
                Mitrailleuses : 7,62 mm, 12,7 mm. 
                Mortier : 60 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char12.jpg",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "M-60"),
            category = "chars",
            description = """
                Le Merkava Mk IV est un char de combat israélien, développé dans les années 1990. 
                Il est entré en service en 2004. 
                Son châssis a servi de base pour le véhicule blindé de transport de troupes lourd Namer. 
                Israël est l’unique utilisateur du Merkava Mk IV. 
                Canon : 120 mm. 
                Mitrailleuses : 7,62 mm, 12,7 mm. 
                Mortier : 60 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char13.jpg",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-62 est un char moyen soviétique, entré en service en 1961. 
                Canon : 115 mm. 
                Equipage : 4 (chef de char, opérateur tourelle, chargeur, pilote).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char14.jpg",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-64 est un char de combat soviétique, développé dans les années 1960. 
                Il est entré en service en 1965. 
                Premier char soviétique à intégrer le chargement automatique. 
                Versions : T-64A, T-64B, T-64BM Bulat. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Missile antichar : AT-8 (T-64B). 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char15.jpg",
            additionalImages = listOf("char15_extra1.jpg", "char15_extra2.jpg", "char15_extra3.jpg", "char15_extra4.jpg"),
            correct = "T-72",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-72 est un char de combat russe, développé dans les années 1960-1970. 
                Il est entré en service en 1973. 
                Versions : T-72, T-72K, T-72A, T-72M, T-72AV, T-72B, T-72B1, T-72BK, T-72C, T-72BM, T-72B3. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char16.jpg",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-80 est un char de combat russe, développé dans les années 1980. 
                Versions : T-80U, T-80UK, T-80UD, T-80UE-1. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char17.jpg",
            additionalImages = listOf("char17_extra1.jpg", "char17_extra2.jpg", "char17_extra3.jpg"),
            correct = "T-90",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-90 est un char de combat russe, développé dans les années 1990. 
                Il est entré en service en 1992. 
                Versions : T-90A, T-90M, T-90MS. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char18.jpg",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("M-60", "Chieftain", "Challenger-1", "AMX-30"),
            category = "chars",
            description = """
                L’AMX-30 est un char de combat français, développé dans les années 1960. 
                Il est entré en service en 1967. 
                Près de 3 500 chars AMX-30 ont été produits pour l’armée française et pour l’exportation. 
                Remplacé par le char Leclerc dans l’armée française. 
                Versions : AMX-30B (1967), AMX-30B2 (1980). 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm ANF1. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char19.jpg",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("AMX-30", "AMX-10RC", "Leclerc", "M-60"),
            category = "chars",
            description = """
                L’AMX-30 est un char de combat français, développé dans les années 1960. 
                Il est entré en service en 1967. 
                Près de 3 500 chars AMX-30 ont été produits pour l’armée française et pour l’exportation. 
                Remplacé par le char Leclerc dans l’armée française. 
                Versions : AMX-30B (1967), AMX-30B2 (1980). 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm ANF1. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char20.jpg",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("Magach-7", "AMX-30", "Abrams", "M-60"),
            category = "chars",
            description = """
                L’AMX-30 est un char de combat français, développé dans les années 1960. 
                Il est entré en service en 1967. 
                Près de 3 500 chars AMX-30 ont été produits pour l’armée française et pour l’exportation. 
                Remplacé par le char Leclerc dans l’armée française. 
                Versions : AMX-30B (1967), AMX-30B2 (1980). 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm ANF1. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char21.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Magach-7", "M-60"),
            category = "chars",
            description = """
                Le M60 est un char de combat américain, développé dans les années 1950. 
                Très largement exporté, il reste en service dans plusieurs forces armées. 
                Modernisations : M60A1 (1962), M60A2 (1970), M60A3 (1975). 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm Browning M2. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char22.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Leopard-1", "M-60"),
            category = "chars",
            description = """
                Le M60 est un char de combat américain, développé dans les années 1950. 
                Très largement exporté, il reste en service dans plusieurs forces armées. 
                Modernisations : M60A1 (1962), M60A2 (1970), M60A3 (1975). 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm Browning M2. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char23.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Leopard-1", "M-60"),
            category = "chars",
            description = """
                Le M60 est un char de combat américain, développé dans les années 1950. 
                Très largement exporté, il reste en service dans plusieurs forces armées. 
                Modernisations : M60A1 (1962), M60A2 (1970), M60A3 (1975). 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm Browning M2. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char24.jpg",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-80 est un char de combat russe, développé dans les années 1980. 
                Versions : T-80U, T-80UK, T-80UD, T-80UE-1. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char25.jpg",
            additionalImages = listOf("char6_extra1.jpg", "char6_extra2.jpg"),
            correct = "Leopard-2",
            options = listOf("Leclerc", "Leopard-2", "Leopard-1", "Ariete"),
            category = "chars",
            description = """
                Le Leopard 2 est un char de combat allemand, développé dans les années 1970. 
                Versions : Leopard 2A0 (1979), Leopard 2A1 (1982), Leopard 2A2 (1983), Leopard 2A3 (1984), 
                Leopard 2A4 (1985), Leopard 2A5 (2000), Leopard 2A6 (2001), Leopard 2A7 (2014), 
                Leopard 2A7V (2021), Leopard 2A8 (2023). 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char26.jpg",
            additionalImages = listOf("char2_extra1.jpg", "char2_extra2.jpg"),
            correct = "Ariete",
            options = listOf("Leclerc", "Leopard-2", "Leopard-1", "Ariete"),
            category = "chars",
            description = """
                Le C1 Ariete est un char de combat italien, développé dans les années 1980-1990. 
                Il est entré en service en 1995. 
                Canon : 120 mm. 
                Mitrailleuses : 12,7 mm, 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char27.jpg",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = """
                Le T-80 est un char de combat russe, développé dans les années 1980. 
                Versions : T-80U, T-80UK, T-80UD, T-80UE-1. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char28.jpg",
            additionalImages = listOf("char28_extra1.jpg", "char28_extra2.jpg"),
            correct = "Arjun MK1",
            options = listOf("Arjun MK1", "TYPE-98", "TYPE-99", "K-1"),
            category = "chars",
            description = """
                L’Arjun est un char de combat indien, développé dans les années 1990-2000. 
                Il est entré en service en 2004. 
                Canon : 120 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char29.jpg",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.jpg"),
            correct = "Chieftain",
            options = listOf("Challenger-1", "Leopard-1", "Leopard-2", "Chieftain"),
            category = "chars",
            description = """
                Le Chieftain est un char de combat britannique, développé dans les années 1950-1960 par Leyland Motors. 
                Il est entré en service en 1966. 
                Il a suivi plusieurs campagnes de modernisation jusqu’à la version Chieftain Mk-11. 
                Employé dans les unités blindées britanniques jusqu’à son remplacement par le Challenger 1 en 1983. 
                Exporté en Iran, Irak, Koweït, Jordanie (sous l’appellation Khalid) et Oman (sous l’appellation Shir). 
                Versions : Chieftain Mk.1 (1966), Chieftain Mk.5 (1972), Khalid, Shir. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char30.jpg",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.jpg"),
            correct = "Chieftain",
            options = listOf("Chieftain", "Abrams", "Leclerc", "Challenger-1"),
            category = "chars",
            description = """
                Le Chieftain est un char de combat britannique, développé dans les années 1950-1960 par Leyland Motors. 
                Il est entré en service en 1966. 
                Il a suivi plusieurs campagnes de modernisation jusqu’à la version Chieftain Mk-11. 
                Employé dans les unités blindées britanniques jusqu’à son remplacement par le Challenger 1 en 1983. 
                Exporté en Iran, Irak, Koweït, Jordanie (sous l’appellation Khalid) et Oman (sous l’appellation Shir). 
                Versions : Chieftain Mk.1 (1966), Chieftain Mk.5 (1972), Khalid, Shir. 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char31.jpg",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1",
            options = listOf("Arjun MK1", "K-1", "TYPE-99", "Abrams"),
            category = "chars",
            description = """
                Le K1 est un char de combat sud-coréen, développé dans les années 1970-1980 par Hyundai (aujourd’hui Rotem) avec l’assistance technique de General Dynamics. 
                Il est entré en service en 1987. 
                Connu sous les appellations Type 88 et 88-Tank, il a été développé pour contrer les chars T-62 nord-coréens. 
                Sa production a été remplacée par le K2 Black Panther à partir de 2014. 
                Versions : K1/Type 88/88-Tank (1987), K1A1 (2001), K1A2 (2013). 
                Canon : 105 mm (K1), 120 mm (K1A1). 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char32.jpg",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1",
            options = listOf("Arjun MK1", "K-1", "TYPE-99", "Abrams"),
            category = "chars",
            description = """
                Le K1 est un char de combat sud-coréen, développé dans les années 1970-1980 par Hyundai (aujourd’hui Rotem) avec l’assistance technique de General Dynamics. 
                Il est entré en service en 1987. 
                Connu sous les appellations Type 88 et 88-Tank, il a été développé pour contrer les chars T-62 nord-coréens. 
                Sa production a été remplacée par le K2 Black Panther à partir de 2014. 
                Versions : K1/Type 88/88-Tank (1987), K1A1 (2001), K1A2 (2013). 
                Canon : 105 mm (K1), 120 mm (K1A1). 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char33.jpg",
            additionalImages = listOf("char33_extra1.jpg", "char33_extra2.jpg"),
            correct = "Merkava-3",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "Challenger-2"),
            category = "chars",
            description = """
                Le Merkava Mk 3 est un char de combat israélien, entré en service en 1990. 
                Canon : 120 mm MG251 (lisse). 
                Mitrailleuses : trois 7,62 mm MAG. 
                Equipage : 4 (chef de char, pilote, tireur, chargeur).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char34.jpg",
            additionalImages = listOf("char33_extra1.jpg", "char33_extra2.jpg"),
            correct = "Merkava-3",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "Challenger-2"),
            category = "chars",
            description = """
                Le Merkava Mk 3 est un char de combat israélien, entré en service en 1990. 
                Canon : 120 mm MG251 (lisse). 
                Mitrailleuses : trois 7,62 mm MAG. 
                Equipage : 4 (chef de char, pilote, tireur, chargeur).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char35.jpg",
            additionalImages = listOf("char35_extra1.jpg", "char35_extra2.jpg", "char35_extra3.jpg"),
            correct = "PT-91",
            options = listOf("ZTZ-99", "TYPE-98", "PT-91", "T-64"),
            category = "chars",
            description = """
                Le PT-91 Twardy est un char de combat polonais, développé dans les années 1990 par Bumar Labedy à partir du T-72M1. 
                Il est entré en service en 1995. 
                Produit en 232 exemplaires pour l’armée polonaise et exporté en Malaisie (48 exemplaires sous le nom PT-91M Pendekar, 2007-2010). 
                Canon : 125 mm. 
                Mitrailleuses : 7,62 mm (coaxiale), 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char36.jpg",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-62 est un char moyen soviétique, entré en service en 1961. 
                Canon : 115 mm. 
                Equipage : 4 (chef de char, opérateur tourelle, chargeur, pilote).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char37.jpg",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-64 est un char de combat soviétique, développé dans les années 1960. 
                Il est entré en service en 1965. 
                Premier char soviétique à intégrer le chargement automatique. 
                Après la première série avec un canon de 115 mm, le T-64A est équipé d’un canon de 125 mm. 
                Le T-64B peut tirer des missiles antichars 9K112-1 Cobra (AT-8 Songster). 
                Versions : T-64A, T-64B, T-64BM Bulat. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char38.jpg",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-64 est un char de combat soviétique, développé dans les années 1960. 
                Il est entré en service en 1965. 
                Premier char soviétique à intégrer le chargement automatique. 
                Après la première série avec un canon de 115 mm, le T-64A est équipé d’un canon de 125 mm. 
                Le T-64B peut tirer des missiles antichars 9K112-1 Cobra (AT-8 Songster). 
                Versions : T-64A, T-64B, T-64BM Bulat. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char39.jpg",
            additionalImages = listOf("char39_extra1.jpg", "char39_extra2.jpg", "char39_extra3.jpg", "char39_extra4.jpg"),
            correct = "TYPE-10",
            options = listOf("Leopard-2", "K-1", "TYPE-10", "Abrams"),
            category = "chars",
            description = """
                Le Type 10 est un char de combat japonais, développé dans les années 2000 par Mitsubishi Heavy Industries. 
                Il est entré en service en 2012. 
                Canon : 120 mm (44 calibres, 36 obus). 
                Mitrailleuses : 7,62 mm (coaxiale), 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char40.jpg",
            additionalImages = listOf("char40_extra1.jpg", "char40_extra2.jpg", "char40_extra3.jpg"),
            correct = "TYPE-98",
            options = listOf("T-72", "T-80", "TYPE-98", "TYPE-99"),
            category = "chars",
            description = """
                Le Type 98 (ZTZ-98) est un char de combat chinois, introduit en 1999 lors d’un défilé militaire à Pékin. 
                Ressemblant au T-72, il se distingue par une tourelle soudée. 
                Canon : 125 mm (âme lisse). 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char41.jpg",
            additionalImages = listOf("char40_extra1.jpg", "char40_extra2.jpg", "char40_extra3.jpg"),
            correct = "TYPE-98",
            options = listOf("T-80", "TYPE-98", "T-72", "TYPE-99"),
            category = "chars",
            description = """
                Le Type 98 (ZTZ-98) est un char de combat chinois, introduit en 1999 lors d’un défilé militaire à Pékin. 
                Ressemblant au T-72, il se distingue par une tourelle soudée. 
                Canon : 125 mm (âme lisse). 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char42.jpg",
            additionalImages = listOf("char42_extra1.jpg", "char42_extra2.jpg", "char42_extra3.jpg", "char42_extra4.jpg"),
            correct = "TYPE-99",
            options = listOf("T-72", "TYPE-10", "TYPE-98", "TYPE-99"),
            category = "chars",
            description = """
                Le ZTZ-99 (Type 99) est un char de combat chinois, développé dans les années 1990-2000. 
                Il est entré en service en 2003. 
                Équipé d’une caméra thermique, d’une lunette panoramique stabilisée avec capacité hunter-killer et d’un système de contre-mesures laser tout azimut. 
                Versions : ZTZ-99G, ZTZ-99A1, ZTZ-99A2. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char43.jpg",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-1", "Merkava-2", "Merkava-3", "Merkava-4"),
            category = "chars",
            description = """
                Le Merkava Mk IV est un char de combat israélien, développé dans les années 1990. 
                Il est entré en service en 2004. 
                Son châssis a servi de base pour le véhicule blindé de transport de troupes lourd Namer. 
                Israël est l’unique utilisateur du Merkava Mk IV. 
                Canon : 120 mm. 
                Mitrailleuses : 7,62 mm, 12,7 mm. 
                Mortier : 60 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char44.jpg",
            additionalImages = listOf("char44_extra1.jpg", "char44_extra2.jpg", "char44_extra3.jpg"),
            correct = "Merkava-1",
            options = listOf("Merkava-1", "Merkava-2", "Merkava-3", "Merkava-4"),
            category = "chars",
            description = """
                Le Merkava Mk 1 est un char de combat israélien, livré à l’armée israélienne (Tsahal) à partir de 1979. 
                Supérieur au T-72 syrien. 
                Versions : Merkava 1, Merkava 1B. 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm (coaxiale). 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char45.jpg",
            additionalImages = listOf("char39_extra1.jpg", "char39_extra2.jpg", "char39_extra3.jpg", "char39_extra4.jpg"),
            correct = "TYPE-10",
            options = listOf("TYPE-10", "K-1", "Leclerc", "Arjun MK1"),
            category = "chars",
            description = """
                Le Type 10 est un char de combat japonais, développé dans les années 2000 par Mitsubishi Heavy Industries. 
                Il est entré en service en 2012. 
                Canon : 120 mm (44 calibres, 36 obus). 
                Mitrailleuses : 7,62 mm (coaxiale), 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char46.jpg",
            additionalImages = listOf("char7_extra1.jpg", "char7_extra2.jpg"),
            correct = "Leopard-1",
            options = listOf("TYPE-10", "Challenger-1", "Leopard-1", "Leopard-2"),
            category = "chars",
            description = """
                Le Leopard 1 est un char de combat allemand, développé dans les années 1950-1960. 
                Il est entré en service en 1965. 
                Largement exporté, il est actuellement en service en Turquie, au Brésil et au Chili. 
                Depuis février 2023, l’Allemagne livre des Leopard 1 à l’Ukraine. 
                Le châssis du Leopard 1 a servi de base pour divers véhicules de soutien. 
                Versions : Leopard 1A1, 1A1A1, 1A2, 1A3, 1A4, 1A5. 
                Canon : 105 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char47.jpg",
            additionalImages = listOf("char6_extra1.jpg", "char6_extra2.jpg"),
            correct = "Leopard-2A7",
            options = listOf("Leclerc", "K-1", "M1A1 Abrams", "Leopard-2A7"),
            category = "chars",
            description = """
                Le Leopard 2 est un char de combat allemand, développé dans les années 1970. 
                Versions : Leopard 2A0 (1979), Leopard 2A1 (1982), Leopard 2A2 (1983), Leopard 2A3 (1984), 
                Leopard 2A4 (1985), Leopard 2A5 (2000), Leopard 2A6 (2001), Leopard 2A7 (2014), 
                Leopard 2A7V (2021), Leopard 2A8 (2023). 
                Canon : 120 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char48.jpg",
            additionalImages = listOf("char8_extra1.jpg", "char8_extra2.jpg", "char8_extra3.jpg"),
            correct = "T-55",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-55 est un char de combat russe, développé en Union soviétique dans les années 1950. 
                Il est entré en service en 1958. 
                Très largement diffusé, il a été produit sous licence en Pologne, Tchécoslovaquie et Roumanie. 
                Il a participé à de nombreux conflits et reste en service dans plusieurs forces armées. 
                Versions : T-55K, T-55A, T-55AK, T-55AM. 
                Canon : 100 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char49.jpg",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-62 est un char moyen soviétique, entré en service en 1961. 
                Canon : 115 mm. 
                Equipage : 4 (chef de char, opérateur tourelle, chargeur, pilote).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char50.jpg",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = """
                Le T-62 est un char moyen soviétique, entré en service en 1961. 
                Canon : 115 mm. 
                Equipage : 4 (chef de char, opérateur tourelle, chargeur, pilote).
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char51.jpg",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-62", "T-64", "T-72", "T-80"),
            category = "chars",
            description = """
                Le T-64 est un char de combat soviétique, développé dans les années 1960. 
                Il est entré en service en 1965. 
                Premier char soviétique à intégrer le chargement automatique. 
                Après la première série avec un canon de 115 mm, le T-64A est équipé d’un canon de 125 mm. 
                Le T-64B peut tirer des missiles antichars 9K112-1 Cobra (AT-8 Songster). 
                Versions : T-64A, T-64B, T-64BM Bulat. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char52.jpg",
            additionalImages = listOf("char52_extra1.jpg", "char52_extra2.jpg", "char52_extra3.jpg", "char52_extra4.jpg"),
            correct = "AMX-13",
            options = listOf("AMX-13", "AMX-10RC", "AMX-30", "Scorpion"),
            category = "chars",
            description = """
                L’AMX-13 est un char léger français, produit entre 1953 et 1985. 
                Canon : 105 mm (automoteur). 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char53.jpg",
            additionalImages = listOf("char53_extra1.jpg", "char53_extra2.jpg", "char53_extra3.jpg", "char53_extra4.jpg"),
            correct = "K-2 Black Panther",
            options = listOf("K-2 Black Panther", "K-1 Type 88", "M1A1 Abrams", "Challenger-2"),
            category = "chars",
            description = """
                Le K2 Black Panther est un char de combat sud-coréen, développé dans les années 2000. 
                Il est entré en service en 2014. 
                Canon : 120 mm (55 calibres). 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char54.jpg",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1 Type 88",
            options = listOf("Arjun MK1", "Leclerc", "K-1 Type 88", "K-2 Black Panther"),
            category = "chars",
            description = """
                Le K1 est un char de combat sud-coréen, développé dans les années 1970-1980 par Hyundai (aujourd’hui Rotem) avec l’assistance technique de General Dynamics. 
                Il est entré en service en 1987. 
                Connu sous les appellations Type 88 et 88-Tank, il a été développé pour contrer les chars T-62 nord-coréens. 
                Sa production a été remplacée par le K2 Black Panther à partir de 2014. 
                Versions : K1/Type 88/88-Tank (1987), K1A1 (2001), K1A2 (2013). 
                Canon : 105 mm (K1), 120 mm (K1A1). 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char55.jpg",
            additionalImages = listOf("char55_extra1.jpg", "char55_extra2.jpg", "char55_extra3.jpg", "char55_extra3.jpg"),
            correct = "Leclerc",
            options = listOf("K-2 Black Panther", "M1A1 Abrams", "Leclerc", "Challenger-2"),
            category = "chars",
            description = """
                Le Leclerc est un char de combat français, développé dans les années 1980. 
                Il est entré en service en 1991. 
                Il a remplacé les chars AMX-30 dans les unités blindées de l’armée française. 
                388 exemplaires ont été livrés aux Émirats arabes unis entre 2000 et 2003. 
                Déployé au Kosovo et au Liban. 
                Canon : 120 mm. 
                Mitrailleuses : 12,7 mm (coaxiale), 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char56.jpg",
            additionalImages = listOf("char56_extra1.jpg", "char56_extra2.jpg", "char56_extra3.jpg"),
            correct = "TR-85",
            options = listOf("VT-4", "TR-85", "TYPE-98", "ZTZ-99"),
            category = "chars",
            description = """
                Le TR-85, Tanc Românesc model 1985, est un char de combat roumain, développé dans les années 1980. 
                Il est entré en service en 1986. 
                Développé à partir du TR-580, une version allongée du T-55, il est utilisé exclusivement par la Roumanie. 
                Versions : TR-85, TR-85M1 Bizonul. 
                Canon : 100 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char57.jpg",
            additionalImages = listOf("char57_extra1.jpg", "char57_extra2.jpg", "char57_extra3.jpg", "char57_extra4.jpg"),
            correct = "VT-4",
            options = listOf("VT-4", "TR-85", "TYPE-98", "ZTZ-99"),
            category = "chars",
            description = """
                Le VT-4 (MBT-3000) est un char de combat chinois, présenté en 2012. 
                En 2016, l’armée thaïlandaise a acquis 28 chars VT-4. 
                Canon : 125 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char58.jpg",
            additionalImages = listOf("char58_extra1.jpg"),
            correct = "Jaguar",
            options = listOf("K-2 Black Panther", "Jaguar", "Griffon", "Serval"),
            category = "chars",
            description = """
                L’EBRC Jaguar, Engin Blindé de Reconnaissance et de Combat, est un véhicule blindé de combat à roues 6×6 français, développé dans les années 2010. 
                Il est entré en service en 2022. 
                Destiné à remplacer les AMX-10RCR. 
                Canon : 40 mm. 
                Mitrailleuse : 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char59.jpg",
            additionalImages = listOf("char59_extra1.jpg", "char59_extra2.jpg", "char59_extra3.jpg"),
            correct = "Strv-103",
            options = listOf("Strv-103", "Scorpion", "Wiesel", "Merkava-1"),
            category = "chars",
            description = """
                Le Stridsvagn 103 (Strv 103) est un char de combat suédois, développé dans les années 1950. 
                Parfaitement amphibie avec une jupe de flottaison installable en 20 minutes, il est propulsé dans l’eau par ses chenilles à 6 km/h. 
                Versions : Strv 103A, 103B, 103C, 103D. 
                Canon : 105 mm. 
                Mitrailleuses : deux 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char60.jpg",
            additionalImages = listOf("char59_extra1.jpg", "char59_extra2.jpg", "char59_extra3.jpg"),
            correct = "Strv-103",
            options = listOf("Strv-103", "Scorpion", "Wiesel", "Merkava-1"),
            category = "chars",
            description = """
                Le Stridsvagn 103 (Strv 103) est un char de combat suédois, développé dans les années 1950. 
                Parfaitement amphibie avec une jupe de flottaison installable en 20 minutes, il est propulsé dans l’eau par ses chenilles à 6 km/h. 
                Versions : Strv 103A, 103B, 103C, 103D. 
                Canon : 105 mm. 
                Mitrailleuses : deux 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char61.jpg",
            additionalImages = listOf("char61_extra1.jpg", "char61_extra2.jpg", "char61_extra3.jpg"),
            correct = "T-14 Armata",
            options = listOf("VT-4", "TR-85", "T-14 Armata", "K-2 Black Panther"),
            category = "chars",
            description = """
                Le T-14 Armata est un char de combat russe, en développement depuis les années 2010. 
                La production en série a été annoncée pour 2019. 
                Équipé d’une tourelle inhabitée téléopérée avec les munitions stockées dans un chargeur automatique. 
                Les trois membres d’équipage sont placés à l’avant du châssis. 
                Canon : 125 mm. 
                Mitrailleuses : 12,7 mm ou 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char62.jpg",
            additionalImages = listOf("char61_extra1.jpg", "char61_extra2.jpg", "char61_extra3.jpg"),
            correct = "T-14 Armata",
            options = listOf("VT-4", "TR-85", "T-14 Armata", "K-2 Black Panther"),
            category = "chars",
            description = """
                Le T-14 Armata est un char de combat russe, en développement depuis les années 2010. 
                La production en série a été annoncée pour 2019. 
                Équipé d’une tourelle inhabitée téléopérée avec les munitions stockées dans un chargeur automatique. 
                Les trois membres d’équipage sont placés à l’avant du châssis. 
                Canon : 125 mm. 
                Mitrailleuses : 12,7 mm ou 7,62 mm. 
                Equipage : 3.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char63.jpg",
            additionalImages = listOf("char63_extra1.jpg", "char63_extra2.jpg", "char63_extra3.jpg"),
            correct = "Type 16 MCV",
            options = listOf("Jaguar", "Centauro", "Type 16 MCV", "Freccia"),
            category = "chars",
            description = """
                Le Type 16 MCV, Maneuver Combat Vehicle, est un char léger à roues 8×8 japonais, développé dans les années 2010. 
                Il est entré en service en 2016. 
                Armé d’un canon de 105 mm stabilisé à chargement manuel, avec le groupe motopropulseur à l’avant-gauche et le pilote à droite. 
                Peut recevoir un surblindage additionnel. 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        ),
        Question(
            image = "char64.jpg",
            additionalImages = listOf("char64_extra1.jpg", "char64_extra2.jpg", "char64_extra3.jpg", "char64_extra4.jpg"),
            correct = "ZTL-11",
            options = listOf("ZTL-11", "TR-85", "Type 16 MCV", "TYPE-98"),
            category = "chars",
            description = """
                Le ZTL-11 est un char léger chinois à roues 8×8, développé dans les années 2010. 
                Il est entré en service en 2014. 
                Basé sur le châssis du ZBL-09, il inclut un stabilisateur d’armement et une caméra thermique. 
                Le canon de 105 mm peut tirer des obus explosifs, à charge creuse et antichars sous-calibrés. 
                Canon : 105 mm. 
                Mitrailleuse : 12,7 mm. 
                Equipage : 4.
            """.trimIndent(),
            moreInfo = null
        )
    )
}