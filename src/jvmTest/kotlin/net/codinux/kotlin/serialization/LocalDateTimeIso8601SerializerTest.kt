package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import net.codinux.kotlin.serialization.test.Serializers
import java.time.LocalDateTime
import kotlin.test.Test

class LocalDateTimeIso8601SerializerTest {

    companion object {
        private val ExpectedDateTime = LocalDateTime.of(2016, 10, 21, 11, 27, 31, 48)
        private val ExpectedDateTimeIsoString = "2016-10-21T11:27:31.000000048"
        private val ExpectedKotlinxDateTime = kotlinx.datetime.LocalDateTime.parse(ExpectedDateTimeIsoString)

        private val ExpectedJson = """{"dateTime":"$ExpectedDateTimeIsoString"}"""
    }


    @Serializable
    class LocalDateTimeTestClass(
        @JsonFormat(shape = JsonFormat.Shape.STRING) // so that Jackson uses ISO string instead of default encoding LocalDateTime as IntArray
        @Serializable(with = LocalDateTimeIso8601Serializer::class)
        val dateTime: LocalDateTime
    )

    @Serializable
    class KotlinxLocalDateTimeTestClass(
        val dateTime: kotlinx.datetime.LocalDateTime
    )


    private val json = Serializers.json

    private val jackson = Serializers.jackson


    @Test
    fun serialize() {
        val result = json.encodeToString(LocalDateTimeTestClass(ExpectedDateTime))

        assertThat(result).isEqualTo(ExpectedJson)
    }

    @Test
    fun deserialize() {
        val result = json.decodeFromString<LocalDateTimeTestClass>(ExpectedJson)

        assertThat(result.dateTime).isEqualTo(ExpectedDateTime)
    }


    /*      Test compatibility with values encoded by Jackson and with kotlinx.datetime.LocalDateTime       */

    @Test
    fun compareWithKotlinxDateTime() {
        val javaUtilDateTime = json.encodeToString(LocalDateTimeTestClass(ExpectedDateTime))

        val kotlinxDateTime = json.encodeToString(KotlinxLocalDateTimeTestClass(ExpectedKotlinxDateTime))

        assertThat(javaUtilDateTime).isEqualTo(kotlinxDateTime)
    }

    @Test
    fun canBeDeserializedToKotlinxLocalDateTime() {
        val encodedWithJavaTimeDateTime = json.encodeToString(LocalDateTimeTestClass(ExpectedDateTime))

        val decodedWithKotlinxDateTime = json.decodeFromString<KotlinxLocalDateTimeTestClass>(encodedWithJavaTimeDateTime)

        assertThat(decodedWithKotlinxDateTime.dateTime).isEqualTo(ExpectedKotlinxDateTime)
    }

    @Test
    fun canDeserializeValueEncodedWithKotlinxDateTime() {
        val encodedWithKotlinxDateTime = json.encodeToString(KotlinxLocalDateTimeTestClass(ExpectedKotlinxDateTime))

        val decodedWithJavaTimeDateTime = json.decodeFromString<LocalDateTimeTestClass>(encodedWithKotlinxDateTime)

        assertThat(decodedWithJavaTimeDateTime.dateTime).isEqualTo(ExpectedDateTime)
    }


    @Test
    fun canBeDeserializedByJackson() {
        val encodedByKotlinxSerialization = json.encodeToString(LocalDateTimeTestClass(ExpectedDateTime))

        val jacksonResult = jackson.readValue<LocalDateTimeTestClass>(encodedByKotlinxSerialization)

        assertThat(jacksonResult.dateTime).isEqualTo(ExpectedDateTime)
    }

    @Test
    fun canDeserializeValueEncodedByJackson() {
        val encodedByJackson = jackson.writeValueAsString(LocalDateTimeTestClass(ExpectedDateTime))

        val kotlinxSerializationResult = json.decodeFromString<LocalDateTimeTestClass>(encodedByJackson)

        assertThat(kotlinxSerializationResult.dateTime).isEqualTo(ExpectedDateTime)
    }

}