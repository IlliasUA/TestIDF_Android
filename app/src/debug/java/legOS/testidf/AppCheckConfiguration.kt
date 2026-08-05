package legOS.testidf

import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

internal fun configureAppCheck() {
    FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
        DebugAppCheckProviderFactory.getInstance()
    )
}
