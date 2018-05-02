package tree

import common.MethodType
import common.RequestParamType

class MockDataTreeGenerator {

    fun generateMockTree(): DataTree {
        return DataTree("TestLama",
                "https://lamaapi-staging.azurewebsites.net/", generateMockGroups(), generateMockModels())
    }

    private fun generateMockModels(): MutableList<Model> {
        val result: MutableList<Model> = arrayListOf()
        result.addAll(0, arrayListOf(createCategoryModel(), createShopInfoModel()))
        return result
    }

    private fun generateMockGroups(): MutableList<Group> {
        val result: MutableList<Group> = arrayListOf()
        result.addAll(0, arrayListOf(createCatalogGroup(), createShopGroup()))
        return result
    }

    private fun createCategoryModel() = Model("Category", arrayListOf(
            Field("id", "String", "sdfsd235235"),
            Field("observableName", "String"),
            Field("pic", "String"),
            Field("color", "String", "#6BE379"),
            Field("sub", "Category", isRequired = false, isCollection = true)))

    private fun createShopInfoModel() = Model("ShopInfo", arrayListOf(
            Field("distance", "Double", comment = "Расстояние от клиента до магазина в километрах", isRequired = false),
            Field("pinIcon", "String", comment = "Иконка для карты"),
            Field("price", "Double", comment = "Стоимость доставки"),
            Field("eta", "Int", comment = "Время доставки в минутах", isRequired = false),
            Field("tags", "ShopTags", isCollection = true)))

    private fun createCatalogGroup(): Group {
        return Group("Catalog",
                "/categories/",
                arrayListOf(UriParam("shopId", RequestParamType.PATH, "String", true)),
                arrayListOf(Method("CategoryList", MethodType.GET, "", response = Response(200, MediaType.JSON, bodyAsModelRef = "Category", isCollection = true))),
                "Каталог товаров")

    }

    private fun createShopGroup(): Group {
        return Group("Shops",
                "/shops",
                arrayListOf(UriParam("location", RequestParamType.QUERY, "String", true, comment = "coordinates, divided in {lat},{lon} format. For example Red Square is at 'location=55.754082,37.620526'."),
                UriParam("group", RequestParamType.QUERY, "String", false),
                UriParam("type", RequestParamType.QUERY, "String", false)),
                arrayListOf(Method("ShopList", MethodType.GET, "", response = Response(200, MediaType.JSON, bodyAsModelRef = "ShopInfo", isCollection = true))),
                "Магазины")

    }
}