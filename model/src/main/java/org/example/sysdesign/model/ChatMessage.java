package org.example.sysdesign.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;

import java.time.Instant;

/**
 * Record defining a chat message.
 *
 * @param topicId - The id of the chat topic the message is posted on.
 * @param timestamp - Representation of the time when the message is posted.
 * @param author - The id of the author of the message.
 * @param content - Content of the message, represented as a string.
 */
@RegisterForReflection
public record ChatMessage(@NotNull String topicId, @NotNull Instant timestamp, @NotNull String author,
                          @NotNull String content) {
}
