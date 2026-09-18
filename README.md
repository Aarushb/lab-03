# CMPUT 301 : Lab 3 Participation Exercise

## Student Details

- **Full Name:** Aarush Bhat
- **CCID:** Aarush

## References and Resources

List any resources used here, or simply put `N/A` if not applicable.

- [CMPUT 301 Lab 3 instructions page](https://ualberta-cmput301.github.io/labs/lab3_inst.html)
- Lab 3 Slides, the Lab 3 Instructions walkthrough, and the Kotlin Code Conventions PDF (all linked from the instructions page above).
- [Jetpack Compose state docs](https://developer.android.com/develop/ui/compose/state) for how mutableStateListOf triggers recomposition.
- Be My Eyes' [BeMyAI](https://www.bemyeyes.com/bme-ai/) feature to describe the reference screenshots, same as Lab 2.
- [WCAG 2, level AA](https://www.w3.org/WAI/WCAG2AA-Conformance)

## Verbal Collaboration

| Student Name | CCID |
| ------------- | ---- |
| N/A | N/A |

## Implementation Notes

`ListyCity3` follows the Lab 3 instructions PDF for Part 1-3 (mutableStateListOf, addCity, the City/Province fields, the "+" FloatingActionButton), then extends the same fields for the participation exercise instead of building a second form for it:

- Tapping a row selects it and pre-fills the fields with that city's current name and province; the button switches from "Add City" to "Update City" and calls `CityRepository.updateCity` instead of `addCity`.
- Tapping the selected row again, or the "+" button while fields are open, clears the selection and closes the fields.
- Accessibility: city rows use `Modifier.selectable` inside a `selectableGroup()`, not just a background colour, so TalkBack announces each row's selected state
  - The "+" button's visible glyph is just a plus sign, so it gets a `contentDescription` that states what tapping it will actually do instead of leaving TalkBack to read "+" literally
  - Text still uses `sp` units throughout so it respects the system font-size setting

