package com.example.demo.http.siren

import com.fasterxml.jackson.annotation.JsonProperty

@JvmInline
value class LinkRelation(
    val value: String
)

data class SirenModel<T>(
    @get:JsonProperty("class")
    val clazz: List<String>,
    val properties: T,
    val entities: List<EntityModel<*>>,
    val links: List<LinkModel>,
    val actions: List<ActionModel>
) {
    companion object {
        const val MEDIA_TYPE = "application/vnd.siren+json"
    }
}

data class EntityModel<T>(
    val properties: T,
    val links: List<LinkModel>,
    val rel: List<String>
)

data class LinkModel(
    val rel: List<String>,
    val href: String
)

data class ActionModel(
    val name: String,
    val href: String,
    val method: String,
    val fields: List<FieldModel>
)

data class FieldModel(
    val name: String,
    val type: String?,
    val value: String? = null
)