# CartWise — Project Handoff

Kotlin/Jetpack Compose Android app. `com.geo.cartwise`. Clean Architecture (data/domain/presentation).
Source spec: `C:\Users\geopa\.claude\scratch\cyclewise\Building Apps\13 - CartWise Grocery List\13_CartWise_Grocery_List.pdf`
(spec recommends Flutter — built in Kotlin/Compose instead, per Geo's choice).

Repo: `D:\git clones\CartWise` — remote `https://github.com/Geopaul94/CartWise.git`, branch `main`.

## Scope decision (2026-08-22)
Full spec parity for v1, including Firebase family sync. Play Store assets (icon, screenshots,
feature graphic, listing copy, privacy policy) are generated **after** all feature sprints land,
so screenshots reflect the finished UI.

## Completed sprints
1. Scaffold — Room DB, grocery lists + items, add/check/delete, Material 3 theme
2. Multi-store lists — arbitrary named lists (supermarket, chemist, club) via `ListsScreen`/`ListCard`
3. Barcode scan — ML Kit-style camera scan → Open Food Facts API lookup → auto-fill item name
4. Voice input — Android `RecognizerIntent` (system dialog, no RECORD_AUDIO permission needed) →
   `ParseSpokenItemsUseCase` splits "eggs and bread" into separate items

## Remaining sprints (spec parity), in planned order
~~5. **Auto-Aisle Sorting**~~ ✅ done — `Aisle.kt` keyword classifier (11 categories), `GroceryItemEntity.aisle`,
   DB v3, grouped sticky-header `LazyColumn`, muted "Checked (N)" section at bottom
~~6. **Budget Tracker**~~ ✅ done — `estimatedPrice` on items, `budget` on lists (DB v4); `BudgetSummaryBar`
   (₹total/₹budget + progress bar, error color at 90%); `SetBudgetDialog` (₹ icon in top bar); price
   field added to `AddItemBar`; price shown on `GroceryItemRow` when > 0
7. **Smart Restock** — "Restock" action rebuilds last list's items in a new list with one tap
8. **Spend History** — monthly spend log by category (depends on sprint 6's price field)
9. **Home screen widget** — Glance AppWidget showing active list, tap item to check off without opening app
10. **Smart item entry autosuggest** — suggest name/brand/qty from local purchase history as user types
11. **Family Sync** — Firebase Realtime Database, share list via link, real-time cross-device checkoff
12. **Monetization** — AdMob (free tier) + Play Billing premium ($1.99/mo, $9.99/yr) gating lists>1,
    family sync, barcode scan, budget tracker, spend history, widget, ad removal
13. **Final polish pass** — screen-by-screen checklist from global CLAUDE.md (loading/empty/error/success,
    dark mode, 360dp width, animations, touch targets), real app icon
14. **Play Store assets** — icon, screenshots, feature graphic, listing copy, privacy policy, signed AAB

## Known gotchas
- No `AndroidManifest.xml` RECORD_AUDIO permission — intentional, voice uses system speech dialog.
- No project-level `key.properties`/keystore yet — needed before sprint 14.
- App icon is still the default Compose template icon — real icon comes in sprint 13.
- `strings.xml` only has `app_name` — no other strings extracted yet.

## Build env
JAVA_HOME=`C:\Program Files\Android\Android Studio\jbr` (no `java` on PATH). See global memory
`android-build-install-env-windows` for adb/device details.
