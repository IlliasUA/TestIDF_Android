package com.example.quizapp

object Genie_Data {
    val QUESTION = listOf(
        Question(
            image = "genie1.jpg",
            additionalImages = listOf("genie1_extra1.webp", "genie1_extra2.jpg", "genie1_extra3.jpg"),
            correct = "SPRAT",
            options = listOf("SPRAT", "PMM-2", "DACHS", "EFA"),
            description = """
                Dans le cadre de l'appui direct des unités engagées dans la zone des combats,
                l'armée de Terre dispose du Système de Pose Rapide de Travures (SPRAT).
                pour le franchissement de coupures sèches ou humides comprise entre 3 et 24m.
                SPRAT donne à l’armée de Terre la capacité de franchir avec ses chars Leclerc des brèches de 24 m de large,
                en ambiance tactique.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie2.webp",
            additionalImages = listOf("genie2_extra1.jpg", "genie2_extra2.jpg"),
            correct = "Biber",
            options = listOf("Leguan", "Buffalo", "Biber", "PZM-3"),
            description = """
                Le Biber est un char poseur de ponts d'origine allemande. Il fait partie des véhicules de soutien au combat.
                Son prédécesseur était le char poseur de ponts M48 et son successeur est le Leguan.
                Dans les années 1960, l’entreprise MaK a développé pour l’armée de terre allemande, sur la base du char Leopard 1.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie3.jpg",
            additionalImages = listOf("genie3_extra1.jpg", "genie3_extra2.jpg", "genie3_extra3.jpg", "genie3_extra4.jpg"),
            correct = "Buffalo",
            options = listOf("SPRAT", "Buffalo", "MDK-2", "GSP"),
            description = """
                Le Buffalo Mine protected clearance vehicule est un véhicule militaire du génie américain.
                Alors que le Casspir était un véhicule à quatre roues, le Buffalo en a une paire de plus. Il est aussi équipé d'un grand bras articulé,
                qu'un opérateur peut utiliser pour examiner une mine suspecte à une bonne distance de sécurité, en restant abrité.
                Armement principal : mitrailleuse Browning M2 de 12,7 mm.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie4.jpg",
            additionalImages = listOf("genie4_extra1.webp", "genie4_extra2.jpg", "genie4_extra3.webp", "genie4_extra4.jpg"),
            correct = "DACHS",
            options = listOf("GMZ", "Buffalo", "DACHS", "Biber"),
            description = """
                Le Pionierpanzer est conçu pour le remorquage des véhicules endommagés,
                mais sa mission principale consiste à travailler sur les terrains, à assurer l’accès et la sortie des obstacles aquatiques,
                à construire des fortifications et à réaliser d’autres tâches visant à éliminer divers obstacles sur le champ de bataille.
                Les premiers véhicules de série de ce type ont été mis en service dans la Bundeswehr en avril 1989. Dès fin 1990,
                36 Pionierpanzer 2A1 – surnommés informellement Blaireau – avaient été fabriqués et livrés à l’armée allemande.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie5.jpg",
            additionalImages = listOf("genie4_extra1.webp", "genie4_extra2.jpg", "genie4_extra3.webp", "genie4_extra4.jpg"),
            correct = "DACHS",
            options = listOf("SPRAT", "M9 ACE", "PZM-3", "DACHS"),
            description = """
                Le Pionierpanzer est conçu pour le remorquage des véhicules endommagés,
                mais sa mission principale consiste à travailler sur les terrains, à assurer l’accès et la sortie des obstacles aquatiques,
                à construire des fortifications et à réaliser d’autres tâches visant à éliminer divers obstacles sur le champ de bataille.
                Les premiers véhicules de série de ce type ont été mis en service dans la Bundeswehr en avril 1989. Dès fin 1990,
                36 Pionierpanzer 2A1 – surnommés informellement Blaireau – avaient été fabriqués et livrés à l’armée allemande.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie8.jpg",
            additionalImages = listOf("genie8_extra1.webp", "genie8_extra2.jpg", "genie8_extra3.webp", "genie8_extra4.jpg"),
            correct = "GSP",
            options = listOf("GMZ", "PTS", "GSP", "M3 Amphibius"),
            description = """
                GSP-55 est un véhicule amphibie chenillé militaire russe capable de transporter des véhicules sur son toit
                et de les transporter sur l'eau à la manière d'un bac (bateau) ou d'un ferry (bateau).
                On peut l'utiliser comme pont et donc comme véhicule pontonnier, au besoin en en déployant plusieurs en série.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie9.jpg",
            additionalImages = listOf("genie8_extra1.webp", "genie8_extra2.jpg", "genie8_extra3.webp", "genie8_extra4.jpg"),
            correct = "GSP",
            options = listOf("GMZ", "PTS", "MDK-2", "GSP"),
            description = """
                GSP-55 est un véhicule amphibie chenillé militaire russe capable de transporter des véhicules sur son toit
                et de les transporter sur l'eau à la manière d'un bac (bateau) ou d'un ferry (bateau).
                On peut l'utiliser comme pont et donc comme véhicule pontonnier, au besoin en en déployant plusieurs en série.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie10.webp",
            additionalImages = listOf("genie10_extra1.webp", "genie10_extra2.webp"),
            correct = "IRM-2",
            options = listOf("IRM-2", "M9 ACE", "PZM-3", "DACHS"),
            description = """
                Le IMR-2 (où IMR signifie Inzhenernaya mashina razgrazhdeniya) est un char du génie russe introduit en 1982.
                En service aujourd’hui dans les forces armées russes.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie11.webp",
            additionalImages = listOf("genie10_extra1.webp", "genie10_extra2.webp"),
            correct = "IRM-2",
            options = listOf("IRM-2", "M9 ACE", "PMM-2", "PZM-3"),
            description = """
                Le IMR-2 (où IMR signifie Inzhenernaya mashina razgrazhdeniya) est un char du génie russe introduit en 1982.
                En service aujourd’hui dans les forces armées russes.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie12.jpg",
            additionalImages = listOf("genie12_extra1.jpg", "genie12_extra2.webp", "genie12_extra3.webp", "genie12_extra4.jpg", "genie12_extra5.jpg"),
            correct = "M3 Amphibius",
            options = listOf("EFA", "PTS", "M3 Amphibius", "GSP"),
            description = """
                Le M3 Amphibious Rig est un véhicule militaire amphibie allemand,
                utilisé pour assurer la traversée des chars et autres véhicules à travers les obstacles aquatiques.
                Il peut être assemblé pour former aussi bien des ponts que des bacs de franchissement.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie13.jpg",
            additionalImages = listOf("genie12_extra1.jpg", "genie12_extra2.webp", "genie12_extra3.webp", "genie12_extra4.jpg", "genie12_extra5.jpg"),
            correct = "M3 Amphibius",
            options = listOf("GMZ", "M3 Amphibius", "EFA", "PTS"),
            description = """
                Le M3 Amphibious Rig est un véhicule militaire amphibie allemand,
                utilisé pour assurer la traversée des chars et autres véhicules à travers les obstacles aquatiques.
                Il peut être assemblé pour former aussi bien des ponts que des bacs de franchissement.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie14.webp",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("MDK-2", "IRM-2", "PZM-3", "M9 ACE"),
            description = """
                Un bulldozer blindé du génie militaire développé par les États-Unis.
                Il est en service dans l’US Army depuis 1988. Ses missions principales consistent à :
                dégager les obstacles à l’aide de sa lame bulldozer avant,
                creuser des tranchées et construire des installations de protection,
                aménager des pistes auxiliaires,
                ouvrir des voies d’accès aux points de franchissement.
                Sa mission fondamentale est d’accompagner les forces blindées alliées pour leur ouvrir la voie lors des avancées.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie15.jpg",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("M9 ACE", "Buffalo", "PZM-3", "DACHS"),
            description = """
                Un bulldozer blindé du génie militaire développé par les États-Unis.
                Il est en service dans l’US Army depuis 1988. Ses missions principales consistent à :
                dégager les obstacles à l’aide de sa lame bulldozer avant,
                creuser des tranchées et construire des installations de protection,
                aménager des pistes auxiliaires,
                ouvrir des voies d’accès aux points de franchissement.
                Sa mission fondamentale est d’accompagner les forces blindées alliées pour leur ouvrir la voie lors des avancées.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie16.jpg",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("DACHS", "M9 ACE", "PZM-3", "GSP"),
            description = """
                Un bulldozer blindé du génie militaire développé par les États-Unis.
                Il est en service dans l’US Army depuis 1988. Ses missions principales consistent à :
                dégager les obstacles à l’aide de sa lame bulldozer avant,
                creuser des tranchées et construire des installations de protection,
                aménager des pistes auxiliaires,
                ouvrir des voies d’accès aux points de franchissement.
                Sa mission fondamentale est d’accompagner les forces blindées alliées pour leur ouvrir la voie lors des avancées.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie17.jpg",
            additionalImages = listOf("genie17_extra1.webp", "genie17_extra2.webp", "genie17_extra3.webp"),
            correct = "MDK-2",
            options = listOf("PZM-3", "PMM-2", "MDK-2", "PTS"),
            description = """
                Véhicule à chenilles excavateur de tranchées MDK-2M (AT-T) armée URSS.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie18.webp",
            additionalImages = listOf("genie17_extra1.webp", "genie17_extra2.webp", "genie17_extra3.webp"),
            correct = "MDK-2",
            options = listOf("PZM-3", "PMM-2", "MDK-2", "PTS"),
            description = """
                Véhicule à chenilles excavateur de tranchées MDK-2M (AT-T) armée URSS.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie19.webp",
            additionalImages = listOf("genie19_extra1.jpg", "genie19_extra2.webp", "genie19_extra3.jpg"),
            correct = "PMM-2",
            options = listOf("Biber", "PMM-2", "M3 Amphibius", "GSP"),
            description = """
                Le véhicule poseur de ponts PPM-2 est conçu pour assurer le franchissement des obstacles aquatiques par les chars,
                les canons automoteurs et autres véhicules militaires.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie20.jpg",
            additionalImages = listOf("genie19_extra1.jpg", "genie19_extra2.webp", "genie19_extra3.jpg"),
            correct = "PMM-2",
            options = listOf("EFA", "PMM-2", "M3 Amphibius", "SPRAT"),
            description = """
                Le véhicule poseur de ponts PPM-2 est conçu pour assurer le franchissement des obstacles aquatiques par les chars,
                les canons automoteurs et autres véhicules militaires.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie21.jpg",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("PTS", "M3 Amphibius", "Biber", "GMZ"),
            description = """
                PTS est un véhicule de transport de troupes amphibie chenillé militaire soviétique capable de transporter des véhicules
                sur son toit et de les transporter sur l'eau à la manière d'un bac ou d'un ferry.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie22.jpg",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("PTS", "M3 Amphibius", "Biber", "EFA"),
            description = """
                PTS est un véhicule de transport de troupes amphibie chenillé militaire soviétique capable de transporter des véhicules
                sur son toit et de les transporter sur l'eau à la manière d'un bac ou d'un ferry.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie23.jpg",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("IRM-2", "SPRAT", "Biber", "PTS"),
            description = """
                PTS est un véhicule de transport de troupes amphibie chenillé militaire soviétique capable de transporter des véhicules
                sur son toit et de les transporter sur l'eau à la manière d'un bac ou d'un ferry.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie25.jpg",
            additionalImages = listOf("genie25_extra1.jpg", "genie25_extra2.jpg", "genie25_extra3.webp"),
            correct = "AMX-30 EBG",
            options = listOf("AMX-30 SDPMAC", "AMX-30 EBG", "DACHS", "M9 ACE"),
            description = """
                L’engin blindé du génie (EBG) est un char du génie monté sur le même châssis que les chars AMX-30B2.
                Sa mission est de dégager et préparer la voie pour d'autres engins.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie26.jpg",
            additionalImages = listOf("genie26_extra1.jpg", "genie26_extra2.jpg", "genie26_extra3.jpg"),
            correct = "AMX-30 EBG SDPMAC",
            options = listOf("AMX-30 EBG SDPMAC", "AMX-30 EBG", "TC-910", "M9 ACE"),
            description = """
                Le système de déminage pyrotechnique pour mines antichars (SDPMAC)
                est un ensemble composé du lanceur israélien CARPET monté sur l'engin blindé du génie (EBG).
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie27.jpg",
            additionalImages = listOf("genie25_extra1.jpg", "genie25_extra2.jpg", "genie25_extra3.webp"),
            correct = "AMX-30 EBG",
            options = listOf("DACHS", "Buffalo", "GMZ", "AMX-30 EBG"),
            description = """
                L’engin blindé du génie (EBG) est un char du génie monté sur le même châssis que les chars AMX-30B2.
                Sa mission est de dégager et préparer la voie pour d'autres engins.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie28.jpg",
            additionalImages = listOf("genie28_extra1.jpg", "genie28_extra2.jpg", "genie28_extra3.jpg", "genie28_extra4.jpg", "genie28_extra5.jpg"),
            correct = "EFA",
            options = listOf("M3 Amphibius", "PMM-2", "GSP", "EFA"),
            description = """
                Successeur du bac Gillois valorisé, l'engin de franchissement de l'avant (EFA)
                est un matériel amphibie autonome conçu pour des franchissements continus (pont)
                ou discontinus (bac) aux véhicules jusqu’à une MLC (Military Load Class) 70.
                L'EFA comprend une coque contenant la cabine de l'équipage, la salle des machines,
                les essieux et un compartiment arrière, 2 rampes articulées, 2 volets supports de flotteurs
                et un poste de pilotage aquatique amovible.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie29.jpg",
            additionalImages = listOf("genie28_extra1.jpg", "genie28_extra2.jpg", "genie28_extra3.jpg", "genie28_extra4.jpg", "genie28_extra5.jpg"),
            correct = "EFA",
            options = listOf("EFA", "PMM-2", "DACHS", "GSP"),
            description = """
                Successeur du bac Gillois valorisé, l'engin de franchissement de l'avant (EFA)
                est un matériel amphibie autonome conçu pour des franchissements continus (pont)
                ou discontinus (bac) aux véhicules jusqu’à une MLC (Military Load Class) 70.
                L'EFA comprend une coque contenant la cabine de l'équipage, la salle des machines,
                les essieux et un compartiment arrière, 2 rampes articulées, 2 volets supports de flotteurs
                et un poste de pilotage aquatique amovible.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie30.webp",
            additionalImages = listOf("genie30_extra1.jpg"),
            correct = "EGAME",
            options = listOf("TC-910", "EGAME", "TNA", "EGRAP"),
            description = """
                Engin du génie d’aménagement. Bouteur à roues à 4 roues motrices et directrices,
                l’EGAME est destiné à être déployé au plus près des forces de premier échelon (détachement précurseur)
                et bénéficie d’une protection balistique amovible.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie31.webp",
            additionalImages = listOf("genie31_extra1.webp"),
            correct = "EGRAP",
            options = listOf("TNA", "GMZ", "EGRAP", "Manitou"),
            description = """
                Engin du génie rapide de protection. Chargeuse-pelleteuse issu de la gamme civile,
                cet engin a pour but de faciliter le déploiement, le mouvement et la protection d’une force.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie32.jpg",
            additionalImages = listOf("genie32.jpg"),
            correct = "Manitou",
            options = listOf("TNA", "Manitou", "TC-910", "DACHS"),
            description = """
                Le chariot télescopique MLT 625-75 H est une machine très compacte.
                Dédié spécialement aux activités agricoles.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie33.jpg",
            additionalImages = listOf("genie33_extra1.jpg", "genie33_extra2.webp", "genie33_extra3.jpg"),
            correct = "PPLD",
            options = listOf("PPLD", "PVP", "URAL-4320", "GSP"),
            description = """
                Le porteur polyvalent lourd de dépannage - PPLD permet notamment l’évacuation de véhicules endommagés.
                Il possède 4 essieux toutes roues motrices (8x8) lui permettant de rouler sur tous types de routes
                et de chemins et dans toutes les conditions climatiques.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie34.webp",
            additionalImages = listOf("genie34_extra1.jpg", "genie34_extra2.jpg", "genie34_extra3.jpg", "genie34_extra4.jpg"),
            correct = "VDM Souvim",
            options = listOf("SPRAT", "Buffalo", "VDM Souvim", "PPLD"),
            description = """
                Le système d'ouverture d'itinéraire miné (SOUVIM) permet de sécuriser un itinéraire faiblement pollué
                par des mines antichar ou antipersonnel, détectables ou à pression.
                Il a pour vocation de participer aux missions d'appui à la mobilité en assurant,
                sur de longues distances, l'ouverture rapide d'itinéraires faiblement minés (minage de harcèlement)
                en 2e échelon ou en zone arrière des grandes unités (zone des flux logistiques) dans un conflit de basse intensité.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie35.jpg",
            additionalImages = listOf("genie35_extra1.jpg"),
            correct = "TC-910",
            options = listOf("EGAME", "EGRAP", "Manitou", "TC-910"),
            description = """
                Tracteur chargeur (TC) 910.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie36.webp",
            additionalImages = listOf("genie36_extra1.jpg", "genie36_extra2.jpg"),
            correct = "TNA",
            options = listOf("TNA", "PTS", "TC-910", "VDM Souvim"),
            description = """
                Transport d'un tracteur niveleur aérolargable (TNA).
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie37.jpg",
            additionalImages = listOf("genie34_extra1.jpg", "genie34_extra2.jpg", "genie34_extra3.jpg", "genie34_extra4.jpg"),
            correct = "VDM Souvim",
            options = listOf("TNA", "VDM Souvim", "EGRAP", "EGAME"),
            description = """
                Le système d'ouverture d'itinéraire miné (SOUVIM) permet de sécuriser un itinéraire faiblement pollué
                par des mines antichar ou antipersonnel, détectables ou à pression.
                Il a pour vocation de participer aux missions d'appui à la mobilité en assurant,
                sur de longues distances, l'ouverture rapide d'itinéraires faiblement minés (minage de harcèlement)
                en 2e échelon ou en zone arrière des grandes unités (zone des flux logistiques) dans un conflit de basse intensité.
            """.trimIndent(),
            moreInfo = null,
            category = "genie"
        )
    )
}