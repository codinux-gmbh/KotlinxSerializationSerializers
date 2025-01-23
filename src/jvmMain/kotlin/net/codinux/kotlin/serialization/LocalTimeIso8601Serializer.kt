package net.codinux.kotlin.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

/**
 * A serializer for [LocalTime] that uses the ISO 8601 representation.
 *
 * JSON example: `"12:01:03.999"`
 *
 * @see LocalDate.parse
 * @see LocalDate.toString
 */
object LocalTimeIso8601Serializer : KSerializer<LocalTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.time.LocalTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime =
        LocalTime.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

}