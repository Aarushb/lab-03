package com.example.listycity3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme

// Top-level screen for the app. cities is what MainActivity currently holds; onAddCity and
// onUpdateCity report what the user did back up to CityRepository, this screen never touches the
// list itself. The City/Province fields and button below are reused for both adding and editing -
// selecting a row switches them into edit mode instead of showing a second form.
@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    val editing = selectedCity != null

    fun closeFields() {
        showAddCityFields = false
        selectedCity = null
        newCityName = ""
        newProvinceName = ""
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .semantics {
                        // The visible glyph is just "+", which reads fine when fields are hidden
                        // but says nothing about what a second tap does, so state the action
                        // explicitly for screen readers instead.
                        contentDescription = if (showAddCityFields) {
                            "Hide city fields"
                        } else {
                            "Show city fields"
                        }
                    },
                onClick = {
                    if (showAddCityFields) closeFields() else showAddCityFields = true
                },
            ) {
                Text("+")
            }
        }

        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text(if (editing) "Updated City" else "City") },
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text(if (editing) "Updated Province" else "Province") },
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            val updated = City(name = newCityName, province = newProvinceName)
                            val currentlySelected = selectedCity
                            if (currentlySelected != null) {
                                onUpdateCity(currentlySelected, updated)
                            } else {
                                onAddCity(updated)
                            }
                            closeFields()
                        }
                    },
                ) {
                    Text(if (editing) "Update City" else "Add City")
                }
            }
        }

        // selectableGroup() tells accessibility services the rows below behave like a
        // single-choice group, so TalkBack announces each one's selected state
        LazyColumn(modifier = Modifier.fillMaxSize().selectableGroup()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    selected = city == selectedCity,
                    onClick = {
                        if (selectedCity == city) {
                            closeFields()
                        } else {
                            selectedCity = city
                            newCityName = city.name
                            newProvinceName = city.province
                            showAddCityFields = true
                        }
                    },
                )

                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

// Displays one city as a name/province row.
// Uses Modifier.selectable alongside a background color;
// so TalkBack announces whether the row is selected, in addition to the visual highlight.
@Composable
fun CityRow(
    city: City,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val textColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .semantics {
                contentDescription =
                    "${city.name}, ${city.province}, ${if (selected) "selected" else "not selected"}"
            }
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            color = textColor,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            color = textColor,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB"),
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> },
        )
    }
}
