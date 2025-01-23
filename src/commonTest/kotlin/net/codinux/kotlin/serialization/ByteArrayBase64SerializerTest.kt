package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ByteArrayBase64SerializerTest {

    companion object {
        private const val Unencoded = "Liebe"

        private const val ExpectedJson = """{"bytes":"TGllYmU="}"""
    }

    @Serializable
    data class ByteArrayTestClass(
        @Serializable(with = ByteArrayBase64Serializer::class)
        val bytes: ByteArray
    )

    private val json = Json


    @Test
    fun deserialize() {
        val result: ByteArrayTestClass = json.decodeFromString(ExpectedJson)

        assertThat(result.bytes).isEqualTo(Unencoded.encodeToByteArray())
    }

    @Test
    fun serialize() {
        val testClass = ByteArrayTestClass(Unencoded.encodeToByteArray())

        val result = json.encodeToString(testClass)

        assertThat(result).isEqualTo(ExpectedJson)
    }

}