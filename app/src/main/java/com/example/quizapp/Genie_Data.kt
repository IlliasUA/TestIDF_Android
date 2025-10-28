package com.example.quizapp

object Genie_Data {
    val QUESTION = listOf(
        Question(
            image = "genie1.jpg",
            additionalImages = listOf("genie1_extra1.webp", "genie1_extra2.jpg", "genie1_extra3.jpg"),
            correct = "SPRAT",
            options = listOf("SPRAT", "PMM-2", "DACHS", "EFA"),
            descriptionResId = legOS.testidf.R.string.genie_sprat_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie2.webp",
            additionalImages = listOf("genie2_extra1.jpg", "genie2_extra2.jpg"),
            correct = "Biber",
            options = listOf("Leguan", "Buffalo", "Biber", "PZM-3"),
            descriptionResId = legOS.testidf.R.string.genie_biber_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie3.webp",
            additionalImages = listOf("genie3_extra1.jpg", "genie3_extra2.jpg", "genie3_extra3.jpg", "genie3_extra4.jpg"),
            correct = "Buffalo",
            options = listOf("SPRAT", "Buffalo", "MDK-2", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_buffalo_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie4.webp",
            additionalImages = listOf("genie4_extra1.webp", "genie4_extra2.jpg", "genie4_extra3.webp", "genie4_extra4.jpg"),
            correct = "DACHS",
            options = listOf("GMZ", "Buffalo", "DACHS", "Biber"),
            descriptionResId = legOS.testidf.R.string.genie_dachs_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie5.webp",
            additionalImages = listOf("genie4_extra1.webp", "genie4_extra2.jpg", "genie4_extra3.webp", "genie4_extra4.jpg"),
            correct = "DACHS",
            options = listOf("SPRAT", "M9 ACE", "PZM-3", "DACHS"),
            descriptionResId = legOS.testidf.R.string.genie_dachs_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie8.webp",
            additionalImages = listOf("genie8_extra1.webp", "genie8_extra2.jpg", "genie8_extra3.webp", "genie8_extra4.jpg"),
            correct = "GSP",
            options = listOf("GMZ", "PTS", "GSP", "M3 Amphibius"),
            descriptionResId = legOS.testidf.R.string.genie_gsp_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie9.jpg",
            additionalImages = listOf("genie8_extra1.webp", "genie8_extra2.jpg", "genie8_extra3.webp", "genie8_extra4.jpg"),
            correct = "GSP",
            options = listOf("GMZ", "PTS", "MDK-2", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_gsp_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie10.webp",
            additionalImages = listOf("genie10_extra1.webp", "genie10_extra2.webp"),
            correct = "IRM-2",
            options = listOf("IRM-2", "M9 ACE", "PZM-3", "DACHS"),
            descriptionResId = legOS.testidf.R.string.genie_irm2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie11.webp",
            additionalImages = listOf("genie10_extra1.webp", "genie10_extra2.webp"),
            correct = "IRM-2",
            options = listOf("IRM-2", "M9 ACE", "PMM-2", "PZM-3"),
            descriptionResId = legOS.testidf.R.string.genie_irm2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie12.jpg",
            additionalImages = listOf("genie12_extra1.jpg", "genie12_extra2.webp", "genie12_extra3.webp", "genie12_extra4.jpg", "genie12_extra5.jpg"),
            correct = "M3 Amphibius",
            options = listOf("EFA", "PTS", "M3 Amphibius", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_m3_amphibius_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie13.webp",
            additionalImages = listOf("genie12_extra1.jpg", "genie12_extra2.webp", "genie12_extra3.webp", "genie12_extra4.jpg", "genie12_extra5.jpg"),
            correct = "M3 Amphibius",
            options = listOf("GMZ", "M3 Amphibius", "EFA", "PTS"),
            descriptionResId = legOS.testidf.R.string.genie_m3_amphibius_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie14.webp",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("MDK-2", "IRM-2", "PZM-3", "M9 ACE"),
            descriptionResId = legOS.testidf.R.string.genie_m9_ace_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie15.webp",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("M9 ACE", "Buffalo", "PZM-3", "DACHS"),
            descriptionResId = legOS.testidf.R.string.genie_m9_ace_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie16.webp",
            additionalImages = listOf("genie14_extra1.webp", "genie14_extra2.webp", "genie14_extra3.webp", "genie14_extra4.webp"),
            correct = "M9 ACE",
            options = listOf("DACHS", "M9 ACE", "PZM-3", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_m9_ace_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie17.webp",
            additionalImages = listOf("genie17_extra1.webp", "genie17_extra2.webp", "genie17_extra3.webp"),
            correct = "MDK-2",
            options = listOf("PZM-3", "PMM-2", "MDK-2", "PTS"),
            descriptionResId = legOS.testidf.R.string.genie_mdk2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie18.webp",
            additionalImages = listOf("genie17_extra1.webp", "genie17_extra2.webp", "genie17_extra3.webp"),
            correct = "MDK-2",
            options = listOf("PZM-3", "PMM-2", "MDK-2", "PTS"),
            descriptionResId = legOS.testidf.R.string.genie_mdk2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie19.webp",
            additionalImages = listOf("genie19_extra1.jpg", "genie19_extra2.webp", "genie19_extra3.jpg"),
            correct = "PMM-2",
            options = listOf("Biber", "PMM-2", "M3 Amphibius", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_pmm2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie20.webp",
            additionalImages = listOf("genie19_extra1.jpg", "genie19_extra2.webp", "genie19_extra3.jpg"),
            correct = "PMM-2",
            options = listOf("EFA", "PMM-2", "M3 Amphibius", "SPRAT"),
            descriptionResId = legOS.testidf.R.string.genie_pmm2_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie21.webp",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("PTS", "M3 Amphibius", "Biber", "GMZ"),
            descriptionResId = legOS.testidf.R.string.genie_pts_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie22.webp",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("PTS", "M3 Amphibius", "Biber", "EFA"),
            descriptionResId = legOS.testidf.R.string.genie_pts_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie23.webp",
            additionalImages = listOf("genie21_extra1.jpg", "genie21_extra2.jpg", "genie21_extra3.jpg", "genie21_extra4.jpg"),
            correct = "PTS",
            options = listOf("IRM-2", "SPRAT", "Biber", "PTS"),
            descriptionResId = legOS.testidf.R.string.genie_pts_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie25.webp",
            additionalImages = listOf("genie25_extra1.jpg", "genie25_extra2.jpg", "genie25_extra3.webp"),
            correct = "AMX-30 EBG",
            options = listOf("AMX-30 SDPMAC", "AMX-30 EBG", "DACHS", "M9 ACE"),
            descriptionResId = legOS.testidf.R.string.genie_amx30_ebg_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie26.webp",
            additionalImages = listOf("genie26_extra1.jpg", "genie26_extra2.jpg", "genie26_extra3.jpg"),
            correct = "AMX-30 EBG SDPMAC",
            options = listOf("AMX-30 EBG SDPMAC", "AMX-30 EBG", "TC-910", "M9 ACE"),
            descriptionResId = legOS.testidf.R.string.genie_amx30_ebg_sdpmac_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie27.jpg",
            additionalImages = listOf("genie25_extra1.jpg", "genie25_extra2.jpg", "genie25_extra3.webp"),
            correct = "AMX-30 EBG",
            options = listOf("DACHS", "Buffalo", "GMZ", "AMX-30 EBG"),
            descriptionResId = legOS.testidf.R.string.genie_amx30_ebg_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie28.jpg",
            additionalImages = listOf("genie28_extra1.jpg", "genie28_extra2.jpg", "genie28_extra3.jpg", "genie28_extra4.jpg", "genie28_extra5.jpg"),
            correct = "EFA",
            options = listOf("M3 Amphibius", "PMM-2", "GSP", "EFA"),
            descriptionResId = legOS.testidf.R.string.genie_efa_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie29.jpg",
            additionalImages = listOf("genie28_extra1.jpg", "genie28_extra2.jpg", "genie28_extra3.jpg", "genie28_extra4.jpg", "genie28_extra5.jpg"),
            correct = "EFA",
            options = listOf("EFA", "PMM-2", "DACHS", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_efa_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie30.webp",
            additionalImages = listOf("genie30_extra1.jpg"),
            correct = "EGAME",
            options = listOf("TC-910", "EGAME", "TNA", "EGRAP"),
            descriptionResId = legOS.testidf.R.string.genie_egame_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie31.webp",
            additionalImages = listOf("genie31_extra1.webp"),
            correct = "EGRAP",
            options = listOf("TNA", "GMZ", "EGRAP", "Manitou"),
            descriptionResId = legOS.testidf.R.string.genie_egrap_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie32.webp",
            additionalImages = listOf("genie32.webp"),
            correct = "Manitou",
            options = listOf("TNA", "Manitou", "TC-910", "DACHS"),
            descriptionResId = legOS.testidf.R.string.genie_manitou_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie33.webp",
            additionalImages = listOf("genie33_extra1.jpg", "genie33_extra2.webp", "genie33_extra3.jpg"),
            correct = "PPLD",
            options = listOf("PPLD", "PVP", "URAL-4320", "GSP"),
            descriptionResId = legOS.testidf.R.string.genie_ppld_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie34.webp",
            additionalImages = listOf("genie34_extra1.jpg", "genie34_extra2.jpg", "genie34_extra3.jpg", "genie34_extra4.jpg"),
            correct = "VDM Souvim",
            options = listOf("SPRAT", "Buffalo", "VDM Souvim", "PPLD"),
            descriptionResId = legOS.testidf.R.string.genie_vdm_souvim_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie35.webp",
            additionalImages = listOf("genie35_extra1.jpg"),
            correct = "TC-910",
            options = listOf("EGAME", "EGRAP", "Manitou", "TC-910"),
            descriptionResId = legOS.testidf.R.string.genie_tc910_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie36.webp",
            additionalImages = listOf("genie36_extra1.jpg", "genie36_extra2.jpg"),
            correct = "TNA",
            options = listOf("TNA", "PTS", "TC-910", "VDM Souvim"),
            descriptionResId = legOS.testidf.R.string.genie_tna_desc,
            moreInfo = null,
            category = "genie"
        ),
        Question(
            image = "genie37.webp",
            additionalImages = listOf("genie34_extra1.jpg", "genie34_extra2.jpg", "genie34_extra3.jpg", "genie34_extra4.jpg"),
            correct = "VDM Souvim",
            options = listOf("TNA", "VDM Souvim", "EGRAP", "EGAME"),
            descriptionResId = legOS.testidf.R.string.genie_vdm_souvim_desc,
            moreInfo = null,
            category = "genie"
        )
    )
}