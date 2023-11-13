Get-Content .env | ForEach-Object {
    $key, $value = $_ -split '=', 2
    Set-Item -Path env:$key -Value $value
}