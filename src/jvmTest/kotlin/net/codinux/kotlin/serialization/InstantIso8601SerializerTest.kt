package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import net.codinux.kotlin.serialization.test.Serializers
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.Test

class InstantIso8601SerializerTest {

    companion object {
        private val ExpectedInstant = LocalDate.of(2016, 10, 21).atStartOfDay().toInstant(ZoneOffset.UTC)
        private val ExpectedInstantIsoString = "2016-10-21T00:00:00Z"
        private val ExpectedKotlinxInstant = kotlinx.datetime.Instant.parse(ExpectedInstantIsoString)

        private val ExpectedJson = """{"instant":"$ExpectedInstantIsoString"}"""
    }


    @Serializable
    class InstantTestClass(
        @JsonFormat(shape = JsonFormat.Shape.STRING) // so that Jackson uses ISO string instead of default encoding Instant as Decimal
        @Serializable(with = InstantIso8601Serializer::class)
        val instant: Instant
    )

    @Serializable
    class KotlinxInstantTestClass(
        val instant: kotlinx.datetime.Instant
    )


    private val json = Serializers.json

    private val jackson = Serializers.jackson


    @Test
    fun serialize() {
        val result = json.encodeToString(InstantTestClass(ExpectedInstant))

        assertThat(result).isEqualTo(ExpectedJson)
    }

    @Test
    fun deserialize() {
        val result = json.decodeFromString<InstantTestClass>(ExpectedJson)

        assertThat(result.instant).isEqualTo(ExpectedInstant)
    }


    /*      Test compatibility with values encoded by Jackson and with kotlinx.datetime.Instant       */

    @Test
    fun compareWithKotlinxInstant() {
        val javaUtilInstant = json.encodeToString(InstantTestClass(ExpectedInstant))

        val kotlinxInstant = json.encodeToString(KotlinxInstantTestClass(ExpectedKotlinxInstant))

        assertThat(javaUtilInstant).isEqualTo(kotlinxInstant)
    }

    @Test
    fun canBeDeserializedToKotlinxInstant() {
        val encodedWithJavaTimeInstant = json.encodeToString(InstantTestClass(ExpectedInstant))

        val decodedWithKotlinxInstant = json.decodeFromString<KotlinxInstantTestClass>(encodedWithJavaTimeInstant)

        assertThat(decodedWithKotlinxInstant.instant).isEqualTo(ExpectedKotlinxInstant)
    }

    @Test
    fun canDeserializeValueEncodedWithKotlinxInstant() {
        val encodedWithKotlinxInstant = json.encodeToString(KotlinxInstantTestClass(ExpectedKotlinxInstant))

        val decodedWithJavaTimeInstant = json.decodeFromString<InstantTestClass>(encodedWithKotlinxInstant)

        assertThat(decodedWithJavaTimeInstant.instant).isEqualTo(ExpectedInstant)
    }


    @Test
    fun canBeDeserializedByJackson() {
        val encodedByKotlinxSerialization = json.encodeToString(InstantTestClass(ExpectedInstant))

        val jacksonResult = jackson.readValue<InstantTestClass>(encodedByKotlinxSerialization)

        assertThat(jacksonResult.instant).isEqualTo(ExpectedInstant)
    }

    @Test
    fun canDeserializeValueEncodedByJackson() {
        val encodedByJackson = jackson.writeValueAsString(InstantTestClass(ExpectedInstant))

        val kotlinxSerializationResult = json.decodeFromString<InstantTestClass>(encodedByJackson)

        assertThat(kotlinxSerializationResult.instant).isEqualTo(ExpectedInstant)
    }

}