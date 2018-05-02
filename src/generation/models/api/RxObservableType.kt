package generation.models.api

enum class RxObservableType(val observableName: String) {
    SINGLE("Single"),
    OBSERVABLE("Observable"),
    MAYBE("Maybe"),
    COMPLETABLE("Completable")
}