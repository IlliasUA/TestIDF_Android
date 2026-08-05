package legOS.testidf

import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

internal fun configureAppCheck() {
    FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
        PlayIntegrityAppCheckProviderFactory.getInstance()
    )
}
