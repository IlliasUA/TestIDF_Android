package com.example.quizapp

import legOS.testidf.R

object Test_Data {
    val QUESTION = listOf(
        Question(
            image = "char1.webp",
            additionalImages = listOf("char1_extra1.jpg", "char1_extra2.jpg"),
            correct = "Abrams",
            options = listOf("Abrams", "Leopard-2", "Challenger-2", "M-109 Paladin"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_abrams,
            moreInfo = null
        ),
        Question(
            image = "char2.jpg",
            additionalImages = listOf("char2_extra1.jpg", "char2_extra2.jpg"),
            correct = "Ariete",
            options = listOf("Leclerc", "Leopard-1", "Ariete", "T-14"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_ariete,
            moreInfo = null
        ),
        Question(
            image = "char3.webp",
            additionalImages = listOf("char3_extra1.jpg", "char3_extra2.jpg", "char3_extra3.jpg"),
            correct = "Challenger-1",
            options = listOf("Chieftain", "Abrams", "M-60", "Challenger-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_challenger_1,
            moreInfo = null
        ),
        Question(
            image = "char4.webp",
            additionalImages = listOf("char4_extra1.jpg", "char4_extra2.jpg", "char4_extra3.jpg"),
            correct = "Challenger-2",
            options = listOf("Leclerc", "Leopard-2", "Challenger-2", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_challenger_2,
            moreInfo = null
        ),
        Question(
            image = "char5.webp",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.webp"),
            correct = "Chieftain",
            options = listOf("Challenger-1", "Chieftain", "Leopard-1", "Challenger-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_chieftain,
            moreInfo = null
        ),
        Question(
            image = "char6.webp",
            additionalImages = listOf("char6_extra1.jpg", "char6_extra2.jpg"),
            correct = "Leopard-2",
            options = listOf("Leopard-2", "PZH-2000", "Leopard-1", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_leopard_2,
            moreInfo = null
        ),
        Question(
            image = "char7.webp",
            additionalImages = listOf("char7_extra1.jpg", "char7_extra2.jpg", "char7_extra3.jpg"),
            correct = "Leopard-1",
            options = listOf("Ariete", "Tigre", "Panther", "Leopard-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_leopard_1,
            moreInfo = null
        ),
        Question(
            image = "char8.webp",
            additionalImages = listOf("char8_extra1.webp", "char8_extra2.jpg", "char8_extra3.jpg"),
            correct = "T-55",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_55,
            moreInfo = null
        ),
        Question(
            image = "char9.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("M-60", "AMX-30", "Magach-7", "T-64"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_m_60,
            moreInfo = null
        ),
        Question(
            image = "char10.webp",
            additionalImages = listOf("char10_extra1.webp", "char10_extra2.webp"),
            correct = "Magach-7",
            options = listOf("Merkava-3", "Magach-7", "M-60", "Chieftain"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_magach_7,
            moreInfo = null
        ),
        Question(
            image = "char11.webp",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-3", "Merkava-4", "Magach-7", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_4,
            moreInfo = null
        ),
        Question(
            image = "char12.jpg",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_4,
            moreInfo = null
        ),
        Question(
            image = "char13.webp",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_62,
            moreInfo = null
        ),
        Question(
            image = "char14.jpg",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_64,
            moreInfo = null
        ),
        Question(
            image = "char15.webp",
            additionalImages = listOf("char15_extra1.jpg", "char15_extra2.jpg", "char15_extra3.jpg", "char15_extra4.webp"),
            correct = "T-72",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_72,
            moreInfo = null
        ),
        Question(
            image = "char16.webp",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_80,
            moreInfo = null
        ),
        Question(
            image = "char17.webp",
            additionalImages = listOf("char17_extra1.jpg", "char17_extra2.jpg", "char17_extra3.jpg"),
            correct = "T-90",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_90,
            moreInfo = null
        ),
        Question(
            image = "char18.jpg",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("M-60", "Chieftain", "Challenger-1", "AMX-30"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_amx_30,
            moreInfo = null
        ),
        Question(
            image = "char19.webp",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("AMX-30", "AMX-10RC", "Leclerc", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_amx_30,
            moreInfo = null
        ),
        Question(
            image = "char20.webp",
            additionalImages = listOf("char18_extra1.jpg", "char18_extra2.jpg", "char18_extra3.jpg"),
            correct = "AMX-30",
            options = listOf("Magach-7", "AMX-30", "Abrams", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_amx_30,
            moreInfo = null
        ),
        Question(
            image = "char21.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Magach-7", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_m_60,
            moreInfo = null
        ),
        Question(
            image = "char22.webp",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Leopard-1", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_m_60,
            moreInfo = null
        ),
        Question(
            image = "char23.jpg",
            additionalImages = listOf("char9_extra1.jpg", "char9_extra2.jpg"),
            correct = "M-60",
            options = listOf("AMX-30", "T-64", "Leopard-1", "M-60"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_m_60,
            moreInfo = null
        ),
        Question(
            image = "char24.webp",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_80,
            moreInfo = null
        ),
        Question(
            image = "char25.webp",
            additionalImages = listOf("char6_extra1.jpg", "char6_extra2.jpg"),
            correct = "Leopard-2",
            options = listOf("Leclerc", "Leopard-2", "Leopard-1", "Ariete"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_leopard_2,
            moreInfo = null
        ),
        Question(
            image = "char26.jpg",
            additionalImages = listOf("char2_extra1.jpg", "char2_extra2.jpg"),
            correct = "Ariete",
            options = listOf("Leclerc", "Leopard-2", "Leopard-1", "Ariete"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_ariete,
            moreInfo = null
        ),
        Question(
            image = "char27.webp",
            additionalImages = listOf("char16_extra1.jpg", "char16_extra2.jpg", "char16_extra3.jpg"),
            correct = "T-80",
            options = listOf("T-64", "T-72", "T-80", "T-90"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_80,
            moreInfo = null
        ),
        Question(
            image = "char28.webp",
            additionalImages = listOf("char28_extra1.jpg", "char28_extra2.jpg"),
            correct = "Arjun MK1",
            options = listOf("Arjun MK1", "TYPE-98", "TYPE-99", "K-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_arjun_mk1,
            moreInfo = null
        ),
        Question(
            image = "char29.webp",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.webp"),
            correct = "Chieftain",
            options = listOf("Challenger-1", "Leopard-1", "Leopard-2", "Chieftain"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_chieftain,
            moreInfo = null
        ),
        Question(
            image = "char30.webp",
            additionalImages = listOf("char5_extra1.jpg", "char5_extra2.jpg", "char5_extra3.webp"),
            correct = "Chieftain",
            options = listOf("Chieftain", "Abrams", "Leclerc", "Challenger-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_chieftain,
            moreInfo = null
        ),
        Question(
            image = "char31.jpg",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1",
            options = listOf("Arjun MK1", "K-1", "TYPE-99", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_k_1,
            moreInfo = null
        ),
        Question(
            image = "char32.webp",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1",
            options = listOf("Arjun MK1", "K-1", "TYPE-99", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_k_1,
            moreInfo = null
        ),
        Question(
            image = "char33.webp",
            additionalImages = listOf("char33_extra1.jpg", "char33_extra2.jpg"),
            correct = "Merkava-3",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "Challenger-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_3,
            moreInfo = null
        ),
        Question(
            image = "char34.webp",
            additionalImages = listOf("char33_extra1.jpg", "char33_extra2.jpg"),
            correct = "Merkava-3",
            options = listOf("Merkava-3", "Merkava-4", "Challenger-1", "Challenger-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_3,
            moreInfo = null
        ),
        Question(
            image = "char35.webp",
            additionalImages = listOf("char35_extra1.jpg", "char35_extra2.jpg", "char35_extra3.jpg"),
            correct = "PT-91",
            options = listOf("ZTZ-99", "TYPE-98", "PT-91", "T-64"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_pt_91,
            moreInfo = null
        ),
        Question(
            image = "char36.webp",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_62,
            moreInfo = null
        ),
        Question(
            image = "char37.webp",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_64_extended,
            moreInfo = null
        ),
        Question(
            image = "char38.webp",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_64_extended,
            moreInfo = null
        ),
        Question(
            image = "char39.webp",
            additionalImages = listOf("char39_extra1.jpg", "char39_extra2.jpg", "char39_extra3.jpg", "char39_extra4.jpg"),
            correct = "TYPE-10",
            options = listOf("Leopard-2", "K-1", "TYPE-10", "Abrams"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_10,
            moreInfo = null
        ),
        Question(
            image = "char40.webp",
            additionalImages = listOf("char40_extra1.jpg", "char40_extra2.webp", "char40_extra3.jpg"),
            correct = "TYPE-98",
            options = listOf("T-72", "T-80", "TYPE-98", "TYPE-99"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_98,
            moreInfo = null
        ),
        Question(
            image = "char41.webp",
            additionalImages = listOf("char40_extra1.jpg", "char40_extra2.webp", "char40_extra3.jpg"),
            correct = "TYPE-98",
            options = listOf("T-80", "TYPE-98", "T-72", "TYPE-99"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_98,
            moreInfo = null
        ),
        Question(
            image = "char42.webp",
            additionalImages = listOf("char42_extra1.webp", "char42_extra2.webp", "char42_extra3.webp", "char42_extra4.webp"),
            correct = "TYPE-99",
            options = listOf("T-72", "TYPE-10", "TYPE-98", "TYPE-99"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_99,
            moreInfo = null
        ),
        Question(
            image = "char43.jpg",
            additionalImages = listOf("char11_extra1.jpg", "char11_extra2.jpg"),
            correct = "Merkava-4",
            options = listOf("Merkava-1", "Merkava-2", "Merkava-3", "Merkava-4"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_4,
            moreInfo = null
        ),
        Question(
            image = "char44.webp",
            additionalImages = listOf("char44_extra1.jpg", "char44_extra2.jpg", "char44_extra3.jpg"),
            correct = "Merkava-1",
            options = listOf("Merkava-1", "Merkava-2", "Merkava-3", "Merkava-4"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_merkava_1,
            moreInfo = null
        ),
        Question(
            image = "char45.webp",
            additionalImages = listOf("char39_extra1.jpg", "char39_extra2.jpg", "char39_extra3.jpg", "char39_extra4.jpg"),
            correct = "TYPE-10",
            options = listOf("TYPE-10", "K-1", "Leclerc", "Arjun MK1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_10,
            moreInfo = null
        ),
        Question(
            image = "char46.webp",
            additionalImages = listOf("char7_extra1.jpg", "char7_extra2.jpg"),
            correct = "Leopard-1",
            options = listOf("TYPE-10", "Challenger-1", "Leopard-1", "Leopard-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_leopard_1,
            moreInfo = null
        ),
        Question(
            image = "char48.jpg",
            additionalImages = listOf("char8_extra1.webp", "char8_extra2.jpg", "char8_extra3.jpg"),
            correct = "T-55",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_55,
            moreInfo = null
        ),
        Question(
            image = "char49.webp",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_62,
            moreInfo = null
        ),
        Question(
            image = "char50.webp",
            additionalImages = listOf("char13_extra1.jpg", "char13_extra2.jpg", "char13_extra3.jpg"),
            correct = "T-62",
            options = listOf("T-55", "T-62", "T-64", "T-72"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_62,
            moreInfo = null
        ),
        Question(
            image = "char51.webp",
            additionalImages = listOf("char14_extra1.jpg", "char14_extra2.jpg", "char14_extra3.jpg"),
            correct = "T-64",
            options = listOf("T-62", "T-64", "T-72", "T-80"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_64_extended,
            moreInfo = null
        ),
        Question(
            image = "char52.jpg",
            additionalImages = listOf("char52_extra1.jpg", "char52_extra2.jpg", "char52_extra3.jpg", "char52_extra4.jpg"),
            correct = "AMX-13",
            options = listOf("AMX-13", "AMX-10RC", "AMX-30", "Scorpion"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_amx_13,
            moreInfo = null
        ),
        Question(
            image = "char53.jpg",
            additionalImages = listOf("char53_extra1.jpg", "char53_extra2.jpg", "char53_extra3.jpg", "char53_extra4.jpg"),
            correct = "K-2 Black Panther",
            options = listOf("K-2 Black Panther", "K-1 Type 88", "M1A1 Abrams", "Challenger-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_k_2_black_panther,
            moreInfo = null
        ),
        Question(
            image = "char54.webp",
            additionalImages = listOf("char31_extra1.jpg", "char31_extra2.jpg", "char31_extra3.jpg"),
            correct = "K-1 Type 88",
            options = listOf("Arjun MK1", "Leclerc", "K-1 Type 88", "K-2 Black Panther"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_k_1,
            moreInfo = null
        ),
        Question(
            image = "char55.webp",
            additionalImages = listOf("char55_extra1.webp", "char55_extra2.webp", "char55_extra3.webp"),
            correct = "Leclerc",
            options = listOf("K-2 Black Panther", "M1A1 Abrams", "Leclerc", "Challenger-2"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_leclerc,
            moreInfo = null
        ),
        Question(
            image = "char56.webp",
            additionalImages = listOf("char56_extra1.jpg", "char56_extra2.jpg", "char56_extra3.jpg"),
            correct = "TR-85",
            options = listOf("VT-4", "TR-85", "TYPE-98", "ZTZ-99"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_tr_85,
            moreInfo = null
        ),
        Question(
            image = "char57.webp",
            additionalImages = listOf("char57_extra1.webp", "char57_extra2.webp", "char57_extra3.webp", "char57_extra4.webp", "char57_extra5.webp"),
            correct = "VT-4",
            options = listOf("VT-4", "TR-85", "TYPE-98", "ZTZ-99"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_vt_4,
            moreInfo = null
        ),
        Question(
            image = "char58.jpg",
            additionalImages = listOf("char58_extra1.webp"),
            correct = "Jaguar",
            options = listOf("K-2 Black Panther", "Jaguar", "Griffon", "Serval"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_jaguar,
            moreInfo = null
        ),
        Question(
            image = "char59.webp",
            additionalImages = listOf("char59_extra1.webp", "char59_extra2.webp", "char59_extra3.jpg"),
            correct = "Strv-103",
            options = listOf("Strv-103", "Scorpion", "Wiesel", "Merkava-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_strv_103,
            moreInfo = null
        ),
        Question(
            image = "char60.jpg",
            additionalImages = listOf("char59_extra1.webp", "char59_extra2.webp", "char59_extra3.jpg"),
            correct = "Strv-103",
            options = listOf("Strv-103", "Scorpion", "Wiesel", "Merkava-1"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_strv_103,
            moreInfo = null
        ),
        Question(
            image = "char61.webp",
            additionalImages = listOf("char61_extra1.jpg", "char61_extra2.jpg", "char61_extra3.jpg"),
            correct = "T-14 Armata",
            options = listOf("VT-4", "TR-85", "T-14 Armata", "K-2 Black Panther"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_14_armata,
            moreInfo = null
        ),
        Question(
            image = "char62.webp",
            additionalImages = listOf("char61_extra1.jpg", "char61_extra2.jpg", "char61_extra3.jpg"),
            correct = "T-14 Armata",
            options = listOf("VT-4", "TR-85", "T-14 Armata", "K-2 Black Panther"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_t_14_armata,
            moreInfo = null
        ),
        Question(
            image = "char63.webp",
            additionalImages = listOf("char63_extra1.webp", "char63_extra2.webp", "char63_extra3.webp"),
            correct = "Type 16 MCV",
            options = listOf("Jaguar", "Centauro", "Type 16 MCV", "Freccia"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_type_16_mcv,
            moreInfo = null
        ),
        Question(
            image = "char64.webp",
            additionalImages = listOf("char64_extra1.jpg", "char64_extra2.jpg", "char64_extra3.webp", "char64_extra4.jpg"),
            correct = "ZTL-11",
            options = listOf("ZTL-11", "TR-85", "Type 16 MCV", "TYPE-98"),
            category = "chars",
            description = null,
            descriptionResId = R.string.desc_ztl_11,
            moreInfo = null
        )
    )
}