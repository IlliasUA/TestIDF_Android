package legOS.testidf.screens

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class ParticipantWaitingScreenTest {

    @Test
    fun percentageForDisplay_isIndependentOfDecimalSeparator() {
        val previousLocale = Locale.getDefault()

        try {
            Locale.setDefault(Locale.FRENCH)

            assertEquals(67, percentageForDisplay(66.666))
        } finally {
            Locale.setDefault(previousLocale)
        }
    }
}
