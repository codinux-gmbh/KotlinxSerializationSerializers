package net.codinux.kotlin.serialization.usage

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codinux.kotlin.serialization.ByteArrayBase64Serializer

@Serializable
data class DtoWithByteArray(
    @Serializable(with = ByteArrayBase64Serializer::class)
    val bytes: ByteArray
)

class ShowUsageForByteArrayBase64Serializer {

    fun serializeByteArrayToBase64() {
        val dto = DtoWithByteArray("Liebe".encodeToByteArray())

        val json = Json { prettyPrint = true }

        val encoded = json.encodeToString(dto)

        println(encoded)
    }
}