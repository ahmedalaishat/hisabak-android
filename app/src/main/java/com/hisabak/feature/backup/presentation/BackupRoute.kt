package com.hisabak.feature.backup.presentation

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BackupRoute(
    modifier: Modifier = Modifier,
    viewModel: BackupViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var importUri by remember { mutableStateOf<Uri?>(null) }
    var importName by remember { mutableStateOf<String?>(null) }
    var pendingExportPassphrase by remember { mutableStateOf<String?>(null) }

    // SAF: create the destination file, then write the encrypted bytes the use case produced.
    val createLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream"),
    ) { uri ->
        val passphrase = pendingExportPassphrase
        pendingExportPassphrase = null
        if (uri != null && passphrase != null) {
            viewModel.export(passphrase) { bytes ->
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
                        ?: throw IOException("Could not open output stream")
                }
            }
        }
    }

    val openLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            importUri = uri
            importName = context.displayName(uri) ?: uri.lastPathSegment
        }
    }

    BackupScreen(
        state = state,
        importFileName = importName,
        onExport = { passphrase ->
            pendingExportPassphrase = passphrase
            createLauncher.launch(defaultBackupFileName())
        },
        onChooseImportFile = { openLauncher.launch(arrayOf("*/*")) },
        onRestore = { passphrase ->
            val uri = importUri ?: return@BackupScreen
            viewModel.import(passphrase) {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        ?: throw IOException("Could not open input stream")
                }
            }
        },
        onDismissResult = viewModel::clearResult,
        modifier = modifier,
    )
}

private fun defaultBackupFileName(): String =
    "hisabak-backup-${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)}.hisabak"

private fun Context.displayName(uri: Uri): String? =
    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(0) else null
    }
