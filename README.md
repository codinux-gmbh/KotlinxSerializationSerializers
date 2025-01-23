# Kotlinx Serialization Serializer

Serializers for [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) not provided by kotlinx-serialization 
like for java.time classes or serializing/deserializing ByteArray to/from Base64.

Hint: If you need serializers for Jackson for kotlinx-datetime look at 
[KotlinxDateTimeJacksonModule](https://github.com/codinux-gmbh/KotlinxDateTimeJacksonModule).


## Setup

### Gradle

```
implementation("net.codinux.kotlin.serialization:kotlinx-serialization-serializers:1.0.0")
```


## Usage

### java.time Types

Serializers for java.time types like `LocalDate` and `Instant`:

```kotlin
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
```

Outputs:

```json
{
    "date": "2016-10-21",
    "time": "11:27:31",
    "dateTime": "2016-10-21T11:27:31",
    "instant": "2016-10-21T00:00:00Z"
}
```

### ByteArray to Base64 serializer

Encode/Decode `ByteArray` to/from Base64 like XML or most webservices do:

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codinux.kotlin.serialization.ByteArrayBase64Serializer

@Serializable
data class DtoWithByteArray(
    @Serializable(with = ByteArrayBase64Serializer::class)
    val bytes: ByteArray
)

fun serializeByteArrayToBase64() {
    val dto = DtoWithByteArray("Liebe".encodeToByteArray())

    val json = Json { prettyPrint = true }

    val encoded = json.encodeToString(dto)

    println(encoded)
}
```

Outputs:

```json
{
    "bytes": "TGllYmU="
}
```


## License

```
Copyright 2025 codinux GmbH & Co. KG

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```