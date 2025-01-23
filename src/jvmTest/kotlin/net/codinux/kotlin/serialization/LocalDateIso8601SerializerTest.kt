package net.codinux.kotlin.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.test.Test

class LocalDateIso8601SerializerTest {

    companion object {
        private val ExpectedDate = LocalDate.of(2016, 10, 21)
        private val ExpectedDateIsoString = "2016-10-21"

        private val ExpectedJson = """{"date":"$ExpectedDateIsoString"}"""
    }


    @Serializable
    class LocalDateTestClass(
        @Serializable(with = LocalDateIso8601Serializer::class)
        val date: LocalDate
    )


    private val json = Json


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

}