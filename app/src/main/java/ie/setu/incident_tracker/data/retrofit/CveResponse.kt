data class CveResponse(
    val vulnerabilities: List<Vulnerability>
)

data class Vulnerability(
    val cve: CveDetail
)

data class CveDetail(
    val id: String,
    val descriptions: List<Description>,
    val metrics: Metrics
)

data class Description(
    val lang: String,
    val value: String
)

data class Metrics(
    val cvssMetricV31: List<CvssV31> = emptyList()
)

data class CvssV31(
    val cvssData: CvssV31Data
)

data class CvssV31Data(
    val baseScore: Double,
    val baseSeverity: String
)
