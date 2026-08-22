package com.geo.cartwise.domain.usecase

/**
 * Turns one spoken phrase into separate item names — "eggs and bread, milk"
 * becomes ["eggs", "bread", "milk"]. Splitting rule lives here (not in the
 * ViewModel) so it's covered by its own unit tests and reusable if a second
 * voice-entry surface ever needs the same parsing.
 */
class ParseSpokenItemsUseCase {
    private val separators = Regex("""\s*(,|&|\band\b)\s*""", RegexOption.IGNORE_CASE)

    operator fun invoke(spokenText: String): List<String> {
        return spokenText
            .split(separators)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}
