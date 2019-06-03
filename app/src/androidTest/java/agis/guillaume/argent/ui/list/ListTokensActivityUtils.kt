package agis.guillaume.argent.ui.list

import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import javax.inject.Provider


object ListTokensActivityUtils {

    @JvmStatic
    // inject the mock and the created ViewModelFactory (with the PostUseCase mocked) into the view
    fun initDispatcher(
        vmFactory: ListTokensViewModel.Factory,
        listTokensAdapter: ListTokensAdapterImpl
    ): DispatchingAndroidInjector<Activity> {
        return createFakeActivityInjector { viewModelFactory = vmFactory
            tokenAdapter = listTokensAdapter
        }
    }

    // utils
    private fun createFakeActivityInjector(block: ListTokensActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {
        val injector = AndroidInjector<Activity> { instance ->
            if (instance is ListTokensActivity) {
                instance.block()
            }
        }
        val factory = AndroidInjector.Factory<Activity> { injector }
        val map = mapOf(
            Pair<Class<*>,
                    Provider<AndroidInjector.Factory<*>>>(ListTokensActivity::class.java, Provider { factory })
        )
        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(map, emptyMap(), emptyMap(), emptyMap())
    }
}