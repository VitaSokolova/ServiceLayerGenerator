package common

enum class RequestParamType(name: String) {
    QUERY("Query"),
    PATH("Path"),
    PART("Part"),
    BODY("Body")
}