object BuildArgs {
    const val applicationId = "app.getorca.b2c"
    const val versionName = "1.36.0"
    const val versionCode = 77
    const val stagingPrivateApiUrl = "https://test-api.myorcas.com/api/"

    override fun toString(): String {
        return """
            "applicationId": "$applicationId",
            "versionName": "$versionName",
            "versionCode": "$versionCode",
            "stagingPrivateApiUrl": "$stagingPrivateApiUrl",
        """.trimIndent()
    }
}
