package net.codinux.kotlin.serialization.test

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json

object Serializers {

    val json = Json

    val jackson = ObjectMapper().apply {
        findAndRegisterModules()
    }

}