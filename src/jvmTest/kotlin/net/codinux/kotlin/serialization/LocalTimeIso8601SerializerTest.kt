package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import net.codinux.kotlin.serialization.test.Serializers
import java.time.LocalTime
import kotlin.test.Test

class LocalTimeIso8601SerializerTest {

    companion object {
        private val ExpectedTime = LocalTime.of(11, 27, 31, 48)
        private val ExpectedTimeIsoString = "11:27:31.000000048"
        private val ExpectedKotlinxTime = kotlinx.datetime.LocalTime.parse(ExpectedTimeIsoString)

        private val ExpectedJson = """{"time":"$ExpectedTimeIsoString"}"""
    }


    @Serializable
    class LocalTimeTestClass(
        @JsonFormat(shape = JsonFormat.Shape.STRING) // so that Jackson uses ISO string instead of default encoding LocalTime as IntArray
        @Serializable(with = LocalTimeIso8601Serializer::class)
        val time: LocalTime
    )

    @Serializable
    class KotlinxLocalTimeTestClass(
        val time: kotlinx.datetime.LocalTime
    )


    private val json = Serializers.json

    private val jackson = Serializers.jackson


    @Test
    fun serialize() {
        val result = json.encodeToString(LocalTimeTestClass(ExpectedTime))

        assertThat(result).isEqualTo(ExpectedJson)
    }

    @Test
    fun deserialize() {
        val result = json.decodeFromString<LocalTimeTestClass>(ExpectedJson)

        assertThat(result.time).isEqualTo(ExpectedTime)
    }


    /*      Test compatibility with values encoded by Jackson and with kotlinx.datetime.LocalTime       */

    @Test
    fun compareWithKotlinxLocalTime() {
        val javaUtilTime = json.encodeToString(LocalTimeTestClass(ExpectedTime))

        val kotlinxTime = json.encodeToString(KotlinxLocalTimeTestClass(ExpectedKotlinxTime))

        assertThat(javaUtilTime).isEqualTo(kotlinxTime)
    }

    @Test
    fun canBeDeserializedToKotlinxLocalTime() {
        val encodedWithJavaTimeLocalTime = json.encodeToString(LocalTimeTestClass(ExpectedTime))

        val decodedWithKotlinxLocalTime = json.decodeFromString<KotlinxLocalTimeTestClass>(encodedWithJavaTimeLocalTime)

        assertThat(decodedWithKotlinxLocalTime.time).isEqualTo(ExpectedKotlinxTime)
    }

    @Test
    fun canDeserializeValueEncodedWithKotlinxLocalTime() {
        val encodedWithKotlinxLocalTime= json.encodeToString(KotlinxLocalTimeTestClass(ExpectedKotlinxTime))

        val decodedWithJavaTimeLocalTime = json.decodeFromString<LocalTimeTestClass>(encodedWithKotlinxLocalTime)

        assertThat(decodedWithJavaTimeLocalTime.time).isEqualTo(ExpectedTime)
    }


    @Test
    fun canBeDeserializedByJackson() {
        val encodedByKotlinxSerialization = json.encodeToString(LocalTimeTestClass(ExpectedTime))

        val jacksonResult = jackson.readValue<LocalTimeTestClass>(encodedByKotlinxSerialization)

        assertThat(jacksonResult.time).isEqualTo(ExpectedTime)
    }

    @Test
    fun canDeserializeValueEncodedByJackson() {
        val encodedByJackson = jackson.writeValueAsString(LocalTimeTestClass(ExpectedTime))

        val kotlinxSerializationResult = json.decodeFromString<LocalTimeTestClass>(encodedByJackson)

        assertThat(kotlinxSerializationResult.time).isEqualTo(ExpectedTime)
    }

}