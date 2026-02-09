Steps to get a working person_detection.tflite model for this project

1) Download a ready-made TFLite object-detection model (recommended for beginners)
   - Open: https://www.tensorflow.org/lite/models/object_detection/overview
   - Recommended models:
     * ssd_mobilenet_v2_fpnlite_320x320_coco (small, fast)
     * efficientdet_lite0 (better accuracy)
   - On the model page, find a "TFLite" or "Download" link and download the .tflite file. Some downloads come as a ZIP; extract it.

2) Rename the model file (if needed)
   - The app expects the filename: person_detection.tflite
   - Rename the downloaded .tflite to exactly: person_detection.tflite

3) Place the file into this project
   - Copy the person_detection.tflite into:
     app/src/main/assets/person_detection.tflite
   - If the assets/ folder was just created, rebuild the app so Gradle packages the asset.

4) (Optional) Use a quantized model for performance
   - Prefer a uint8/quantized tflite if available (marked as "quantized" on the model page).
   - Quantized models run faster on older phones.

5) Rebuild and run on device
   - In Android Studio: Build > Clean Project, then Build > Rebuild Project, then Run on your device.

6) Troubleshooting
   - If the app shows a Toast that the model is missing, confirm the filename & location.
   - If detection is slow or inaccurate: try a smaller/quantized model, reduce camera resolution, or process fewer frames.

Automated model installer (easy, recommended)

If you'd like, you can run the included PowerShell helper to download or copy a model into the correct place. This avoids committing large binaries to the repo and is beginner-friendly.

Location (script):
  scripts\download_model.ps1

How it works:
  - You can either pass a direct .tflite download URL, a path to a local .tflite file, or a ZIP / TAR.GZ archive that contains a .tflite.
  - The script will extract (if needed), locate the first .tflite it finds, rename it to exactly `person_detection.tflite`, and copy it into:
      app/src/main/assets/person_detection.tflite

Usage examples (PowerShell) - copy & paste into a PowerShell window run from the project root (C:\Users\ASUS\StudioProjects\CrowdMonitoringAppNew):

# 1) Download a direct .tflite URL and install
# Replace <DIRECT_TFLITE_URL> with the direct download link you found on TF Hub or other trusted source
powershell -ExecutionPolicy Bypass -File .\scripts\download_model.ps1 -Url "<DIRECT_TFLITE_URL>"

# 2) Use a local file you already downloaded
powershell -ExecutionPolicy Bypass -File .\scripts\download_model.ps1 -LocalPath "C:\Users\ASUS\Downloads\model.tflite"

# 3) If you downloaded a ZIP or TAR.GZ (script will extract and search for a .tflite inside)
powershell -ExecutionPolicy Bypass -File .\scripts\download_model.ps1 -LocalPath "C:\Users\ASUS\Downloads\efficientdet-tensorflow2-d0-v1.tar.gz"

Notes:
  - The script will create the assets folder if it does not exist and will overwrite any existing person_detection.tflite.
  - If you prefer, you can manually copy a .tflite file into the assets folder instead of running the script.

If you'd like, reply 'Run script' and I will provide the exact command to run with a URL you paste, or I can run local checks to confirm the file was installed. If you still want me to commit a model binary into the repo, say explicitly 'Commit model' (note: large binaries in Git may be undesirable; prefer using the script).
