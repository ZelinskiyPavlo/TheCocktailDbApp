rootProject.name = "TheCocktailDb"

include(":app")

include(":core")
include(":core:common")
include(":core:common:cocktail-common")
include(":core:dagger")
include(":core:presentation")
include(":core:styling")
include(":core:navigation")

include(":localization")

include(":data")
include(":data:database")
include(":data:database:impl")
include(":data:local")
include(":data:local:preference")
include(":data:network")
include(":data:network:impl")
include(":data:repository")
include(":data:repository:impl")

include(":feature")
include(":feature:auth")
include(":feature:auth:register")
include(":feature:auth:login")

include(":feature:splash")
include(":feature:cocktail")
include(":feature:detail")
include(":feature:search")
include(":feature:setting")
include(":feature:tabhost")
include(":feature:setting:seekbar")
include(":feature:setting:cube")
include(":feature:setting:profile")

include(":platform")
include(":platform:firebase:fcm")
include(":platform:firebase")
