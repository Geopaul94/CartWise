package com.geo.cartwise.domain.model

/**
 * Maps a grocery item name to its store aisle using keyword matching.
 * Rules run in priority order — first match wins so "frozen peas" → Frozen
 * instead of Produce, and "orange juice" → Beverages instead of Produce.
 * Classification is intentionally simple and offline — no ML, no server call.
 */
object Aisle {
    const val PRODUCE = "Produce"
    const val DAIRY = "Dairy"
    const val MEAT_SEAFOOD = "Meat & Seafood"
    const val BAKERY = "Bakery"
    const val FROZEN = "Frozen"
    const val BEVERAGES = "Beverages"
    const val SNACKS = "Snacks"
    const val PERSONAL_CARE = "Personal Care"
    const val HOUSEHOLD = "Household"
    const val PANTRY = "Pantry"
    const val OTHER = "Other"

    private val rules: List<Pair<List<String>, String>> = listOf(
        listOf("frozen") to FROZEN,
        listOf(
            "beef", "chicken", "pork", "lamb", "turkey", "steak", "mince", "sausage",
            "bacon", "ham", "chop", "fillet", "fish", "salmon", "tuna", "shrimp", "prawn",
            "seafood", "meat", "wing"
        ) to MEAT_SEAFOOD,
        listOf(
            "milk", "cheese", "butter", "yogurt", "yoghurt", "cream", "egg", "curd"
        ) to DAIRY,
        listOf(
            "apple", "banana", "orange", "grape", "strawberr", "blueberr", "raspberry",
            "mango", "pineapple", "carrot", "potato", "tomato", "onion", "lettuce",
            "spinach", "kale", "broccoli", "cauliflower", "celery", "cucumber", "zucchini",
            "capsicum", "mushroom", "garlic", "ginger", "lemon", "lime", "avocado",
            "corn", "pea", "salad", "fruit", "vegetable", "herb", "capsicum", "beetroot"
        ) to PRODUCE,
        listOf(
            "bread", "roll", "bun", "bagel", "muffin", "cake", "pastry", "croissant",
            "loaf", "toast", "pita", "tortilla", "wrap", "bakery"
        ) to BAKERY,
        listOf(
            "juice", "soda", "cola", "beer", "wine", "coffee", "tea", "energy drink",
            "kombucha", "lemonade", "sparkling", "water", "drink"
        ) to BEVERAGES,
        listOf(
            "chips", "crisps", "cookie", "chocolate", "candy", "popcorn",
            "nuts", "cracker", "biscuit", "granola", "pretzel", "snack"
        ) to SNACKS,
        listOf(
            "shampoo", "conditioner", "soap", "toothpaste", "toothbrush", "deodorant",
            "body wash", "lotion", "moisturiser", "moisturizer", "razor", "sunscreen",
            "lip balm", "tampon", "feminine", "sanitiser", "sanitizer"
        ) to PERSONAL_CARE,
        listOf(
            "detergent", "laundry", "dishwasher", "dish soap", "bleach", "paper towel",
            "toilet paper", "tissue", "trash bag", "garbage bag", "sponge", "foil",
            "plastic wrap", "ziploc", "zip lock", "cleaner", "cleaning", "bin liner"
        ) to HOUSEHOLD,
        listOf(
            "rice", "pasta", "noodle", "flour", "sugar", "salt", "oil", "vinegar",
            "sauce", "ketchup", "mustard", "mayonnaise", "mayo", "soup", "cereal", "oat",
            "honey", "jam", "jelly", "syrup", "lentil", "chickpea", "spice", "seasoning",
            "canned", "tin", "jar", "stock", "broth", "pickle"
        ) to PANTRY,
    )

    fun classify(name: String): String {
        val lower = name.lowercase()
        for ((keywords, aisle) in rules) {
            if (keywords.any { lower.contains(it) }) return aisle
        }
        return OTHER
    }
}
