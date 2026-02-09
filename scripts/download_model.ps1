$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path | Split-Path -Parent
$assetsDir = Join-Path $projectRoot "app\src\main\assets"
$destModel = Join-Path $assetsDir "person_detection.tflite"
$url = "https://github.com/tensorflow/models/raw/master/research/object_detection/test_images/ssd_mobilenet_v1_1_metadata_1.tflite"

if (-not (Test-Path $assetsDir)) { New-Item -ItemType Directory -Path $assetsDir | Out-Null }

Write-Host "Downloading model using certutil..."
# certutil is a built-in Windows tool that is very reliable for downloads
certutil.exe -urlcache -split -f $url $destModel

if (Test-Path $destModel) {
    $size = (Get-Item $destModel).Length
    if ($size -gt 1000) {
        Write-Host "Success! Model installed to $destModel ($size bytes)"
        Write-Host "Please Clean and Rebuild your project in Android Studio."
    } else {
        Write-Error "Download failed: File is too small. Please check your internet connection."
    }
} else {
    Write-Error "Download failed: File not created."
}
