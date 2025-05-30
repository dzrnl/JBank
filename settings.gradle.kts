rootProject.name = "dzrnl"

include("bank")
include("bank:data")
include("bank:business")
include("bank:presentation")

include("api-gateway")
include("api-gateway:business")
include("api-gateway:data")
include("api-gateway:presentation")

include("storage")