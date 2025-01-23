package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import net.codinux.kotlin.serialization.test.Serializers
import java.time.LocalDate
import kotlin.test.Test

class LocalDateIso8601SerializerTest {

    companion object {
        private val ExpectedDate = LocalDate.of(2016, 10, 21)
        private val ExpectedDateIsoString = "2016-10-21"
        private val ExpectedKotlinxDate = kotlinx.datetime.LocalDate.parse(ExpectedDateIsoString)

        private val ExpectedJson = """{"date":"$ExpectedDateIsoString"}"""
    }


    @Serializable
    class LocalDateTestClass(
        @JsonFormat(shape = JsonFormat.Shape.STRING) // so that Jackson uses ISO string instead of default encoding LocalDate as IntArray
        @Serializable(with = LocalDateIso8601Serializer::class)
        val date: LocalDate
    )

    @Serializable
    class KotlinxLocalDateTestClass(
        val date: kotlinx.datetime.LocalDate
    )


    private val json = Serializers.json

    private val jackson = Serializers.jackson


    @Test
    fun serialize() {
        val result = json.encodeToString(LocalDateTestClass(ExpectedDate))

        assertThat(result).isEqualTo(ExpectedJson)
    }

    @Test
    fun deserialize() {
        val result = json.decodeFromString<LocalDateTestClass>(ExpectedJson)

        assertThat(result.date).isEqualTo(ExpectedDate)
    }


    /*      Test compatibility with values encoded by Jackson and with kotlinx.datetime.LocalDate       */

    @Test
    fun compareWithKotlinxDateTime() {
        val javaUtilDate = json.encodeToString(LocalDateTestClass(ExpectedDate))

        val kotlinxDate = json.encodeToString(KotlinxLocalDateTestClass(ExpectedKotlinxDate))

        assertThat(javaUtilDate).isEqualTo(kotlinxDate)
    }

    @Test
    fun canBeDeserializedToKotlinxLocalDate() {
        val encodedWithJavaTimeDate = json.encodeToString(LocalDateTestClass(ExpectedDate))

        val decodedWithKotlinxDate = json.decodeFromString<KotlinxLocalDateTestClass>(encodedWithJavaTimeDate)

        assertThat(decodedWithKotlinxDate.date).isEqualTo(ExpectedKotlinxDate)
    }

    @Test
    fun canDeserializeValueEncodedWithKotlinxDate() {
        val encodedWithKotlinxDate = json.encodeToString(KotlinxLocalDateTestClass(ExpectedKotlinxDate))

        val decodedWithJavaTimeDate = json.decodeFromString<LocalDateTestClass>(encodedWithKotlinxDate)

        assertThat(decodedWithJavaTimeDate.date).isEqualTo(ExpectedDate)
    }


    @Test
    fun canBeDeserializedByJackson() {
        val encodedByKotlinxSerialization = json.encodeToString(LocalDateTestClass(ExpectedDate))

        val jacksonResult = jackson.readValue<LocalDateTestClass>(encodedByKotlinxSerialization)

        assertThat(jacksonResult.date).isEqualTo(ExpectedDate)
    }

    @Test
    fun canDeserializeValueEncodedByJackson() {
        val encodedByJackson = jackson.writeValueAsString(LocalDateTestClass(ExpectedDate))

        val kotlinxSerializationResult = json.decodeFromString<LocalDateTestClass>(encodedByJackson)

        assertThat(kotlinxSerializationResult.date).isEqualTo(ExpectedDate)
    }

}