package legOS.testidf.utils

import android.content.Context
import com.example.quizapp.Question

/**
 * Extension function to get the description text from a Question object.
 * Tries to get from string resource first, falls back to hardcoded description.
 */
fun Question.getDescriptionText(context: Context): String? {
    return when {
        descriptionResId != null -> {
            try {
                context.getString(descriptionResId)
            } catch (e: Exception) {
                description
            }
        }
        else -> description
    }
}

/**
 * Extension function to get the more info text from a Question object.
 * Tries to get from string resource first, falls back to hardcoded moreInfo.
 */
fun Question.getMoreInfoText(context: Context): String? {
    return when {
        moreInfoResId != null -> {
            try {
                context.getString(moreInfoResId)
            } catch (e: Exception) {
                moreInfo
            }
        }
        else -> moreInfo
    }
}