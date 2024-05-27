import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  static const platform = MethodChannel('com.example.mupdf_viewer/pdf');

  const MyHomePage({super.key});

  Future<void> _openPdfViewer() async {
    try {
      await platform.invokeMethod('openPdfViewer');
    } on PlatformException catch (e) {
      print("Failed to open PDF viewer: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('PDF Viewer Example'),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: _openPdfViewer,
          child: const Text('Open PDF'),
        ),
      ),
    );
  }
}
