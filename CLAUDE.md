# CartWise — Project Handoff

Kotlin/Jetpack Compose Android app. Package: `com.geo.cartwise`.
Clean Architecture: data / domain / presentation layers, use-cases, repositories.
Source spec: `C:\Users\geopa\.claude\scratch\cyclewise\Building Apps\13 - CartWise Grocery List\13_CartWise_Grocery_List.pdf`
(spec recommends Flutter — built in Kotlin/Compose per Geo's choice.)

Repo: `D:\git clones\CartWise` — remote `https://github.com/Geopaul94/CartWise.git`, branch `main`.

---

## Scope (decided 2026-08-22)

Full spec parity for v1, including Firebase family sync. Play Store assets (icon, screenshots,
feature graphic, listing copy, privacy policy) generated **after** all feature sprints so screenshots
reflect the finished UI.

---

## Completed sprints

| # | Name | What landed |
|---|------|-------------|
| 1 | Scaffold | Room DB v1, `GroceryList` + `GroceryItem` entities, add/check/delete, Material 3 green theme |
| 2 | Multi-store lists | Arbitrary named lists (supermarket, chemist, club) via `ListsScreen` / `ListCard` |
| 3 | Barcode scan | ML Kit camera → Open Food Facts API → auto-fill item name; `BarcodeScanViewModel` |
| 4 | Voice input | `RecognizerIntent` system dialog (no RECORD_AUDIO perm); `ParseSpokenItemsUseCase` splits "eggs and bread" into separate items |
| 5 | Auto-aisle sorting | `Aisle.kt` keyword classifier (11 categories); `GroceryItemEntity.aisle`; DB v3; grouped sticky-header `LazyColumn`; muted "Checked (N)" section |
| 6 | Budget tracker | `estimatedPrice` on items, `budget` on lists (DB v4); `BudgetSummaryBar` (₹total/₹budget + progress bar, error at 90%); `SetBudgetDialog`; price field in `AddItemBar`; price on `GroceryItemRow` |
| 7 | Smart restock | ↺ icon in top bar (disabled when nothing checked); `RestockConfirmDialog`; `RestockListUseCase` → `uncheckAll(listId)` bulk UPDATE |
| 8 | Spend History | `SpendHistoryScreen` + `SpendHistoryViewModel`; `MonthPicker` (last 6 months); `SpendCategoryRow` with fraction progress bar; grand-total card; `observeSpendHistory()` DAO query (checked items grouped by YYYY-MM + aisle); ₹ chart icon in `ListsScreen` top bar navigates to history |
| 9 | Home Screen Widget | `CartWiseWidget` (GlanceAppWidget) shows unchecked items from the busiest list; `CartWiseWidgetReceiver`; `ToggleItemActionCallback` checks off an item without opening the app; `GroceryItemDao` extended with `getById`, `getUncheckedByList`, `getListIdWithMostUnchecked`; `GroceryListDao` extended with `getNameById`; `AppContainer` exposes DAOs for widget access; registered in AndroidManifest |
| 10 | Smart Autosuggest | `getSuggestedNames` DAO query (DISTINCT name LIKE query, newest-first, limit 5); `SuggestItemNamesUseCase`; 300ms debounce in `GroceryListViewModel` via `Job` cancel/relaunch; `suggestions` field in `GroceryListUiState`; `ItemSuggestionDropdown` (`DropdownMenu` anchored to name field); `AddItemBar` wraps field in `Box`; selecting a suggestion fills `inputText` and clears dropdown |

---

## Remaining sprints

### Sprint 8 — Spend History
Monthly spend log: how much was spent per aisle category, built from checked items' estimated prices.

**Key decisions:**
- No new DB table needed — query `grocery_items` where `isChecked = 1` and group by month + aisle
- New screen `SpendHistoryScreen` reachable from `ListsScreen` (trailing icon or menu)
- Month selector (previous 6 months), bar chart or category breakdown per month

**New files to create:**
- `domain/model/SpendRecord.kt` — `(month: String, aisle: String, total: Double)`
- `domain/usecase/ObserveSpendHistoryUseCase.kt` — DAO query grouping checked items by month + aisle
- `data/local/dao/SpendHistoryDao.kt` (or add query to `GroceryItemDao`)
- `presentation/history/SpendHistoryScreen.kt`
- `presentation/history/SpendHistoryViewModel.kt`
- `presentation/history/components/SpendCategoryRow.kt`
- `presentation/history/components/MonthPicker.kt`

**Nav:** add route `"spend_history"` in `CartWiseNavHost`; back arrow returns to lists.

---

### Sprint 9 — Home Screen Widget
Glance AppWidget showing the active list's unchecked items. Tap an item → toggle checked without opening the app.

**Key decisions:**
- Use `androidx.glance:glance-appwidget` (already in ecosystem, no extra dependency conflicts)
- Widget shows the list with the most unchecked items (or the most recently opened list — TBD at sprint start)
- Widget state sourced from Room directly via `GlanceStateDefinition`

**New files to create:**
- `widget/CartWiseWidget.kt` — `GlanceAppWidget` subclass
- `widget/CartWiseWidgetReceiver.kt` — `GlanceAppWidgetReceiver`
- `res/xml/cart_wise_widget_info.xml` — widget metadata (min size, update period)

**Manifest additions:** `<receiver>` for the widget, `<meta-data>` for widget info.

**Dependency to add:** `implementation("androidx.glance:glance-appwidget:<version>")`

---

### Sprint 10 — Smart Item Entry Autosuggest
As the user types in `AddItemBar`, suggest previously-added item names from purchase history.

**Key decisions:**
- Source: `grocery_items` table itself (names already there) — no separate history table needed
- DAO query: `SELECT DISTINCT name FROM grocery_items WHERE name LIKE :query LIMIT 5 ORDER BY createdAt DESC`
- Show suggestions in a `DropdownMenu` anchored to the name `OutlinedTextField`
- Selecting a suggestion fills the name + auto-classifies aisle

**New files to create:**
- `domain/usecase/SuggestItemNamesUseCase.kt`
- `presentation/list/components/ItemSuggestionDropdown.kt`

**Modified files:** `AddItemBar.kt`, `GroceryListUiState.kt`, `GroceryListViewModel.kt`

---

### Sprint 11 — Family Sync (Firebase Realtime Database)
Share a list via link. Partner checks items off in real time from another device.

**Key decisions:**
- Firebase RTDB (not Firestore) — spec says "Firebase Realtime Database"
- Sharing = generating a shareable key for a list; other device enters/taps link → joins the list
- Sync strategy: RTDB is source of truth for shared lists; Room is local cache
- Local-only lists (not shared) remain Room-only — no Firebase writes

**New dependencies:** `firebase-database-ktx`, `firebase-analytics-ktx` (required by BOM)
**google-services.json** must be placed in `app/` before this sprint (Geo to provide Firebase project)

**New files to create:**
- `data/remote/FirebaseSyncRepository.kt`
- `domain/repository/SyncRepository.kt`
- `domain/usecase/ShareListUseCase.kt`
- `domain/usecase/JoinSharedListUseCase.kt`
- `presentation/list/components/ShareListBottomSheet.kt` — shows share link + copy button

**Modified files:** `GroceryListScreen.kt` (share icon in top bar), `CartWiseNavHost.kt`

---

### Sprint 12 — Monetization (AdMob + Play Billing)
Free tier: 1 active list + manual item entry + banner ads.
Premium ($1.99/mo or $9.99/yr): unlimited lists, family sync, barcode scan, budget tracker, spend history, widget, no ads.

**Key decisions:**
- Use `com.android.billingclient:billing-ktx` for Play Billing
- Store premium state in `EncryptedSharedPreferences` (verified against Play Billing on each launch)
- AdMob banner at the bottom of `ListsScreen` (free users only)
- Gate logic lives in a `PremiumRepository` checked by use-cases, not scattered in screens

**New files to create:**
- `data/billing/PremiumRepositoryImpl.kt`
- `domain/repository/PremiumRepository.kt`
- `domain/usecase/ObservePremiumStatusUseCase.kt`
- `domain/usecase/PurchasePremiumUseCase.kt`
- `presentation/premium/PremiumScreen.kt` — paywall / upgrade screen
- `presentation/common/components/AdBanner.kt`

**AdMob app ID** must be in `AndroidManifest.xml` `<meta-data>` before shipping.

---

### Sprint 13 — Final Polish Pass
Screen-by-screen pass against the global CLAUDE.md polish checklist.

**Checklist per screen (ListsScreen, GroceryListScreen, BarcodeScannerScreen, SpendHistoryScreen, PremiumScreen):**
- [ ] Theme tokens only — no hardcoded colors/sizes/text styles
- [ ] Correct in light AND dark mode
- [ ] Real loading / empty / error / success states
- [ ] State changes animate (150–300ms)
- [ ] Touch targets ≥ 48dp; system insets respected (`WindowInsets`)
- [ ] Reads correctly at 360dp width
- [ ] Real app icon (replace default Compose template icon)
- [ ] `strings.xml` — extract all user-visible strings
- [ ] ProGuard rules reviewed for Retrofit, Room, Firebase, Glance

---

### Sprint 14 — Play Store Assets + Signed Release
Generate everything needed to submit to Play Console.

**Checklist:**
- [ ] Keystore: `cartwise-upload.jks`, password `Geopaul@7557`, alias `upload`, validity 36500 days
- [ ] `key.properties` in project root (gitignored)
- [ ] `versionCode = 1`, `versionName = "1.0.0"`
- [ ] `./gradlew bundleRelease` → AAB at `app/build/outputs/bundle/release/`
- [ ] Backup folder: `D:\PlayStoreBackups\cartwise_drive_playstore_backup\`
  - `cartwise-upload.jks`, `key.properties`, `README-CARTWISE.md`
  - `CartWise-v1.0.0-release.aab`
  - `store-assets/` (icon, feature graphic, screenshots)
  - `store-listing-text.txt`, `privacy-policy-text.txt`, `local.properties.backup`
- [ ] Privacy policy hosted at `geopaul94.github.io` in its own `cartwise-privacy` repo
- [ ] Play Console: create app, fill listing, upload AAB, submit for review

**Store listing assets needed:**
- App icon: 512×512 PNG (green theme, shopping cart motif)
- Feature graphic: 1024×500 PNG
- Screenshots: at least 2 phone screenshots (min 320px wide)
- Short description (80 chars max), full description (4000 chars max)

---

## Architecture notes

| Layer | Location | Rule |
|-------|----------|------|
| Data | `data/local/`, `data/remote/`, `data/repository/` | Room entities, DAOs, Retrofit, Firebase; **no domain imports** |
| Domain | `domain/model/`, `domain/repository/`, `domain/usecase/` | Pure Kotlin; no Android/Room/Firebase imports |
| Presentation | `presentation/<feature>/` | ViewModels, Screens, Components; imports domain only |

**DI:** Manual (`AppContainer`) — no Hilt yet. Graduate to Hilt when graph gets complex.

---

## DB versions

| Version | Change |
|---------|--------|
| 1 | Initial schema: `grocery_lists`, `grocery_items` |
| 2 | (internal migration, pre-release) |
| 3 | Added `grocery_items.aisle TEXT NOT NULL DEFAULT 'Other'` |
| 4 | Added `grocery_items.estimatedPrice REAL DEFAULT 0`, `grocery_lists.budget REAL DEFAULT 0` |

Using `fallbackToDestructiveMigration()` — safe while pre-release. Switch to explicit `Migration` objects before v1 launch (Sprint 14 checklist).

---

## Known gotchas

- No `RECORD_AUDIO` permission in `AndroidManifest.xml` — intentional (voice uses system speech dialog).
- No `key.properties` / keystore yet — created in Sprint 14.
- App icon is still the default Compose template — real icon in Sprint 13.
- `strings.xml` only has `app_name` — full extraction in Sprint 13.
- `google-services.json` not yet present — needed for Sprint 11 (Firebase). Geo must create a Firebase project and drop the file into `app/`.
- AdMob app ID not yet in Manifest — added in Sprint 12.

---

## Build environment

- **JAVA_HOME:** `C:\Program Files\Android\Android Studio\jbr` (no `java` on PATH)
- **adb:** `C:\Users\geopa\AppData\Local\Android\Sdk\platform-tools\adb.exe`
- **Test device:** Redmi `24094RAD4I` over WiFi — two adb transport entries, target explicitly with `-s 192.168.1.73:<port>`
- See global memory `android-build-install-env-windows` for full device quirks
