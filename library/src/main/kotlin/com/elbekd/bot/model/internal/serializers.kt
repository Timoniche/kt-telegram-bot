package com.elbekd.bot.model.internal

import com.elbekd.bot.model.ChatId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class ChatIdSerializer : KSerializer<ChatId> {
    private val strategy by lazy { ChatId.serializer() }

    override val descriptor: SerialDescriptor
        get() = strategy.descriptor

    override fun deserialize(decoder: Decoder): ChatId {
        try {
            val id = decoder.decodeString()
            return ChatId.StringId(id)
        } catch (ignored: ClassCastException) {
        }

        try {
            val id = decoder.decodeLong()
            return ChatId.IntegerId(id)
        } catch (ignored: ClassCastException) {
        }

        throw RuntimeException("Cannot deserialize chat_id")
    }

    override fun serialize(encoder: Encoder, value: ChatId) {
        when (value) {
            is ChatId.IntegerId -> encoder.encodeLong(value.id)
            is ChatId.StringId -> encoder.encodeString(value.id)
        }
    }
}