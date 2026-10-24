package ru.urfu.courseproject.entity;

/**
 * Роли пользователей приложения.
 * <ul>
 *     <li>READ_ONLY — просмотр данных без возможности изменения;</li>
 *     <li>USER — работа со своими записями;</li>
 *     <li>ADMIN — полный доступ ко всем данным и управление ролями.</li>
 * </ul>
 */
public enum Role {
    READ_ONLY("Только чтение"),
    USER("Пользователь"),
    ADMIN("Администратор");

    private final String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
