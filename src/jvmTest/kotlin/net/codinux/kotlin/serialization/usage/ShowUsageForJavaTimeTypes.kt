package net.codinux.kotlin.serialization.usage

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codinux.kotlin.serialization.InstantIso8601Serializer
import net.codinux.kotlin.serialization.LocalDateIso8601Serializer
import net.codinux.kotlin.serialization.LocalDateTimeIso8601Serializer
import net.codinux.kotlin.serialization.LocalTimeIso8601Serializer
import java.time.*

@Serializable
data class JavaTimeTypes(

    @Serializable(with = LocalDateIso8601Serializer::class)
    val date: LocalDate,

    @Serializable(with = LocalTimeIso8601Serializer::class)
    val time: LocalTime,

    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val dateTime: LocalDateTime,

    @Serializable(with = InstantIso8601Serializer::class)
    val instant: Instant

)

class ShowUsageForJavaTimeTypes {

    fun serializeJavaTimeTypes() {
        val javaTimeTypes = JavaTimeTypes(
            LocalDate.of(2016, 10, 21),
            LocalTime.of(11, 27, 31),
            LocalDateTime.of(2016, 10, 21, 11, 27, 31),
            LocalDate.of(2016, 10, 21).atStartOfDay().toInstant(ZoneOffset.UTC)
        )

        val json = Json { prettyPrint = true }

        val encoded = json.encodeToString(javaTimeTypes)

        println(encoded)
    }

}