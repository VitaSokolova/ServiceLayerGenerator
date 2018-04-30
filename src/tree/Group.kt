package tree

class Group(val name: String,
            val uri: String,
            val params: List<UriParam>,
            val methods: List<Method>,
            val comment: String) {
}