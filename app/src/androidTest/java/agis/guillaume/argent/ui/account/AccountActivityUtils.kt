package agis.guillaume.argent.ui.account

import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import javax.inject.Provider


object AccountActivityUtils {

    @JvmStatic
    // inject the mock and the created ViewModelFactory (with the PostUseCase mocked) into the view
    fun initDispatcher(vmFactory: AccountViewModel.Factory): DispatchingAndroidInjector<Activity> {
        return createFakeActivityInjector { viewModelFactory = vmFactory }
    }

    // utils
    private fun createFakeActivityInjector(block: AccountActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {
        val injector = AndroidInjector<Activity> { instance ->
            if (instance is AccountActivity) {
                instance.block()
            }
        }
        val factory = AndroidInjector.Factory<Activity> { injector }
        val map = mapOf(
            Pair<Class<*>,
                    Provider<AndroidInjector.Factory<*>>>(AccountActivity::class.java, Provider { factory })
        )
        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(map, emptyMap(), emptyMap(), emptyMap())
    }
}