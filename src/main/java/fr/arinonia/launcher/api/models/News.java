package fr.arinonia.launcher.api.models;

import java.util.Objects;

public class News {
    private final String id;
    private final String title;
    private final String content;
    private final String imageUrl;
    private final String author;
    private final NewsType type;
    private final String createdAt;
    private final boolean important;

    public News(final String id, final String title, final String content, final String imageUrl, final String author,
                final String type, final String createdAt, final boolean important) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.author = author;
        this.type = NewsType.fromValue(type);
        this.createdAt = createdAt;
        this.important = important;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getAuthor() {
        return this.author;
    }

    public NewsType getType() {
        return this.type;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public boolean isImportant() {
        return this.important;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final News news = (News) obj;
        return Objects.equals(this.id, news.id) && Objects.equals(this.title, news.title)
                && Objects.equals(this.content, news.content) && Objects.equals(this.imageUrl, news.imageUrl)
                && Objects.equals(this.author, news.author) && this.type == news.type
                && Objects.equals(this.createdAt, news.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public enum NewsType {
        ANNOUNCEMENT("announcement", "Annonce"),
        UPDATE("update", "Mise à jour"),
        EVENT("event", "Événement"),
        MAINTENANCE("maintenance", "Maintenance"),
        DEVELOPMENT("development", "Développement"),
        OTHER("other", "Autre");

        private final String value;
        private final String displayName;

        NewsType(final String value, final String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public String getValue() {
            return this.value;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static NewsType fromValue(final String value) {
            for (final NewsType type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return OTHER;
        }
    }
}
