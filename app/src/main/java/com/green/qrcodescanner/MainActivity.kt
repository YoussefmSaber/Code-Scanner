package com.green.qrcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.green.qrcodescanner.ui.theme.QrCodeScannerTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {
    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the barcode launcher
        barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
            viewModel.onScanResult(result.contents) // handles the data returned after the scan is complete
        }

        setContent {
            QrCodeScannerScreen(viewModel = viewModel) // Pass the state to the composable
        }
    }

    @Composable
    fun QrCodeScannerScreen(viewModel: MainViewModel) {
        val scanResult = viewModel.scanResult.collectAsState()
        QrCodeScannerTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                innerPadding
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    LinkifyText(
                        scanResult.value,
                        fontSize = 16.sp
                    ) // Display the updated state

                    ElevatedButton(
                        onClick = {
                            val options = ScanOptions()
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE) // setting the barcode format to Qr code
                            barcodeLauncher.launch(options)  // launching the qr code scanner through the camera
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (scanResult.value != "")
                            Text(
                                "Scan another code",
                                fontSize = 16.sp
                            )
                        else
                            Text(
                                "Scan code",
                                fontSize = 16.sp
                            )
                    }
                }
            }
        }
    }
}
