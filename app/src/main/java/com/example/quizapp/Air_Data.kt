package com.example.quizapp

import legOS.testidf.R

object Air_Data {
    val QUESTION = listOf(
        Question(
            image = "air1.webp",
            additionalImages = listOf("air1_extra1.webp", "air1_extra2.jpg", "air1_extra3.jpg"),
            correct = "AH-1W Super Cobra",
            options = listOf("AH-1W Super Cobra", "Apache AH64", "KA-50 Black Shark", "Changhe Z-10"),
            descriptionResId = R.string.air_data_ah1w_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air2.webp",
            additionalImages = listOf("air2_extra1.jpg", "air2_extra2.jpg", "air2_extra3.jpg", "air2_extra4.jpg"),
            correct = "Apache AH64",
            options = listOf("AH-1W Super Cobra", "Apache AH64", "A129 Mangusta", "Changhe Z-10"),
            descriptionResId = R.string.air_data_apache_ah64_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air3.webp",
            additionalImages = listOf("air2_extra1.jpg", "air2_extra2.jpg", "air2_extra3.jpg", "air2_extra4.jpg"),
            correct = "Apache AH64",
            options = listOf("AH-1W Super Cobra", "Apache AH64", "A129 Mangusta", "Changhe Z-10"),
            descriptionResId = R.string.air_data_apache_ah64_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air4.jpg",
            additionalImages = listOf("air2_extra1.jpg", "air2_extra2.jpg", "air2_extra3.jpg", "air2_extra4.jpg"),
            correct = "Apache AH64",
            options = listOf("AH-1W Super Cobra", "Changhe Z-10", "A129 Mangusta", "Apache AH64"),
            descriptionResId = R.string.air_data_apache_ah64_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air5.webp",
            additionalImages = listOf("air5_extra1.jpg", "air5_extra2.jpg", "air5_extra3.jpg", "air5_extra4.jpg"),
            correct = "CH-47 Chinook",
            options = listOf("Stallion CH-53", "UH-60 Black Hawk", "CH-47 Chinook", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_ch47_chinook_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air6.jpg",
            additionalImages = listOf("air5_extra1.jpg", "air5_extra2.jpg", "air5_extra3.jpg", "air5_extra4.jpg"),
            correct = "CH-47 Chinook",
            options = listOf("Stallion CH-53", "UH-60 Black Hawk", "CH-47 Chinook", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_ch47_chinook_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air7.jpg",
            additionalImages = listOf("air7_extra1.jpg", "air7_extra2.jpg", "air7_extra3.jpg", "air7_extra4.jpg"),
            correct = "KA-50 Black Shark",
            options = listOf("KA-50 Black Shark", "KA-27 Helix", "MI-28", "KA-52 Alligator"),
            descriptionResId = R.string.air_data_ka50_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air8.jpg",
            additionalImages = listOf("air7_extra1.jpg", "air7_extra2.jpg", "air7_extra3.jpg", "air7_extra4.jpg"),
            correct = "KA-50 Black Shark",
            options = listOf("KA-50 Black Shark", "Changhe Z-10", "AH-1W Super Cobra", "KA-52 Alligator"),
            descriptionResId = R.string.air_data_ka50_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air9.webp",
            additionalImages = listOf("air9_extra1.jpg", "air9_extra2.jpg", "air9_extra3.jpg", "air9_extra4.jpg"),
            correct = "KA-52 Alligator",
            options = listOf("KA-50 Black Shark", "Changhe Z-10", "AH-1W Super Cobra", "KA-52 Alligator"),
            descriptionResId = R.string.air_data_ka52_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air10.webp",
            additionalImages = listOf("air10_extra1.jpg", "air10_extra2.jpg", "air10_extra3.jpg", "air10_extra4.jpg", "air10_extra5.jpg"),
            correct = "MI-8",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            descriptionResId = R.string.air_data_mi8_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air11.jpg",
            additionalImages = listOf("air11_extra1.jpg", "air11_extra2.jpg", "air11_extra3.jpg", "air11_extra4.jpg", "air11_extra5.jpg"),
            correct = "MI-24",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            descriptionResId = R.string.air_data_mi24_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air12.jpg",
            additionalImages = listOf("air12_extra1.jpg", "air12_extra2.jpg", "air12_extra3.jpg", "air12_extra4.jpg"),
            correct = "MI-26",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            descriptionResId = R.string.air_data_mi26_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air13.webp",
            additionalImages = listOf("air12_extra1.jpg", "air12_extra2.jpg", "air12_extra3.jpg", "air12_extra4.jpg"),
            correct = "MI-26",
            options = listOf("Stallion CH-53", "Changhe Z-20", "MI-26", "MI-35"),
            descriptionResId = R.string.air_data_mi26_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air14.webp",
            additionalImages = listOf("air14_extra1.jpg", "air14_extra2.jpg", "air14_extra3.jpg", "air14_extra4.jpg"),
            correct = "MI-28",
            options = listOf("Changhe Z-10", "MI-28", "KA-50 Black Shark", "AH-1W Super Cobra"),
            descriptionResId = R.string.air_data_mi28_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air15.webp",
            additionalImages = listOf("air14_extra1.jpg", "air14_extra2.jpg", "air14_extra3.jpg", "air14_extra4.jpg"),
            correct = "MI-28",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            descriptionResId = R.string.air_data_mi28_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air16.jpg",
            additionalImages = listOf("air11_extra1.jpg", "air11_extra2.jpg", "air11_extra3.jpg", "air11_extra4.jpg", "air11_extra5.jpg"),
            correct = "MI-24",
            options = listOf("MI-8", "MI-24", "MI-26", "Changhe Z-20"),
            descriptionResId = R.string.air_data_mi24_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air17.webp",
            additionalImages = listOf("air17_extra1.jpg", "air17_extra2.jpg", "air17_extra3.jpg", "air17_extra4.jpg", "air17_extra5.jpg"),
            correct = "Stallion CH-53",
            options = listOf("Stallion CH-53", "MI-26", "KA-27 Helix", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_ch53_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air18.webp",
            additionalImages = listOf("air17_extra1.jpg", "air17_extra2.jpg", "air17_extra3.jpg", "air17_extra4.jpg", "air17_extra5.jpg"),
            correct = "Stallion CH-53",
            options = listOf("Stallion CH-53", "MI-26", "KA-27 Helix", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_ch53_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air19.jpg",
            additionalImages = listOf("air19_extra1.jpg", "air19_extra2.jpg", "air19_extra3.jpg", "air19_extra4.jpg"),
            correct = "UH-60 Black Hawk",
            options = listOf("Stallion CH-53", "Changhe Z-20", "UH-60 Black Hawk", "Caracal H225 M"),
            descriptionResId = R.string.air_data_uh60_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air20.webp",
            additionalImages = listOf("air19_extra1.jpg", "air19_extra2.jpg", "air19_extra3.jpg", "air19_extra4.jpg"),
            correct = "UH-60 Black Hawk",
            options = listOf("Stallion CH-53", "Changhe Z-20", "UH-60 Black Hawk", "Caracal H225 M"),
            descriptionResId = R.string.air_data_uh60_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air21.webp",
            additionalImages = listOf("air21_extra1.jpg", "air21_extra2.jpg", "air21_extra3.jpg", "air21_extra4.jpg"),
            correct = "A129 Mangusta",
            options = listOf("AH-1W Super Cobra", "Apache AH64", "A129 Mangusta", "Tigr HAD"),
            descriptionResId = R.string.air_data_a129_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air22.webp",
            additionalImages = listOf("air21_extra1.jpg", "air21_extra2.jpg", "air21_extra3.jpg", "air21_extra4.jpg"),
            correct = "A129 Mangusta",
            options = listOf("AH-1W Super Cobra", "CSH-2 Rooivalk", "A129 Mangusta", "Tigr HAD"),
            descriptionResId = R.string.air_data_a129_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air23.jpg",
            additionalImages = listOf("air23_extra1.jpg", "air23_extra2.webp", "air23_extra3.jpg", "air23_extra4.jpg"),
            correct = "A330 Phénix",
            options = listOf("C-5 Galaxy", "A330 Phénix", "IL-76 Ilyushin", "A400M Atlas"),
            descriptionResId = R.string.air_data_a330_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air24.webp",
            additionalImages = listOf("air24_extra1.jpg", "air24_extra2.jpg", "air24_extra3.jpg", "air24_extra4.jpg"),
            correct = "A400M Atlas",
            options = listOf("C-160 Transall", "A330 Phénix", "C-130J Super Hercules", "A400M Atlas"),
            descriptionResId = R.string.air_data_a400m_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air26.webp",
            additionalImages = listOf("air26_extra1.jpg", "air26_extra2.jpg", "air26_extra3.jpg"),
            correct = "C-130J Super Hercules",
            options = listOf("A400M Atlas", "C-160 Transall", "C-130J Super Hercules", "IL-76 Ilyushin"),
            descriptionResId = R.string.air_data_c130j_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air27.webp",
            additionalImages = listOf("air27_extra1.jpg", "air27_extra2.jpg", "air27_extra3.jpg"),
            correct = "C-160 Transall",
            options = listOf("C-130J Super Hercules", "A400M Atlas", "A330 Phénix", "C-160 Transall"),
            descriptionResId = R.string.air_data_c160_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air28.webp",
            additionalImages = listOf("air28_extra1.jpg", "air28_extra2.jpg", "air28_extra3.jpg", "air28_extra4.jpg"),
            correct = "Caracal H225M",
            options = listOf("Caracal H225M", "UH-60 Black Hawk", "Stallion CH-53", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_h225m_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air29.webp",
            additionalImages = listOf("air29_extra1.jpg", "air29_extra2.jpg"),
            correct = "Casa CN-235",
            options = listOf("C-160 Transall", "Casa CN-235", "AWACS", "C-130J Super Hercules"),
            descriptionResId = R.string.air_data_cn235_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air30.jpg",
            additionalImages = listOf("air30_extra1.jpg", "air30_extra2.jpg", "air30_extra3.jpg", "air30_extra4.webp"),
            correct = "Changhe Z-20",
            options = listOf("UH-60 Black Hawk", "FENNEC AS555", "KA-52 Alligator", "Changhe Z-20"),
            descriptionResId = R.string.air_data_z20_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air31.webp",
            additionalImages = listOf("air31_extra1.webp", "air31_extra2.jpg", "air31_extra3.jpg"),
            correct = "Changhe Z-10",
            options = listOf("AH-1 Cobra", "Apache AH64", "Changhe Z-10", "A129 Mangusta"),
            descriptionResId = R.string.air_data_z10_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air32.webp",
            additionalImages = listOf("air31_extra1.webp", "air31_extra2.jpg", "air31_extra3.jpg"),
            correct = "Changhe Z-10",
            options = listOf("Changhe Z-10", "Apache AH64", "CSH-2 Rooivalk", "Tigr HAD"),
            descriptionResId = R.string.air_data_z10_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air33.jpg",
            additionalImages = listOf("air33_extra1.jpg", "air33_extra2.jpg", "air33_extra3.jpg", "air33_extra4.jpg"),
            correct = "F-16 Fighting Falcon",
            options = listOf("F-16 Fighting Falcon", "F-22 Raptor", "F-35 Lightning", "Typhoon"),
            descriptionResId = R.string.air_data_f16_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air34.jpg",
            additionalImages = listOf("air33_extra1.jpg", "air33_extra2.jpg", "air33_extra3.jpg", "air33_extra4.jpg"),
            correct = "F-16 Fighting Falcon",
            options = listOf("F-16 Fighting Falcon", "Rafale F4", "F-35 Lightning", "Typhoon"),
            descriptionResId = R.string.air_data_f16_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air35.webp",
            additionalImages = listOf("air35_extra1.jpg", "air35_extra2.jpg", "air35_extra3.jpg", "air33_extra4.jpg"),
            correct = "F-35 Lightning",
            options = listOf("F-16 Fighting Falcon", "F-22 Raptor", "F-35 Lightning", "Typhoon"),
            descriptionResId = R.string.air_data_f35_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air36.jpg",
            additionalImages = listOf("air36_extra3.webp"),
            correct = "FENNEC AS555",
            options = listOf("Gazelle", "FENNEC AS555", "Caracal H225 M", "AH-6 Little Bird"),
            descriptionResId = R.string.air_data_fennec_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air37.webp",
            additionalImages = listOf("air37_extra1.jpg", "air37_extra2.jpg"),
            correct = "Gazelle",
            options = listOf("Gazelle", "FENNEC AS555", "Caracal H225 M", "AH-6 Little Bird"),
            descriptionResId = R.string.air_data_gazelle_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air38.jpg",
            additionalImages = listOf("air38_extra1.jpg", "air38_extra2.jpg", "air38_extra3.jpg", "air38_extra4.jpg"),
            correct = "IL-76 Ilyushin",
            options = listOf("C-160 Transall", "A330 Phénix", "IL-76 Ilyushin", "A400M Atlas"),
            descriptionResId = R.string.air_data_il76_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air39.webp",
            additionalImages = listOf("air39_extra1.jpg", "air39_extra2.jpg", "air39_extra3.jpg"),
            correct = "KA-27 Helix",
            options = listOf("Stallion CH-53", "KA-27 Helix", "CH-47 Chinook", "KA-52 Alligator"),
            descriptionResId = R.string.air_data_ka27_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air40.jpg",
            additionalImages = listOf("air10_extra1.jpg", "air10_extra2.jpg", "air10_extra3.jpg", "air10_extra4.jpg", "air10_extra5.jpg"),
            correct = "MI-8",
            options = listOf("MI-8", "MI-24", "MI-26", "MI-28"),
            descriptionResId = R.string.air_data_mi8_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air41.webp",
            additionalImages = listOf("air41_extra1.jpg", "air41_extra2.jpg", "air41_extra3.jpg", "air41_extra4.jpg", "air41_extra5.jpg"),
            correct = "MIG-29",
            options = listOf("Chengdu J-10", "SU-27", "MIG-29", "SU-57"),
            descriptionResId = R.string.air_data_mig29_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air42.webp",
            additionalImages = listOf("air42_extra1.jpg", "air42_extra2.jpg", "air42_extra3.jpg", "air42_extra4.jpg", "air42_extra5.jpg"),
            correct = "MIG-31",
            options = listOf("Panavia Tornado", "Chengdu J-10", "SU-24", "MIG-31"),
            descriptionResId = R.string.air_data_mig31_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air43.webp",
            additionalImages = listOf("air43_extra1.jpg", "air43_extra2.jpg", "air43_extra3.jpg", "air43_extra4.jpg"),
            correct = "Mirage 2000",
            options = listOf("Typhoon", "Mirage 2000", "MIG-35", "SU-57"),
            descriptionResId = R.string.air_data_mirage2000_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air44.webp",
            additionalImages = listOf("air44_extra1.jpg"),
            correct = "MQ-9 Reaper",
            options = listOf("MQ-9 Reaper", "S-70 Okhotnik-B", "Falco EVO", "Pilatus PC-21"),
            descriptionResId = R.string.air_data_mq9_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air46.jpg",
            additionalImages = listOf("air46_extra1.webp", "air46_extra2.jpg"),
            correct = "Panavia Tornado",
            options = listOf("F-16 Fighting Falcon", "SU-24", "Typhoon", "Panavia Tornado"),
            descriptionResId = R.string.air_data_tornado_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air47.webp",
            additionalImages = listOf("air47_extra1.webp"),
            correct = "Pilatus PC-21",
            options = listOf("Falco EVO", "Wing Loong", "Pilatus PC-21", "Panavia Tornado"),
            descriptionResId = R.string.air_data_pc21_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air48.jpg",
            additionalImages = listOf("air48_extra1.jpg", "air48_extra2.webp", "air48_extra3.jpg"),
            correct = "Rafale F-4",
            options = listOf("Mirage 2000", "Rafale F-4", "Typhoon", "Panavia Tornado"),
            descriptionResId = R.string.air_data_rafale_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air49.webp",
            additionalImages = listOf("air49_extra1.jpg"),
            correct = "S-70 Okhotnik-B",
            options = listOf("MQ-9 Reaper", "S-70 Okhotnik-B", "Falco EVO", "Wing Loong"),
            descriptionResId = R.string.air_data_s70_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air50.webp",
            additionalImages = listOf("air50_extra1.webp", "air50_extra2.webp", "air50_extra3.jpg"),
            correct = "SA 330 PUMA",
            options = listOf("SA 330 PUMA", "Stallion CH-53", "KA-27 Helix", "FENNEC AS555"),
            descriptionResId = R.string.air_data_puma_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air51.webp",
            additionalImages = listOf("air51_extra1.jpg", "air51_extra2.jpg", "air51_extra3.jpg"),
            correct = "SU-27",
            options = listOf("SU-27", "SU-57", "MIG-31", "Chengdu J-10"),
            descriptionResId = R.string.air_data_su27_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air52.webp",
            additionalImages = listOf("air52_extra1.jpg", "air52_extra2.jpg", "air52_extra3.jpg", "air41_extra4.jpg", "air52_extra5.jpg"),
            correct = "Su-57",
            options = listOf("Rafale F4", "F-22 Raptor", "F-35 Lightning", "Su-57"),
            descriptionResId = R.string.air_data_su57_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air53.webp",
            additionalImages = listOf("air53_extra1.jpg", "air53_extra2.jpg", "air53_extra3.jpg", "air53_extra4.jpg", "air53_extra5.jpg"),
            correct = "Tigr HAD",
            options = listOf("Changhe Z-10", "Tigr HAD", "A129 Mangusta", "Apache AH64"),
            descriptionResId = R.string.air_data_tigr_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air54.jpg",
            additionalImages = listOf("air54_extra1.jpg", "air54_extra2.jpg", "air54_extra3.jpg", "air54_extra4.jpg", "air54_extra5.jpg"),
            correct = "Typhoon",
            options = listOf("Rafale F4", "Panavia Tornado", "Typhoon", "Su-57"),
            descriptionResId = R.string.air_data_typhoon_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air55.jpg",
            additionalImages = listOf("air55_extra1.jpg", "air55_extra2.jpg", "air55_extra3.jpg", "air55_extra4.jpg", "air55_extra5.jpg"),
            correct = "F-22 Raptor",
            options = listOf("F-16 Fighting Falcon", "F-22 Raptor", "F-35 Lightning", "Typhoon"),
            descriptionResId = R.string.air_data_f22_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air56.webp",
            additionalImages = listOf("air56_extra1.jpg", "air56_extra2.jpg", "air56_extra3.jpg", "air56_extra4.jpg"),
            correct = "Chengdu J-10",
            options = listOf("F-16 Fighting Falcon", "SU-24", "Panavia Tornado", "Chengdu J-10"),
            descriptionResId = R.string.air_data_j10_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air57.webp",
            additionalImages = listOf("air57_extra1.jpg", "air57_extra2.jpg", "air57_extra3.jpg", "air57_extra4.jpg"),
            correct = "MIG-35",
            options = listOf("MIG-35", "SU-57", "Chengdu J-10", "F-16 Fighting Falcon"),
            descriptionResId = R.string.air_data_mig35_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air58.jpg",
            additionalImages = listOf("air58_extra1.webp", "air58_extra2.jpg"),
            correct = "Wing Loong",
            options = listOf("MQ-9 Reaper", "S-70 Okhotnik-B", "Falco EVO", "Wing Loong"),
            descriptionResId = R.string.air_data_wingloong_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air59.jpg",
            additionalImages = listOf("air59_extra1.jpg", "air59_extra2.jpg"),
            correct = "Mirage 4",
            options = listOf("Mirage 4", "Rockwell B-1 Lancer", "TU-22M Backfire", "Xian H-6"),
            descriptionResId = R.string.air_data_mirage4_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air60.webp",
            additionalImages = listOf("air60_extra1.jpg", "air60_extra2.jpg", "air60_extra3.jpg", "air60_extra4.jpg"),
            correct = "Rockwell B-1 Lancer",
            options = listOf("Mirage 4", "Rockwell B-1 Lancer", "TU-22M Backfire", "Xian H-6"),
            descriptionResId = R.string.air_data_b1_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air61.jpg",
            additionalImages = listOf("air61_extra1.jpg", "air61_extra2.jpg", "air61_extra3.jpg", "air61_extra4.jpg"),
            correct = "TU-22M Backfire",
            options = listOf("Mirage 4", "Rockwell B-1 Lancer", "TU-22M Backfire", "Xian H-6"),
            descriptionResId = R.string.air_data_tu22m_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air62.jpg",
            additionalImages = listOf("air62_extra1.jpg", "air62_extra2.jpg", "air62_extra3.jpg", "air62_extra4.jpg"),
            correct = "Xian H-6",
            options = listOf("Mirage 4", "Rockwell B-1 Lancer", "TU-22M Backfire", "Xian H-6"),
            descriptionResId = R.string.air_data_h6_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air63.jpg",
            additionalImages = listOf("air63_extra1.jpg", "air63_extra2.jpg", "air63_extra3.jpg", "air63_extra4.jpg", "air63_extra5.jpg"),
            correct = "AH-6 Little Bird",
            options = listOf("V-22B Osprey", "Gazelle", "AH-6 Little Bird", "Xian H-20"),
            descriptionResId = R.string.air_data_ah6_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air64.jpg",
            additionalImages = listOf("air64_extra1.jpg", "air64_extra2.jpg", "air64_extra3.jpg", "air64_extra4.jpg"),
            correct = "CSH-2 Rooivalk",
            options = listOf("CSH-2 Rooivalk", "Changhe-Z-10", "A129 Mangusta", "Tigr HAD"),
            descriptionResId = R.string.air_data_rooivalk_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air65.jpg",
            additionalImages = listOf("air28_extra1.jpg", "air28_extra2.jpg", "air28_extra3.jpg", "air28_extra4.jpg"),
            correct = "H225M Caracal",
            options = listOf("H225M Caracal", "NH90 Caïman", "Changhe Z-20", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_h225m_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air66.jpg",
            additionalImages = listOf("air10_extra1.jpg", "air10_extra2.jpg", "air10_extra3.jpg", "air10_extra4.jpg", "air10_extra5.jpg"),
            correct = "MI-8",
            options = listOf("NH90 Caïman", "UH-60 Black Hawk", "MI-8", "MI-24"),
            descriptionResId = R.string.air_data_mi8_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air67.jpg",
            additionalImages = listOf("air67_extra1.jpg", "air67_extra2.webp", "air67_extra3.webp"),
            correct = "NH90 Caïman",
            options = listOf("H225M Caracal", "NH90 Caïman", "MI-24", "SA 330 PUMA"),
            descriptionResId = R.string.air_data_nh90_description,
            moreInfo = null,
            category = "air"
        ),
        Question(
            image = "air68.jpg",
            additionalImages = listOf("air68_extra1.jpg", "air68_extra2.jpg", "air68_extra3.jpg", "air68_extra4.jpg"),
            correct = "V-22B Osprey",
            options = listOf("H225M Caracal", "KA-27 Helix", "TU-22M Backfire", "V-22B Osprey"),
            descriptionResId = R.string.air_data_v22_description,
            moreInfo = null,
            category = "air"
        )
    )
}