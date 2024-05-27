package com.example.mupdf_viewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.artifex.mupdf.viewer.DocumentActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    companion object {
        private const val FILE_REQUEST = 42
        private const val CHANNEL = "com.example.mupdf_viewer/pdf"
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "openPdfViewer") {
                openFilePicker()
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "application/vnd.ms-xpsdocument",
                "application/oxps",
                "application/x-cbz",
                "application/vnd.comicbook+zip",
                "application/epub+zip",
                "application/x-fictionbook",
                "application/x-mobipocket-ebook",
                "application/octet-stream"
            ))
        }
        startActivityForResult(intent, FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val intent = Intent(this, DocumentActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                    addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    action = Intent.ACTION_VIEW
                    setDataAndType(data.data, data.type)
                    putExtra(componentName.packageName + ".ReturnToLibraryActivity", 1)
                }
                startActivity(intent)
            }
            finish()
        } else if (requestCode == FILE_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }
}
