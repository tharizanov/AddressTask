package com.example.task.address.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.task.address.domain.Feature
import com.example.task.address.network.repository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AddressScreen() {

    var searchText by remember { mutableStateOf("") }
    var features by remember { mutableStateOf<List<Feature>?>(null) }
    var showDropdown by remember { mutableStateOf(false) }
    var mapPosition by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        mapPosition?.let {
            position = CameraPosition.fromLatLngZoom(it, 10f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val apiResponse = repository.searchAddresses(query = it)
                            features = apiResponse.features?.filter { feature ->
                                /*feature.properties?.type?.contains("house", true) ==*/ true
                            }
                        } catch (e: HttpException) {
                            e.printStackTrace()
                        }
                    }

                    showDropdown = features.isNullOrEmpty().not()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            features?.forEach { feature ->
                DropdownMenuItem(
                    text = {
                        Text(feature.properties?.label?.ifEmpty { null } ?: "no label")
                    },
                    onClick = {
                        showDropdown = false
                        feature.geometry?.coordinates?.ifEmpty { null }?.let {
                            val latLng = LatLng(it[1], it[0])
                            mapPosition = latLng
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 11f)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            mapPosition?.let {
                Marker(state = MarkerState(it))
            }
        }
    }
}
