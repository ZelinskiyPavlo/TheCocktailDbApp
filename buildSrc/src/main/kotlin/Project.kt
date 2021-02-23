import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

inline val DependencyHandler.app: ProjectDependency
	get() = project(":app")

inline val DependencyHandler.domain: ProjectDependency
	get() = project(":domain")

inline val DependencyHandler.hyperion: ProjectDependency
	get() = project(":hyperion")

inline val DependencyHandler.localization: ProjectDependency
	get() = project(":localization")

// core
inline val DependencyHandler.coreCommon: ProjectDependency
	get() = project(":core:common")

inline val DependencyHandler.coreCommonCocktail: ProjectDependency
	get() = project(":core:common:cocktail-common")

inline val DependencyHandler.coreDagger: ProjectDependency
	get() = project(":core:dagger")

inline val DependencyHandler.corePresentation: ProjectDependency
	get() = project(":core:presentation")

inline val DependencyHandler.coreStyling: ProjectDependency
	get() = project(":core:styling")

inline val DependencyHandler.coreNavigation: ProjectDependency
	get() = project(":core:navigation")

// data
inline val DependencyHandler.dataDatabase: ProjectDependency
	get() = project(":data:database")

inline val DependencyHandler.dataDatabaseImpl: ProjectDependency
	get() = project(":data:database:impl")

inline val DependencyHandler.dataNetwork: ProjectDependency
	get() = project(":data:network")

inline val DependencyHandler.dataNetworkImpl: ProjectDependency
	get() = project(":data:network:impl")

inline val DependencyHandler.dataLocal: ProjectDependency
	get() = project(":data:local")

inline val DependencyHandler.dataLocalPreference: ProjectDependency
	get() = project(":data:local:preference")

inline val DependencyHandler.dataRepository: ProjectDependency
	get() = project(":data:repository")

inline val DependencyHandler.dataRepositoryImpl: ProjectDependency
	get() = project(":data:repository:impl")

// platform
inline val DependencyHandler.platformFirebase: ProjectDependency
	get() = project(":platform:firebase")

inline val DependencyHandler.platformFirebaseFcm: ProjectDependency
	get() = project(":platform:firebase:fcm")

// feature
inline val DependencyHandler.featureAuth: ProjectDependency
	get() = project(":feature:auth")

inline val DependencyHandler.featureAuthLogin: ProjectDependency
	get() = project(":feature:auth:login")

inline val DependencyHandler.featureAuthRegister: ProjectDependency
	get() = project(":feature:auth:register")

inline val DependencyHandler.featureCocktail: ProjectDependency
	get() = project(":feature:cocktail")

inline val DependencyHandler.featureTabHost: ProjectDependency
	get() = project(":feature:tabhost")

inline val DependencyHandler.featureSearch: ProjectDependency
	get() = project(":feature:search")

inline val DependencyHandler.featureDetail: ProjectDependency
	get() = project(":feature:detail")

inline val DependencyHandler.featureSetting: ProjectDependency
	get() = project(":feature:setting")

inline val DependencyHandler.featureSettingProfile: ProjectDependency
	get() = project(":feature:setting:profile")

inline val DependencyHandler.featureSettingCube: ProjectDependency
	get() = project(":feature:setting:cube")

inline val DependencyHandler.featureSettingSeekBar: ProjectDependency
	get() = project(":feature:setting:seekbar")

